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

import com.karumi.rosie.doubles.AnyRepositoryKey;
import com.karumi.rosie.doubles.AnyRepositoryValue;
import com.karumi.rosie.repository.datasource.paginated.Page;
import com.karumi.rosie.repository.datasource.paginated.PaginatedCacheDataSource;
import com.karumi.rosie.repository.datasource.paginated.PaginatedReadableDataSource;
import com.karumi.rosie.repository.policy.ReadPolicy;
import java.util.LinkedList;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class) public class PaginatedRosieRepositoryTest {

  private static final int ANY_OFFSET = 0;
  private static final int ANY_LIMIT = 20;

  @Mock private PaginatedCacheDataSource<AnyRepositoryKey, AnyRepositoryValue> cacheDataSource;
  @Mock private PaginatedReadableDataSource<AnyRepositoryKey, AnyRepositoryValue>
      readableDataSource;

  @Test public void shouldReturnValuesFromCacheDataSourceIfDataIsValid() throws Exception {
    Page page = Page.withOffsetAndLimit(ANY_OFFSET, ANY_LIMIT);
    PaginatedCollection<AnyRepositoryValue> cacheValues =
        givenCacheDataSourceReturnsValidValues(page);
    PaginatedRosieRepository<AnyRepositoryKey, AnyRepositoryValue> repository =
        givenAPaginatedRepository();

    PaginatedCollection<AnyRepositoryValue> values = repository.getPage(page);

    assertEquals(cacheValues, values);
  }

  @Test public void shouldReturnItemsFromReadableDataSourceIfCacheDataSourceHasNoData()
      throws Exception {
    Page page = Page.withOffsetAndLimit(ANY_OFFSET, ANY_LIMIT);
    givenCacheDataSourceReturnsNull(page);
    PaginatedCollection<AnyRepositoryValue> readableValues =
        givenReadableDataSourceReturnsValues(page);
    PaginatedRosieRepository<AnyRepositoryKey, AnyRepositoryValue> repository =
        givenAPaginatedRepository();

    PaginatedCollection<AnyRepositoryValue> values = repository.getPage(page);

    assertEquals(readableValues, values);
  }

  @Test public void shouldReturnValuesFromReadableDataSourceIfCacheDataSourceIsNotValid()
      throws Exception {
    Page page = Page.withOffsetAndLimit(ANY_OFFSET, ANY_LIMIT);
    givenCacheDataSourceReturnsNonValidValues(page);
    PaginatedCollection<AnyRepositoryValue> readableValues =
        givenReadableDataSourceReturnsValues(page);
    PaginatedRosieRepository<AnyRepositoryKey, AnyRepositoryValue> repository =
        givenAPaginatedRepository();

    PaginatedCollection<AnyRepositoryValue> values = repository.getPage(page);

    assertEquals(readableValues, values);
  }

  @Test public void shouldReturnValuesFromReadableDataSourceIfCacheDoNotHaveThisPage()
      throws Exception {
    Page page = Page.withOffsetAndLimit(ANY_OFFSET, ANY_LIMIT);
    givenCacheDataSourceReturnsNonValidValues(page);
    givenReadableDataSourceReturnsValues(page);
    PaginatedRosieRepository<AnyRepositoryKey, AnyRepositoryValue> repository =
        givenAPaginatedRepository();
    repository.getPage(page);

    Page nextPage = Page.withOffsetAndLimit(ANY_OFFSET + ANY_LIMIT, ANY_LIMIT);
    repository.getPage(nextPage);

    verify(cacheDataSource).getPage(page);
    verify(readableDataSource).getPage(page);
    verify(cacheDataSource).getPage(nextPage);
    verify(readableDataSource).getPage(nextPage);
  }

  @Test public void shouldNotRemoveDataFromCacheIfCacheDoNotHaveThisPage() throws Exception {
    Page page = Page.withOffsetAndLimit(ANY_OFFSET, ANY_LIMIT);
    givenCacheDataSourceReturnsNonValidValues(page);
    givenReadableDataSourceReturnsValues(page);
    PaginatedRosieRepository<AnyRepositoryKey, AnyRepositoryValue> repository =
        givenAPaginatedRepository();
    repository.getPage(page);

    Page nextPage = Page.withOffsetAndLimit(ANY_OFFSET + ANY_LIMIT, ANY_LIMIT);
    repository.getPage(nextPage);

    verify(cacheDataSource, times(1)).deleteAll();
  }

  @Test public void shouldReturnValuesFromReadableDataSourceIfPolicyForcesOnlyReadable()
      throws Exception {
    Page page = Page.withOffsetAndLimit(ANY_OFFSET, ANY_LIMIT);
    givenCacheDataSourceReturnsValidValues(page);
    PaginatedCollection<AnyRepositoryValue> readableValues =
        givenReadableDataSourceReturnsValues(page);
    PaginatedRosieRepository<AnyRepositoryKey, AnyRepositoryValue> repository =
        givenAPaginatedRepository();

    PaginatedCollection<AnyRepositoryValue> values =
        repository.getPage(page, ReadPolicy.READABLE_ONLY);

    assertEquals(readableValues, values);
  }

  @Test public void shouldPopulateCacheDataSourceAfterGetPageFromReadableDataSource()
      throws Exception {
    Page page = Page.withOffsetAndLimit(ANY_OFFSET, ANY_LIMIT);
    givenCacheDataSourceReturnsNull(page);
    PaginatedCollection<AnyRepositoryValue> readableValues =
        givenReadableDataSourceReturnsValues(page);
    PaginatedRosieRepository<AnyRepositoryKey, AnyRepositoryValue> repository =
        givenAPaginatedRepository();

    repository.getPage(page);

    verify(cacheDataSource).addOrUpdatePage(page, readableValues.getItems(), true);
  }

  @Test public void shouldDeleteCacheDataIfItemsAreNotValid() throws Exception {
    Page page = Page.withOffsetAndLimit(ANY_OFFSET, ANY_LIMIT);
    givenCacheDataSourceReturnsNonValidValues(page);
    PaginatedRosieRepository<AnyRepositoryKey, AnyRepositoryValue> repository =
        givenAPaginatedRepository();

    repository.getPage(page);

    verify(cacheDataSource).deleteAll();
  }

  private PaginatedRosieRepository<AnyRepositoryKey, AnyRepositoryValue> givenAPaginatedRepository() {
    PaginatedRosieRepository<AnyRepositoryKey, AnyRepositoryValue> repository =
        new PaginatedRosieRepository<>();
    repository.addPaginatedCacheDataSources(cacheDataSource);
    repository.addPaginatedReadableDataSources(readableDataSource);
    return repository;
  }

  private void givenCacheDataSourceReturnsNull(Page page) throws Exception {
    when(cacheDataSource.getPage(page)).thenReturn(null);
  }

  private PaginatedCollection<AnyRepositoryValue> givenCacheDataSourceReturnsValidValues(Page page)
      throws Exception {
    return givenCacheDataSourceReturnsValues(page, true);
  }

  private PaginatedCollection<AnyRepositoryValue> givenCacheDataSourceReturnsNonValidValues(
      Page page) throws Exception {
    return givenCacheDataSourceReturnsValues(page, false);
  }

  private PaginatedCollection<AnyRepositoryValue> givenCacheDataSourceReturnsValues(Page page,
      boolean areValidValues) throws Exception {
    PaginatedCollection<AnyRepositoryValue> values = getSomeValues(page);
    when(cacheDataSource.getPage(page)).thenReturn(values);
    when(cacheDataSource.isValid(any(AnyRepositoryValue.class))).thenReturn(areValidValues);
    return values;
  }

  private PaginatedCollection<AnyRepositoryValue> givenReadableDataSourceReturnsValues(Page page)
      throws Exception {
    PaginatedCollection<AnyRepositoryValue> values = getSomeValues(page);
    when(readableDataSource.getPage(page)).thenReturn(values);
    return values;
  }

  private PaginatedCollection<AnyRepositoryValue> getSomeValues(Page page) {
    LinkedList<AnyRepositoryValue> values = new LinkedList<>();
    for (int i = 0; i < 10; i++) {
      values.add(new AnyRepositoryValue(new AnyRepositoryKey(i)));
    }
    PaginatedCollection<AnyRepositoryValue> paginatedValues = new PaginatedCollection<>(values);
    paginatedValues.setHasMore(true);
    paginatedValues.setPage(page);
    return paginatedValues;
  }
}
