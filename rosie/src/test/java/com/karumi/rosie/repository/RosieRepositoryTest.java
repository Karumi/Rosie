/*
 * Copyright (C) 2015 Karumi.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.karumi.rosie.repository;

import android.support.annotation.NonNull;
import com.karumi.rosie.doubles.AnyRepositoryKey;
import com.karumi.rosie.doubles.AnyRepositoryValue;
import com.karumi.rosie.repository.datasource.CacheDataSource;
import com.karumi.rosie.repository.datasource.ReadableDataSource;
import com.karumi.rosie.repository.datasource.WriteableDataSource;
import com.karumi.rosie.repository.policy.ReadPolicy;
import java.util.Collection;
import java.util.EnumSet;
import java.util.LinkedList;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNull;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class) public class RosieRepositoryTest {

  private static final AnyRepositoryKey ANY_KEY = new AnyRepositoryKey(42);
  private static final AnyRepositoryValue ANY_VALUE = new AnyRepositoryValue(ANY_KEY);

  @Mock private ReadableDataSource<AnyRepositoryKey, AnyRepositoryValue> readableDataSource;
  @Mock private WriteableDataSource<AnyRepositoryKey, AnyRepositoryValue> writeableDataSource;
  @Mock private CacheDataSource<AnyRepositoryKey, AnyRepositoryValue> cacheDataSource;

  @Test public void shouldReturnNullIfThereAreNoDataSourcesWithData() throws Exception {
    givenCacheDataSourceReturnsNull();
    givenReadableDataSourceReturnsNull();
    RosieRepository<?, ?> repository = givenAReadableAndCacheRepository();

    Collection<?> values = repository.getAll();

    assertNull(values);
  }

  @Test public void shouldReturnDataFromCacheDataSourceIfDataIsValid() throws Exception {
    Collection<AnyRepositoryValue> cacheValues = givenCacheDataSourceReturnsValidValues();
    givenReadableDataSourceReturnsNull();
    RosieRepository<?, AnyRepositoryValue> repository = givenAReadableAndCacheRepository();

    Collection<AnyRepositoryValue> values = repository.getAll();

    assertEquals(cacheValues, values);
  }

  @Test public void shouldReturnDataFromReadableDataSourceIfCacheDataSourceReturnsNullOnGetAll()
      throws Exception {
    givenCacheDataSourceReturnsNull();
    Collection<AnyRepositoryValue> readableValues = givenReadableDataSourceReturnsValidValues();
    RosieRepository<?, AnyRepositoryValue> repository = givenAReadableAndCacheRepository();

    Collection<AnyRepositoryValue> values = repository.getAll();

    Assert.assertEquals(readableValues, values);
  }

  @Test public void shouldReturnDataFromReadableDataSourceIfTheCacheDataSourceReturnsNoValidData()
      throws Exception {
    givenCacheDataSourceReturnsNonValidValues();
    Collection<AnyRepositoryValue> readableValues = givenReadableDataSourceReturnsValidValues();
    RosieRepository<?, AnyRepositoryValue> repository = givenAReadableAndCacheRepository();

    Collection<AnyRepositoryValue> values = repository.getAll();

    Assert.assertEquals(readableValues, values);
  }

  @Test(expected = Exception.class) public void shouldPropagateExceptionsThrownByAnyDataSource()
      throws Exception {
    givenReadableDataSourceThrowsException();
    RosieRepository<?, ?> repository = givenARepository(EnumSet.of(DataSource.READABLE));

    repository.getAll();
  }

  @Test public void shouldGetDataFromReadableDataSourceIfReadPolicyForcesOnlyReadable()
      throws Exception {
    givenCacheDataSourceReturnsValidValues();
    givenReadableDataSourceReturnsValidValues();
    RosieRepository<?, AnyRepositoryValue> repository = givenAReadableAndCacheRepository();

    repository.getAll(ReadPolicy.READABLE_ONLY);

    verify(cacheDataSource, never()).getAll();
    verify(readableDataSource).getAll();
  }

  @Test public void shouldPopulateCacheDataSources() throws Exception {
    givenCacheDataSourceReturnsNull();
    Collection<AnyRepositoryValue> readableValues = givenReadableDataSourceReturnsValidValues();
    RosieRepository<?, AnyRepositoryValue> repository = givenAReadableAndCacheRepository();

    repository.getAll();

    verify(cacheDataSource).addOrUpdateAll(readableValues);
  }

  @Test public void shouldDeleteAllFromCacheDataSourceIfDataIsNotValid() throws Exception {
    givenCacheDataSourceReturnsNonValidValues();
    RosieRepository<?, AnyRepositoryValue> repository = givenAReadableAndCacheRepository();

    repository.getAll();

    verify(cacheDataSource).deleteAll();
  }

  @Test(expected = IllegalArgumentException.class) public void shouldNotAcceptNullKeys()
      throws Exception {
    RosieRepository<?, ?> repository = givenAReadableAndCacheRepository();

    repository.getByKey(null);
  }

  @Test public void shouldReturnValueByKeyFromCacheDataSource() throws Exception {
    givenReadableDataSourceReturnsNull();
    AnyRepositoryValue cacheValue = givenCacheDataSourceReturnsValidValueWithKey(ANY_KEY);
    RosieRepository<AnyRepositoryKey, AnyRepositoryValue> repository =
        givenAReadableAndCacheRepository();

    AnyRepositoryValue value = repository.getByKey(ANY_KEY);

    Assert.assertEquals(cacheValue, value);
  }

  @Test public void shouldReturnValueFromReadableDataSourceIfCacheDataSourceValueIsNull()
      throws Exception {
    givenCacheDataSourceReturnsNull();
    AnyRepositoryValue readableValue = givenReadableDataSourceReturnsValidValueWithKey(ANY_KEY);
    RosieRepository<AnyRepositoryKey, AnyRepositoryValue> repository =
        givenAReadableAndCacheRepository();

    AnyRepositoryValue value = repository.getByKey(ANY_KEY);

    Assert.assertEquals(readableValue, value);
  }

  @Test public void shouldReturnItemFromReadableDataSourceIfCacheDataSourceValueIsNotValid()
      throws Exception {
    givenCacheDataSourceReturnsNonValidValueWithKey(ANY_KEY);
    AnyRepositoryValue readableValue = givenReadableDataSourceReturnsValidValueWithKey(ANY_KEY);
    RosieRepository<AnyRepositoryKey, AnyRepositoryValue> repository =
        givenAReadableAndCacheRepository();

    AnyRepositoryValue value = repository.getByKey(ANY_KEY);

    Assert.assertEquals(readableValue, value);
  }

  @Test public void shouldPopulateCacheDataSourceWithValueIfCacheDataSourceIsNotValid()
      throws Exception {
    givenCacheDataSourceReturnsNonValidValueWithKey(ANY_KEY);
    AnyRepositoryValue readableValue = givenReadableDataSourceReturnsValidValueWithKey(ANY_KEY);
    RosieRepository<AnyRepositoryKey, AnyRepositoryValue> repository =
        givenAReadableAndCacheRepository();

    repository.getByKey(ANY_KEY);

    verify(cacheDataSource).addOrUpdate(readableValue);
  }

  @Test public void shouldDeleteValuesIfAreNotValid() throws Exception {
    givenCacheDataSourceReturnsNonValidValueWithKey(ANY_KEY);
    givenReadableDataSourceReturnsValidValueWithKey(ANY_KEY);
    RosieRepository<AnyRepositoryKey, AnyRepositoryValue> repository =
        givenAReadableAndCacheRepository();

    repository.getByKey(ANY_KEY);

    verify(cacheDataSource).deleteByKey(ANY_KEY);
  }

  @Test public void shouldLoadItemFromReadableDataSourceIfReadPolicyForcesOnlyReadable()
      throws Exception {
    givenCacheDataSourceReturnsValidValueWithKey(ANY_KEY);
    givenReadableDataSourceReturnsValidValueWithKey(ANY_KEY);
    RosieRepository<AnyRepositoryKey, AnyRepositoryValue> repository =
        givenAReadableAndCacheRepository();

    repository.getByKey(ANY_KEY, ReadPolicy.READABLE_ONLY);

    verify(cacheDataSource, never()).getByKey(ANY_KEY);
    verify(readableDataSource).getByKey(ANY_KEY);
  }

  @Test(expected = IllegalArgumentException.class) public void shouldNotAddOrUpdateNullItems()
      throws Exception {
    RosieRepository<?, AnyRepositoryValue> repository = givenAWriteableAndCacheRepository();

    repository.addOrUpdate(null);
  }

  @Test public void shouldAddOrUpdateItemToWriteableDataSource() throws Exception {
    givenWriteableDataSourceWritesValue(ANY_VALUE);
    RosieRepository<?, AnyRepositoryValue> repository = givenAWriteableAndCacheRepository();

    repository.addOrUpdate(ANY_VALUE);

    verify(writeableDataSource).addOrUpdate(ANY_VALUE);
  }

  @Test public void shouldPopulateCacheDataSourceWithWriteableDataSourceResult() throws Exception {
    AnyRepositoryValue writeableValue = givenWriteableDataSourceWritesValue(ANY_VALUE);
    RosieRepository<?, AnyRepositoryValue> repository = givenAWriteableAndCacheRepository();

    repository.addOrUpdate(ANY_VALUE);

    verify(cacheDataSource).addOrUpdate(writeableValue);
  }

  @Test public void shouldNotPopulateCacheDataSourceIfResultIsNotSuccessful() throws Exception {
    givenWriteableDataSourceDoesNotWriteValue(ANY_VALUE);
    RosieRepository<?, AnyRepositoryValue> repository = givenAWriteableAndCacheRepository();

    repository.addOrUpdate(ANY_VALUE);

    verify(cacheDataSource, never()).addOrUpdate(any(AnyRepositoryValue.class));
  }

  @Test(expected = IllegalArgumentException.class) public void shouldNotAcceptNullValues()
      throws Exception {
    RosieRepository<?, ?> repository = givenAWriteableAndCacheRepository();

    repository.addOrUpdate(null);
  }

  @Test public void shouldAddItemsToWriteableDataSource() throws Exception {
    Collection<AnyRepositoryValue> values = getSomeValues();
    givenWriteableDataSourceWritesValues(values);
    RosieRepository<?, AnyRepositoryValue> repository = givenAWriteableAndCacheRepository();

    repository.addOrUpdateAll(values);

    verify(writeableDataSource).addOrUpdateAll(values);
  }

  @Test public void shouldPopulateCacheDataSourceWithWriteableDataSourceResults() throws Exception {
    Collection<AnyRepositoryValue> values = getSomeValues();
    Collection<AnyRepositoryValue> writeableValues = givenWriteableDataSourceWritesValues(values);
    RosieRepository<?, AnyRepositoryValue> repository = givenAWriteableAndCacheRepository();

    repository.addOrUpdateAll(values);

    verify(cacheDataSource).addOrUpdateAll(writeableValues);
  }

  @Test public void shouldNotPopulateCacheDataSourceIfWriteableDataSourceResultIsNotSuccessful()
      throws Exception {
    Collection<AnyRepositoryValue> values = getSomeValues();
    givenWriteableDataSourceDoesNotWriteValues(values);
    RosieRepository<?, AnyRepositoryValue> repository = givenAWriteableAndCacheRepository();

    repository.addOrUpdateAll(values);

    verify(cacheDataSource, never()).addOrUpdateAll(values);
  }

  @Test public void shouldDeleteAllDataSources() throws Exception {
    RosieRepository<?, ?> repository = givenAWriteableAndCacheRepository();

    repository.deleteAll();

    verify(writeableDataSource).deleteAll();
    verify(cacheDataSource).deleteAll();
  }

  @Test public void shouldDeleteAllDataSourcesByKey() throws Exception {
    RosieRepository<AnyRepositoryKey, ?> repository = givenAWriteableAndCacheRepository();

    repository.deleteByKey(ANY_KEY);

    verify(writeableDataSource).deleteByKey(ANY_KEY);
    verify(cacheDataSource).deleteByKey(ANY_KEY);
  }

  private RosieRepository<AnyRepositoryKey, AnyRepositoryValue> givenAReadableAndCacheRepository() {
    return givenARepository(EnumSet.of(DataSource.READABLE, DataSource.CACHE));
  }

  private RosieRepository<AnyRepositoryKey, AnyRepositoryValue> givenAWriteableAndCacheRepository() {
    return givenARepository(EnumSet.of(DataSource.WRITEABLE, DataSource.CACHE));
  }

  private RosieRepository<AnyRepositoryKey, AnyRepositoryValue> givenARepository(
      EnumSet<DataSource> dataSources) {
    RosieRepository<AnyRepositoryKey, AnyRepositoryValue> repository = new RosieRepository<>();

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
      boolean isValidValue) throws Exception {
    AnyRepositoryValue value = new AnyRepositoryValue(key);
    when(cacheDataSource.getByKey(key)).thenReturn(value);
    when(cacheDataSource.isValid(value)).thenReturn(isValidValue);
    return value;
  }

  @NonNull
  private AnyRepositoryValue givenReadableDataSourceReturnsValidValueWithKey(AnyRepositoryKey key)
      throws Exception {
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

  private Collection<AnyRepositoryValue> givenCacheDataSourceReturnsValues(boolean areValidValues)
      throws Exception {
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

  private void givenReadableDataSourceThrowsException() throws Exception {
    when(readableDataSource.getAll()).thenThrow(new Exception());
  }

  private AnyRepositoryValue givenWriteableDataSourceWritesValue(AnyRepositoryValue value)
      throws Exception {
    AnyRepositoryValue writeableValue = new AnyRepositoryValue(value.getKey());
    when(writeableDataSource.addOrUpdate(value)).thenReturn(writeableValue);
    return writeableValue;
  }

  private void givenWriteableDataSourceDoesNotWriteValue(AnyRepositoryValue value)
      throws Exception {
    when(writeableDataSource.addOrUpdate(value)).thenReturn(null);
  }

  private void givenWriteableDataSourceDoesNotWriteValues(Collection<AnyRepositoryValue> values)
      throws Exception {
    when(writeableDataSource.addOrUpdateAll(values)).thenReturn(null);
  }

  private Collection<AnyRepositoryValue> givenWriteableDataSourceWritesValues(
      Collection<AnyRepositoryValue> values) throws Exception {
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
