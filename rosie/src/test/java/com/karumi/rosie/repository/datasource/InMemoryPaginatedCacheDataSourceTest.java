/*
 * The MIT License (MIT) Copyright (c) 2014 karumi Permission is hereby granted, free of charge,
 * to any person obtaining a copy of this software and associated documentation files (the
 * "Software"), to deal in the Software without restriction, including without limitation the
 * rights to use, copy, modify, merge, publish, distribute, sublicense,
 * and/or sell copies of the Software, and to permit persons to whom the Software is furnished to
  * do so, subject to the following conditions: The above copyright notice and this permission
  * notice shall be included in all copies or substantial portions of the Software. THE SOFTWARE
  * IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED,
  * INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
  * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR
  * COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY,
  * WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
  * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package com.karumi.rosie.repository.datasource;

import com.karumi.rosie.UnitTest;
import com.karumi.rosie.doubles.AnyRepositoryKey;
import com.karumi.rosie.doubles.AnyRepositoryValue;
import com.karumi.rosie.repository.PaginatedCollection;
import com.karumi.rosie.time.TimeProvider;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import org.junit.Test;
import org.mockito.Mock;

import static junit.framework.Assert.assertEquals;

public class InMemoryPaginatedCacheDataSourceTest extends UnitTest {

  private static final long ANY_TTL = 10;
  private static final int ANY_OFFSET = 0;
  private static final int ANY_LIMIT = 20;
  private static final boolean ANY_HAS_MORE = true;

  @Mock private TimeProvider timeProvider;

  @Test public void shouldAddValuesBasedOnTheOffsetAndLimit() throws Exception {
    Collection<AnyRepositoryValue> values = givenSomeItems(ANY_OFFSET + ANY_LIMIT);
    PaginatedCacheDataSource<AnyRepositoryKey, AnyRepositoryValue> cache =
        givenAnInMemoryPaginatedCacheDataSource();

    cache.addOrUpdatePage(ANY_OFFSET, ANY_LIMIT, values, ANY_HAS_MORE);

    PaginatedCollection<AnyRepositoryValue> page = cache.getPage(ANY_OFFSET, ANY_LIMIT);
    assertEquals(values, page.getItems());
  }

  @Test public void shouldReturnTheRequestedOffset() throws Exception {
    Collection<AnyRepositoryValue> values = givenSomeItems(ANY_OFFSET + ANY_LIMIT);
    PaginatedCacheDataSource<AnyRepositoryKey, AnyRepositoryValue> cache =
        givenAnInMemoryPaginatedCacheDataSource();

    cache.addOrUpdatePage(ANY_OFFSET, ANY_LIMIT, values, ANY_HAS_MORE);
    PaginatedCollection<AnyRepositoryValue> page = cache.getPage(ANY_OFFSET, ANY_LIMIT);

    assertEquals(ANY_OFFSET, page.getOffset());
  }

  @Test public void shouldReturnRequestedLimit() throws Exception {
    Collection<AnyRepositoryValue> values = givenSomeItems(ANY_OFFSET + ANY_LIMIT);
    PaginatedCacheDataSource<AnyRepositoryKey, AnyRepositoryValue> cache =
        givenAnInMemoryPaginatedCacheDataSource();

    cache.addOrUpdatePage(ANY_OFFSET, ANY_LIMIT, values, ANY_HAS_MORE);
    PaginatedCollection<AnyRepositoryValue> page = cache.getPage(ANY_OFFSET, ANY_LIMIT);

    assertEquals(ANY_LIMIT, page.getLimit());
  }

  @Test public void shouldReturnHasMoreIfThereAreMoreItemsToLoad() throws Exception {
    Collection<AnyRepositoryValue> values = givenSomeItems(ANY_OFFSET + ANY_LIMIT);
    PaginatedCacheDataSource<AnyRepositoryKey, AnyRepositoryValue> cache =
        givenAnInMemoryPaginatedCacheDataSource();

    cache.addOrUpdatePage(ANY_OFFSET, ANY_LIMIT, values, ANY_HAS_MORE);
    PaginatedCollection<AnyRepositoryValue> page = cache.getPage(ANY_OFFSET, ANY_LIMIT);

    assertEquals(ANY_HAS_MORE, page.hasMore());
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
