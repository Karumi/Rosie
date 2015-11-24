/*
 * Copyright (C) 2015 Karumi.
 */

package com.karumi.rosie.repositorynew;

import android.support.annotation.NonNull;
import com.karumi.rosie.UnitTest;
import com.karumi.rosie.doubles.AnyRepositoryKey;
import com.karumi.rosie.doubles.AnyRepositoryValue;
import com.karumi.rosie.repositorynew.datasource.CacheDataSource;
import com.karumi.rosie.repositorynew.datasource.ReadableDataSource;
import com.karumi.rosie.repositorynew.datasource.WriteableDataSource;
import com.karumi.rosie.repositorynew.policy.ReadPolicy;
import java.util.Collection;
import java.util.EnumSet;
import java.util.LinkedList;
import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mock;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNull;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class RepositoryTest extends UnitTest {

  private static final AnyRepositoryKey ANY_KEY = new AnyRepositoryKey(42);
  private static final AnyRepositoryValue ANY_VALUE = new AnyRepositoryValue(ANY_KEY);

  @Mock private ReadableDataSource<AnyRepositoryKey, AnyRepositoryValue> readableDataSource;
  @Mock private WriteableDataSource<AnyRepositoryKey, AnyRepositoryValue> writeableDataSource;
  @Mock private CacheDataSource<AnyRepositoryKey, AnyRepositoryValue> cacheDataSource;

  @Test public void shouldReturnNullIfThereAreNoDataSourcesWithData() throws Exception {
    givenCacheDataSourceReturnsNull();
    givenReadableDataSourceReturnsNull();
    Repository<?, ?> repository = givenAReadableAndCacheRepository();

    Collection<?> values = repository.getAll();

    assertNull(values);
  }

  @Test public void shouldReturnDataFromCacheDataSourceIfDataIsValid() throws Exception {
    Collection<AnyRepositoryValue> cacheValues = givenCacheDataSourceReturnsValidValues();
    givenReadableDataSourceReturnsNull();
    Repository<?, AnyRepositoryValue> repository = givenAReadableAndCacheRepository();

    Collection<AnyRepositoryValue> values = repository.getAll();

    assertEquals(cacheValues, values);
  }

  @Test public void shouldReturnDataFromReadableDataSourceIfCacheDataSourceReturnsNullOnGetAll()
      throws Exception {
    givenCacheDataSourceReturnsNull();
    Collection<AnyRepositoryValue> readableValues = givenReadableDataSourceReturnsValidValues();
    Repository<?, AnyRepositoryValue> repository = givenAReadableAndCacheRepository();

    Collection<AnyRepositoryValue> values = repository.getAll();

    Assert.assertEquals(readableValues, values);
  }

  @Test public void shouldReturnDataFromReadableDataSourceIfTheCacheDataSourceReturnsNoValidData()
      throws Exception {
    givenCacheDataSourceReturnsNonValidValues();
    Collection<AnyRepositoryValue> readableValues = givenReadableDataSourceReturnsValidValues();
    Repository<?, AnyRepositoryValue> repository = givenAReadableAndCacheRepository();

    Collection<AnyRepositoryValue> values = repository.getAll();

    Assert.assertEquals(readableValues, values);
  }

  @Test(expected = Exception.class) public void shouldPropagateExceptionsThrownByAnyDataSource()
      throws Exception {
    givenReadableDataSourceThrowsException();
    Repository<?, ?> repository = givenARepository(EnumSet.of(DataSource.READABLE));

    repository.getAll();
  }

  @Test public void shouldGetDataFromReadableDataSourceIfReadPolicyForcesOnlyReadable()
      throws Exception {
    givenCacheDataSourceReturnsValidValues();
    givenReadableDataSourceReturnsValidValues();
    Repository<?, AnyRepositoryValue> repository = givenAReadableAndCacheRepository();

    repository.getAll(ReadPolicy.READABLE_ONLY_AND_POPULATE);

    verify(cacheDataSource, never()).getAll();
    verify(readableDataSource).getAll();
  }

  @Test public void shouldPopulateCacheDataSources() throws Exception {
    givenCacheDataSourceReturnsNull();
    Collection<AnyRepositoryValue> readableValues = givenReadableDataSourceReturnsValidValues();
    Repository<?, AnyRepositoryValue> repository = givenAReadableAndCacheRepository();

    repository.getAll();

    verify(cacheDataSource).addOrUpdateAll(readableValues);
  }

  @Test public void shouldDeleteAllFromCacheDataSourceIfDataIsNotValid() throws Exception {
    givenCacheDataSourceReturnsNonValidValues();
    Repository<?, AnyRepositoryValue> repository = givenAReadableAndCacheRepository();

    repository.getAll();

    verify(cacheDataSource).deleteAll();
  }

  @Test(expected = IllegalArgumentException.class) public void shouldNotAcceptNullKeys()
      throws Exception {
    Repository<?, ?> repository = givenAReadableAndCacheRepository();

    repository.getByKey(null);
  }

  @Test public void shouldReturnValueByKeyFromCacheDataSource() throws Exception {
    givenReadableDataSourceReturnsNull();
    AnyRepositoryValue cacheValue = givenCacheDataSourceReturnsValidValueWithKey(ANY_KEY);
    Repository<AnyRepositoryKey, AnyRepositoryValue> repository =
        givenAReadableAndCacheRepository();

    AnyRepositoryValue value = repository.getByKey(ANY_KEY);

    Assert.assertEquals(cacheValue, value);
  }

  @Test public void shouldReturnValueFromReadableDataSourceIfCacheDataSourceValueIsNull()
      throws Exception {
    givenCacheDataSourceReturnsNull();
    AnyRepositoryValue readableValue = givenReadableDataSourceReturnsValidValueWithKey(ANY_KEY);
    Repository<AnyRepositoryKey, AnyRepositoryValue> repository =
        givenAReadableAndCacheRepository();

    AnyRepositoryValue value = repository.getByKey(ANY_KEY);

    Assert.assertEquals(readableValue, value);
  }

  @Test public void shouldReturnItemFromReadableDataSourceIfCacheDataSourceValueIsNotValid()
      throws Exception {
    givenCacheDataSourceReturnsNonValidValueWithKey(ANY_KEY);
    AnyRepositoryValue readableValue = givenReadableDataSourceReturnsValidValueWithKey(ANY_KEY);
    Repository<AnyRepositoryKey, AnyRepositoryValue> repository =
        givenAReadableAndCacheRepository();

    AnyRepositoryValue value = repository.getByKey(ANY_KEY);

    Assert.assertEquals(readableValue, value);
  }

  @Test public void shouldPopulateCacheDataSourceWithValueIfCacheDataSourceIsNotValid()
      throws Exception {
    givenCacheDataSourceReturnsNonValidValueWithKey(ANY_KEY);
    AnyRepositoryValue readableValue = givenReadableDataSourceReturnsValidValueWithKey(ANY_KEY);
    Repository<AnyRepositoryKey, AnyRepositoryValue> repository =
        givenAReadableAndCacheRepository();

    repository.getByKey(ANY_KEY);

    verify(cacheDataSource).addOrUpdate(readableValue);
  }

  @Test public void shouldDeleteValuesIfAreNotValid() throws Exception {
    givenCacheDataSourceReturnsNonValidValueWithKey(ANY_KEY);
    givenReadableDataSourceReturnsValidValueWithKey(ANY_KEY);
    Repository<AnyRepositoryKey, AnyRepositoryValue> repository =
        givenAReadableAndCacheRepository();

    repository.getByKey(ANY_KEY);

    verify(cacheDataSource).deleteByKey(ANY_KEY);
  }

  @Test public void shouldLoadItemFromReadableDataSourceIfReadPolicyForcesOnlyReadable()
      throws Exception {
    givenCacheDataSourceReturnsValidValueWithKey(ANY_KEY);
    givenReadableDataSourceReturnsValidValueWithKey(ANY_KEY);
    Repository<AnyRepositoryKey, AnyRepositoryValue> repository =
        givenAReadableAndCacheRepository();

    repository.getByKey(ANY_KEY, ReadPolicy.READABLE_ONLY_AND_POPULATE);

    verify(cacheDataSource, never()).getByKey(ANY_KEY);
    verify(readableDataSource).getByKey(ANY_KEY);
  }

  @Test(expected = IllegalArgumentException.class) public void shouldNotAddOrUpdateNullItems()
      throws Exception {
    Repository<?, AnyRepositoryValue> repository = givenAWriteableAndCacheRepository();

    repository.addOrUpdate(null);
  }

  @Test public void shouldAddOrUpdateItemToWriteableDataSource() throws Exception {
    givenWriteableDataSourceWritesValue(ANY_VALUE);
    Repository<?, AnyRepositoryValue> repository = givenAWriteableAndCacheRepository();

    repository.addOrUpdate(ANY_VALUE);

    verify(writeableDataSource).addOrUpdate(ANY_VALUE);
  }

  @Test public void shouldPopulateCacheDataSourceWithWriteableDataSourceResult() throws Exception {
    AnyRepositoryValue writeableValue = givenWriteableDataSourceWritesValue(ANY_VALUE);
    Repository<?, AnyRepositoryValue> repository = givenAWriteableAndCacheRepository();

    repository.addOrUpdate(ANY_VALUE);

    verify(cacheDataSource).addOrUpdate(writeableValue);
  }

  @Test public void shouldNotPopulateCacheDataSourceIfResultIsNotSuccessful() throws Exception {
    givenWriteableDataSourceDoesNotWriteValue(ANY_VALUE);
    Repository<?, AnyRepositoryValue> repository = givenAWriteableAndCacheRepository();

    repository.addOrUpdate(ANY_VALUE);

    verify(cacheDataSource, never()).addOrUpdate(any(AnyRepositoryValue.class));
  }

  @Test(expected = IllegalArgumentException.class) public void shouldNotAcceptNullValues()
      throws Exception {
    Repository<?, ?> repository = givenAWriteableAndCacheRepository();

    repository.addOrUpdate(null);
  }

  @Test public void shouldAddItemsToWriteableDataSource() throws Exception {
    Collection<AnyRepositoryValue> values = getSomeValues();
    givenWriteableDataSourceWritesValues(values);
    Repository<?, AnyRepositoryValue> repository = givenAWriteableAndCacheRepository();

    repository.addOrUpdateAll(values);

    verify(writeableDataSource).addOrUpdateAll(values);
  }

  @Test public void shouldPopulateCacheDataSourceWithWriteableDataSourceResults() throws Exception {
    Collection<AnyRepositoryValue> values = getSomeValues();
    Collection<AnyRepositoryValue> writeableValues = givenWriteableDataSourceWritesValues(values);
    Repository<?, AnyRepositoryValue> repository = givenAWriteableAndCacheRepository();

    repository.addOrUpdateAll(values);

    verify(cacheDataSource).addOrUpdateAll(writeableValues);
  }

  @Test public void shouldNotPopulateCacheDataSourceIfWriteableDataSourceResultIsNotSuccessful()
      throws Exception {
    Collection<AnyRepositoryValue> values = getSomeValues();
    givenWriteableDataSourceDoesNotWriteValues(values);
    Repository<?, AnyRepositoryValue> repository = givenAWriteableAndCacheRepository();

    repository.addOrUpdateAll(values);

    verify(cacheDataSource, never()).addOrUpdateAll(values);
  }

  @Test public void shouldDeleteAllDataSources() throws Exception {
    Repository<?, ?> repository = givenAWriteableAndCacheRepository();

    repository.deleteAll();

    verify(writeableDataSource).deleteAll();
    verify(cacheDataSource).deleteAll();
  }

  @Test public void shouldDeleteAllDataSourcesByKey() throws Exception {
    Repository<AnyRepositoryKey, ?> repository = givenAWriteableAndCacheRepository();

    repository.deleteByKey(ANY_KEY);

    verify(writeableDataSource).deleteByKey(ANY_KEY);
    verify(cacheDataSource).deleteByKey(ANY_KEY);
  }

  private Repository<AnyRepositoryKey, AnyRepositoryValue> givenAReadableAndCacheRepository() {
    return givenARepository(EnumSet.of(DataSource.READABLE, DataSource.CACHE));
  }

  private Repository<AnyRepositoryKey, AnyRepositoryValue> givenAWriteableAndCacheRepository() {
    return givenARepository(EnumSet.of(DataSource.WRITEABLE, DataSource.CACHE));
  }

  private Repository<AnyRepositoryKey, AnyRepositoryValue> givenARepository(
      EnumSet<DataSource> dataSources) {
    Repository<AnyRepositoryKey, AnyRepositoryValue> repository = new Repository<>();

    if (dataSources.contains(DataSource.READABLE)) {
      repository.addReadableDataSources(readableDataSource);
    }

    if (dataSources.contains(DataSource.WRITEABLE)) {
      repository.addWriteableDataSources(writeableDataSource);
    }

    if (dataSources.contains(DataSource.CACHE)) {
      repository.addCacheDataSources(cacheDataSource);
    }

    return repository;
  }

  private void givenCacheDataSourceReturnsNull() throws Exception {
    when(cacheDataSource.getByKey(any(AnyRepositoryKey.class))).thenReturn(null);
    when(cacheDataSource.getAll()).thenReturn(null);
  }

  private void givenReadableDataSourceReturnsNull() throws Exception {
    when(readableDataSource.getByKey(any(AnyRepositoryKey.class))).thenReturn(null);
    when(readableDataSource.getAll()).thenReturn(null);
  }

  private AnyRepositoryValue givenCacheDataSourceReturnsValidValueWithKey(AnyRepositoryKey key)
      throws Exception {
    return givenCacheDataSourceReturnsValueWithKey(key, true);
  }

  private AnyRepositoryValue givenCacheDataSourceReturnsNonValidValueWithKey(AnyRepositoryKey key)
      throws Exception {
    return givenCacheDataSourceReturnsValueWithKey(key, false);
  }

  @NonNull private AnyRepositoryValue givenCacheDataSourceReturnsValueWithKey(AnyRepositoryKey key,
      boolean isValidValue) {
    AnyRepositoryValue value = new AnyRepositoryValue(key);
    when(cacheDataSource.getByKey(key)).thenReturn(value);
    when(cacheDataSource.isValid(value)).thenReturn(isValidValue);
    return value;
  }

  @NonNull
  private AnyRepositoryValue givenReadableDataSourceReturnsValidValueWithKey(AnyRepositoryKey key) {
    AnyRepositoryValue value = new AnyRepositoryValue(key);
    when(readableDataSource.getByKey(key)).thenReturn(value);
    return value;
  }

  private Collection<AnyRepositoryValue> givenCacheDataSourceReturnsValidValues() throws Exception {
    return givenCacheDataSourceReturnsValues(true);
  }

  private Collection<AnyRepositoryValue> givenCacheDataSourceReturnsNonValidValues()
      throws Exception {
    return givenCacheDataSourceReturnsValues(false);
  }

  private Collection<AnyRepositoryValue> givenCacheDataSourceReturnsValues(boolean areValidValues) {
    Collection<AnyRepositoryValue> values = getSomeValues();
    when(cacheDataSource.getAll()).thenReturn(values);
    when(cacheDataSource.isValid(any(AnyRepositoryValue.class))).thenReturn(areValidValues);
    return values;
  }

  private Collection<AnyRepositoryValue> givenReadableDataSourceReturnsValidValues()
      throws Exception {
    Collection<AnyRepositoryValue> values = getSomeValues();
    when(readableDataSource.getAll()).thenReturn(values);
    return values;
  }

  private void givenReadableDataSourceThrowsException() {
    when(readableDataSource.getAll()).thenThrow(new Exception());
  }

  private AnyRepositoryValue givenWriteableDataSourceWritesValue(AnyRepositoryValue value) {
    AnyRepositoryValue writeableValue = new AnyRepositoryValue(value.getKey());
    when(writeableDataSource.addOrUpdate(value)).thenReturn(writeableValue);
    return writeableValue;
  }

  private void givenWriteableDataSourceDoesNotWriteValue(AnyRepositoryValue value) {
    when(writeableDataSource.addOrUpdate(value)).thenReturn(null);
  }

  private void givenWriteableDataSourceDoesNotWriteValues(Collection<AnyRepositoryValue> values) {
    when(writeableDataSource.addOrUpdateAll(values)).thenReturn(null);
  }

  private Collection<AnyRepositoryValue> givenWriteableDataSourceWritesValues(
      Collection<AnyRepositoryValue> values) {
    Collection<AnyRepositoryValue> updatedValues = new LinkedList<>(values);
    when(writeableDataSource.addOrUpdateAll(values)).thenReturn(values);
    return updatedValues;
  }

  private Collection<AnyRepositoryValue> getSomeValues() {
    LinkedList<AnyRepositoryValue> values = new LinkedList<>();
    for (int i = 0; i < 10; i++) {
      values.add(new AnyRepositoryValue(new AnyRepositoryKey(i)));
    }
    return values;
  }

  private enum DataSource {
    READABLE,
    WRITEABLE,
    CACHE
  }
}
