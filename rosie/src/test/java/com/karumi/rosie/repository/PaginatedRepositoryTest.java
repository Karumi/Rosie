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

package com.karumi.rosie.repository;

import com.karumi.rosie.UnitTest;
import com.karumi.rosie.doubles.AnyCacheableItem;
import java.util.LinkedList;
import org.junit.Test;
import org.mockito.Mock;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class PaginatedRepositoryTest extends UnitTest {

  private static final int ANY_OFFSET = 0;
  private static final int ANY_LIMIT = 20;

  @Mock private PaginatedDataSource<AnyCacheableItem> cacheDataSource;
  @Mock private PaginatedDataSource<AnyCacheableItem> apiDataSource;

  @Test public void shouldReturnItemsFromTheCacheDataSourceIfDataIsValid() throws Exception {
    PaginatedCollection<AnyCacheableItem> cacheItems = givenCacheReturnsValidData();
    PaginatedRepository<AnyCacheableItem> repository = givenARepository();

    PaginatedCollection<AnyCacheableItem> items = repository.get(ANY_OFFSET, ANY_LIMIT);

    assertEquals(cacheItems, items);
  }

  @Test public void shouldReturnItemsFromTheAPIDataSourceIfTheCacheHasNoData() throws Exception {
    PaginatedCollection<AnyCacheableItem> apiItems = givenCacheIsEmptyAndApiReturnData();
    PaginatedRepository<AnyCacheableItem> repository = givenARepository();

    PaginatedCollection<AnyCacheableItem> items = repository.get(ANY_OFFSET, ANY_LIMIT);

    assertEquals(apiItems, items);
  }

  @Test public void shouldReturnItemsFromTheAPIIfTheCacheDataIsNotValid() throws Exception {
    PaginatedCollection<AnyCacheableItem> apiItems = givenCacheDataIsInvalid();
    PaginatedRepository<AnyCacheableItem> repository = givenARepository();

    PaginatedCollection<AnyCacheableItem> items = repository.get(ANY_OFFSET, ANY_LIMIT);

    assertEquals(apiItems, items);
  }

  @Test public void shouldReturnItemsFromTheAPIIfTheForceLoadFlagIsEnabled() throws Exception {
    PaginatedCollection<AnyCacheableItem> apiItems = givenCacheReturnsValidDataAndAPIToo();
    PaginatedRepository<AnyCacheableItem> repository = givenARepository();

    PaginatedCollection<AnyCacheableItem> items = repository.get(ANY_OFFSET, ANY_LIMIT, true);

    assertEquals(apiItems, items);
  }

  @Test public void shouldPopulateCacheDataSourceAfterGetDataFromTheAPI() throws Exception {
    PaginatedCollection<AnyCacheableItem> apiItems = givenCacheReturnsValidDataAndAPIToo();
    PaginatedRepository<AnyCacheableItem> repository = givenARepository();

    repository.get(ANY_OFFSET, ANY_LIMIT, true);

    verify(cacheDataSource).addOrUpdate(ANY_OFFSET, ANY_LIMIT, apiItems.getItems());
  }

  @Test public void shouldDeleteCacheDataIfItemsAreNotValid() throws Exception {
    givenCacheDataIsInvalid();
    PaginatedRepository<AnyCacheableItem> repository = givenARepository();

    repository.get(ANY_OFFSET, ANY_LIMIT);

    verify(cacheDataSource).deleteAll();
  }

  private PaginatedCollection<AnyCacheableItem> givenCacheReturnsValidDataAndAPIToo()
      throws Exception {
    LinkedList<AnyCacheableItem> items = getSomeItems();
    PaginatedCollection<AnyCacheableItem> cacheItems = new PaginatedCollection<>(items);
    when(cacheDataSource.get(anyInt(), anyInt())).thenReturn(cacheItems);
    when(cacheDataSource.isValid(any(AnyCacheableItem.class))).thenReturn(true);
    PaginatedCollection<AnyCacheableItem> apiItems = new PaginatedCollection<>(items);
    when(apiDataSource.get(anyInt(), anyInt())).thenReturn(apiItems);
    when(apiDataSource.isValid(any(AnyCacheableItem.class))).thenReturn(true);
    return apiItems;
  }

  private PaginatedCollection<AnyCacheableItem> givenCacheDataIsInvalid() throws Exception {
    LinkedList<AnyCacheableItem> items = getSomeItems();
    when(cacheDataSource.get(anyInt(), anyInt())).thenReturn(
        new PaginatedCollection<AnyCacheableItem>(items));
    when(cacheDataSource.isValid(any(AnyCacheableItem.class))).thenReturn(false);
    PaginatedCollection<AnyCacheableItem> apiItems = new PaginatedCollection<>(items);
    when(apiDataSource.get(anyInt(), anyInt())).thenReturn(apiItems);
    return apiItems;
  }

  private PaginatedCollection<AnyCacheableItem> givenCacheIsEmptyAndApiReturnData()
      throws Exception {
    LinkedList<AnyCacheableItem> items = getSomeItems();
    when(cacheDataSource.get(anyInt(), anyInt())).thenReturn(
        new PaginatedCollection<AnyCacheableItem>());
    PaginatedCollection<AnyCacheableItem> apiItems = new PaginatedCollection<>(items);
    when(apiDataSource.get(anyInt(), anyInt())).thenReturn(apiItems);
    return apiItems;
  }

  private PaginatedCollection<AnyCacheableItem> givenCacheReturnsValidData() throws Exception {
    LinkedList<AnyCacheableItem> items = getSomeItems();
    PaginatedCollection<AnyCacheableItem> cacheItems = new PaginatedCollection<>(items);
    when(cacheDataSource.get(anyInt(), anyInt())).thenReturn(cacheItems);
    when(cacheDataSource.isValid(any(AnyCacheableItem.class))).thenReturn(true);
    when(apiDataSource.get(anyInt(), anyInt())).thenReturn(
        new PaginatedCollection<AnyCacheableItem>());
    return cacheItems;
  }

  private PaginatedRepository<AnyCacheableItem> givenARepository() {
    return new PaginatedRepository<AnyCacheableItem>(cacheDataSource, apiDataSource);
  }

  private LinkedList<AnyCacheableItem> getSomeItems() {
    LinkedList<AnyCacheableItem> items = new LinkedList<>();
    for (int i = 0; i < 5; i++) {
      items.add(new AnyCacheableItem(String.valueOf(i)));
    }
    return items;
  }
}
