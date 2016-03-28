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

package com.karumi.rosie.repository.datasource;

import com.karumi.rosie.doubles.AnyRepositoryKey;
import com.karumi.rosie.doubles.AnyRepositoryValue;
import com.karumi.rosie.repository.PaginatedCollection;
import com.karumi.rosie.repository.datasource.paginated.InMemoryPaginatedCacheDataSource;
import com.karumi.rosie.repository.datasource.paginated.Page;
import com.karumi.rosie.repository.datasource.paginated.PaginatedCacheDataSource;
import com.karumi.rosie.time.TimeProvider;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static junit.framework.Assert.assertEquals;

@RunWith(MockitoJUnitRunner.class) public class InMemoryPaginatedCacheDataSourceTest {

  private static final long ANY_TTL = 10;
  private static final int ANY_OFFSET = 0;
  private static final int ANY_LIMIT = 20;
  private static final boolean ANY_HAS_MORE = true;

  @Mock private TimeProvider timeProvider;

  @Test public void shouldAddValuesBasedOnTheOffsetAndLimit() throws Exception {
    Page page = Page.withOffsetAndLimit(ANY_OFFSET, ANY_LIMIT);
    Collection<AnyRepositoryValue> values = givenSomeItems(ANY_OFFSET + ANY_LIMIT);
    PaginatedCacheDataSource<AnyRepositoryKey, AnyRepositoryValue> cache =
        givenAnInMemoryPaginatedCacheDataSource();

    cache.addOrUpdatePage(page, values, ANY_HAS_MORE);

    PaginatedCollection<AnyRepositoryValue> paginatedCollection = cache.getPage(page);
    assertEquals(values, paginatedCollection.getItems());
  }

  @Test public void shouldReturnTheRequestedOffset() throws Exception {
    Page page = Page.withOffsetAndLimit(ANY_OFFSET, ANY_LIMIT);
    Collection<AnyRepositoryValue> values = givenSomeItems(ANY_OFFSET + ANY_LIMIT);
    PaginatedCacheDataSource<AnyRepositoryKey, AnyRepositoryValue> cache =
        givenAnInMemoryPaginatedCacheDataSource();

    cache.addOrUpdatePage(page, values, ANY_HAS_MORE);
    PaginatedCollection<AnyRepositoryValue> paginatedCollection = cache.getPage(page);

    assertEquals(ANY_OFFSET, paginatedCollection.getPage().getOffset());
  }

  @Test public void shouldReturnRequestedLimit() throws Exception {
    Page page = Page.withOffsetAndLimit(ANY_OFFSET, ANY_LIMIT);
    Collection<AnyRepositoryValue> values = givenSomeItems(ANY_OFFSET + ANY_LIMIT);
    PaginatedCacheDataSource<AnyRepositoryKey, AnyRepositoryValue> cache =
        givenAnInMemoryPaginatedCacheDataSource();

    cache.addOrUpdatePage(page, values, ANY_HAS_MORE);
    PaginatedCollection<AnyRepositoryValue> paginatedCollection = cache.getPage(page);

    assertEquals(ANY_LIMIT, paginatedCollection.getPage().getLimit());
  }

  @Test public void shouldReturnHasMoreIfThereAreMoreItemsToLoad() throws Exception {
    Page page = Page.withOffsetAndLimit(ANY_OFFSET, ANY_LIMIT);
    Collection<AnyRepositoryValue> values = givenSomeItems(ANY_OFFSET + ANY_LIMIT);
    PaginatedCacheDataSource<AnyRepositoryKey, AnyRepositoryValue> cache =
        givenAnInMemoryPaginatedCacheDataSource();

    cache.addOrUpdatePage(page, values, ANY_HAS_MORE);
    PaginatedCollection<AnyRepositoryValue> paginatedCollection = cache.getPage(page);

    assertEquals(ANY_HAS_MORE, paginatedCollection.hasMore());
  }

  private InMemoryPaginatedCacheDataSource<AnyRepositoryKey, AnyRepositoryValue>
  givenAnInMemoryPaginatedCacheDataSource() {
    return new InMemoryPaginatedCacheDataSource<>(timeProvider, ANY_TTL);
  }

  private Collection<AnyRepositoryValue> givenSomeItems(int numberOfItems) {
    List<AnyRepositoryValue> values = new LinkedList<>();
    for (int i = 0; i < numberOfItems; i++) {
      AnyRepositoryValue value = new AnyRepositoryValue(new AnyRepositoryKey(i));
      values.add(value);
    }
    return values;
  }
}
