/*
 * Copyright (C) 2015 Karumi.
 */

package com.karumi.rosie.repositorynew;

import com.karumi.rosie.UnitTest;
import com.karumi.rosie.doubles.AnyRepositoryKey;
import com.karumi.rosie.doubles.AnyRepositoryValue;
import com.karumi.rosie.repository.PaginatedCollection;
import com.karumi.rosie.repositorynew.datasource.PaginatedCacheDataSource;
import com.karumi.rosie.repositorynew.datasource.PaginatedReadableDataSource;
import com.karumi.rosie.repositorynew.policy.ReadPolicy;
import java.util.LinkedList;
import org.junit.Test;
import org.mockito.Mock;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class PaginatedRepositoryTest extends UnitTest {

  private static final int ANY_OFFSET = 0;
  private static final int ANY_LIMIT = 20;

  @Mock private PaginatedCacheDataSource<AnyRepositoryKey, AnyRepositoryValue> cacheDataSource;
  @Mock private PaginatedReadableDataSource<AnyRepositoryValue> readableDataSource;

  @Test public void shouldReturnValuesFromCacheDataSourceIfDataIsValid() throws Exception {
    PaginatedCollection<AnyRepositoryValue> cacheValues =
        givenCacheDataSourceReturnsValidValues(ANY_OFFSET, ANY_LIMIT);
    PaginatedRepository<AnyRepositoryKey, AnyRepositoryValue> repository =
        givenAPaginatedRepository();

    PaginatedCollection<AnyRepositoryValue> values = repository.getPage(ANY_OFFSET, ANY_LIMIT);

    assertEquals(cacheValues, values);
  }

  @Test public void shouldReturnItemsFromReadableDataSourceIfCacheDataSourceHasNoData()
      throws Exception {
    givenCacheDataSourceReturnsNull(ANY_OFFSET, ANY_LIMIT);
    PaginatedCollection<AnyRepositoryValue> readableValues =
        givenReadableDataSourceReturnsValues(ANY_OFFSET, ANY_LIMIT);
    PaginatedRepository<AnyRepositoryKey, AnyRepositoryValue> repository =
        givenAPaginatedRepository();

    PaginatedCollection<AnyRepositoryValue> values = repository.getPage(ANY_OFFSET, ANY_LIMIT);

    assertEquals(readableValues, values);
  }

  @Test public void shouldReturnValuesFromReadableDataSourceIfCacheDataSourceIsNotValid()
      throws Exception {
    givenCacheDataSourceReturnsNonValidValues(ANY_OFFSET, ANY_LIMIT);
    PaginatedCollection<AnyRepositoryValue> readableValues =
        givenReadableDataSourceReturnsValues(ANY_OFFSET, ANY_LIMIT);
    PaginatedRepository<AnyRepositoryKey, AnyRepositoryValue> repository =
        givenAPaginatedRepository();

    PaginatedCollection<AnyRepositoryValue> values = repository.getPage(ANY_OFFSET, ANY_LIMIT);

    assertEquals(readableValues, values);
  }

  @Test public void shouldReturnValuesFromReadableDataSourceIfPolicyForcesOnlyReadable()
      throws Exception {
    givenCacheDataSourceReturnsValidValues(ANY_OFFSET, ANY_LIMIT);
    PaginatedCollection<AnyRepositoryValue> readableValues =
        givenReadableDataSourceReturnsValues(ANY_OFFSET, ANY_LIMIT);
    PaginatedRepository<AnyRepositoryKey, AnyRepositoryValue> repository =
        givenAPaginatedRepository();

    PaginatedCollection<AnyRepositoryValue> values =
        repository.getPage(ANY_OFFSET, ANY_LIMIT, ReadPolicy.READABLE_ONLY_AND_POPULATE);

    assertEquals(readableValues, values);
  }

  @Test public void shouldPopulateCacheDataSourceAfterGetPageFromReadableDataSource()
      throws Exception {
    givenCacheDataSourceReturnsNull(ANY_OFFSET, ANY_LIMIT);
    PaginatedCollection<AnyRepositoryValue> readableValues =
        givenReadableDataSourceReturnsValues(ANY_OFFSET, ANY_LIMIT);
    PaginatedRepository<AnyRepositoryKey, AnyRepositoryValue> repository =
        givenAPaginatedRepository();

    repository.getPage(ANY_OFFSET, ANY_LIMIT);

    verify(cacheDataSource).addOrUpdatePage(ANY_OFFSET, ANY_LIMIT, readableValues.getItems(), true);
  }

  @Test public void shouldDeleteCacheDataIfItemsAreNotValid() throws Exception {
    givenCacheDataSourceReturnsNonValidValues(ANY_OFFSET, ANY_LIMIT);
    PaginatedRepository<AnyRepositoryKey, AnyRepositoryValue> repository =
        givenAPaginatedRepository();

    repository.getPage(ANY_OFFSET, ANY_LIMIT);

    verify(cacheDataSource).deleteAll();
  }

  private PaginatedRepository<AnyRepositoryKey, AnyRepositoryValue> givenAPaginatedRepository() {
    PaginatedRepository<AnyRepositoryKey, AnyRepositoryValue> repository =
        new PaginatedRepository<>();
    repository.addPaginatedCaches(cacheDataSource);
    repository.addPaginatedReadables(readableDataSource);
    return repository;
  }

  private void givenCacheDataSourceReturnsNull(int offset, int limit) throws Exception {
    when(cacheDataSource.getPage(offset, limit)).thenReturn(null);
  }

  private PaginatedCollection<AnyRepositoryValue> givenCacheDataSourceReturnsValidValues(int offset,
      int limit) throws Exception {
    return givenCacheDataSourceReturnsValues(offset, limit, true);
  }

  private PaginatedCollection<AnyRepositoryValue> givenCacheDataSourceReturnsNonValidValues(
      int offset, int limit) throws Exception {
    return givenCacheDataSourceReturnsValues(offset, limit, false);
  }

  private PaginatedCollection<AnyRepositoryValue> givenCacheDataSourceReturnsValues(int offset,
      int limit, boolean areValidValues) {
    PaginatedCollection<AnyRepositoryValue> values = getSomeValues(offset, limit);
    when(cacheDataSource.getPage(offset, limit)).thenReturn(values);
    when(cacheDataSource.isValid(any(AnyRepositoryValue.class))).thenReturn(areValidValues);
    return values;
  }

  private PaginatedCollection<AnyRepositoryValue> givenReadableDataSourceReturnsValues(int offset,
      int limit) {
    PaginatedCollection<AnyRepositoryValue> values = getSomeValues(offset, limit);
    when(readableDataSource.getPage(offset, limit)).thenReturn(values);
    return values;
  }

  private PaginatedCollection<AnyRepositoryValue> getSomeValues(int offset, int limit) {
    LinkedList<AnyRepositoryValue> values = new LinkedList<>();
    for (int i = 0; i < 10; i++) {
      values.add(new AnyRepositoryValue(new AnyRepositoryKey(i)));
    }
    PaginatedCollection<AnyRepositoryValue> paginatedValues = new PaginatedCollection<>(values);
    paginatedValues.setHasMore(true);
    paginatedValues.setOffset(offset);
    paginatedValues.setLimit(limit);
    return paginatedValues;
  }
}
