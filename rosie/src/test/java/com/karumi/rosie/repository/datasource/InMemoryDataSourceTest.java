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
import com.karumi.rosie.doubles.AnyCacheableItem;
import com.karumi.rosie.time.TimeProvider;
import java.util.LinkedList;
import java.util.List;
import org.junit.Test;
import org.mockito.Mock;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

public class InMemoryDataSourceTest extends UnitTest {

  private static final long ANY_TTL = 10;
  private static final String ANY_ID = "1";
  private static final int ANY_ITEMS_COUNT = 5;

  @Mock private TimeProvider timeProvider;

  @Test public void shouldAddItem() throws Exception {
    DataSource<AnyCacheableItem> dataSource = givenAnInMemoryDataSource();

    AnyCacheableItem item = new AnyCacheableItem(ANY_ID);
    dataSource.addOrUpdate(item);

    assertEquals(item, dataSource.getById(ANY_ID));
  }

  @Test public void shouldAddItems() throws Exception {
    DataSource<AnyCacheableItem> dataSource = givenAnInMemoryDataSource();
    List<AnyCacheableItem> items = givenSomeItems(ANY_ITEMS_COUNT);

    dataSource.addOrUpdate(items);

    assertEquals(items, dataSource.getAll());
  }

  @Test public void shouldBeAbleToRetrieveItemsById() throws Exception {
    DataSource<AnyCacheableItem> dataSource = givenAnInMemoryDataSource();
    List<AnyCacheableItem> items = givenSomeItems(ANY_ITEMS_COUNT);

    dataSource.addOrUpdate(items);

    assertEquals(items.get(2), dataSource.getById("2"));
  }

  @Test public void shouldDeleteItemById() throws Exception {
    DataSource<AnyCacheableItem> dataSource = givenAnInMemoryDataSource();
    List<AnyCacheableItem> items = givenSomeItems(ANY_ITEMS_COUNT);

    dataSource.addOrUpdate(items);
    String anyItemId = "2";
    dataSource.deleteById(anyItemId);

    assertFalse(dataSource.getAll().contains(new AnyCacheableItem(anyItemId)));
  }

  @Test public void shouldDeleteAllItems() throws Exception {
    DataSource<AnyCacheableItem> dataSource = givenAnInMemoryDataSource();
    List<AnyCacheableItem> items = givenSomeItems(ANY_ITEMS_COUNT);

    dataSource.addOrUpdate(items);
    dataSource.deleteAll();

    assertTrue(dataSource.getAll().isEmpty());
  }

  @Test public void shouldReturnInvalidItemsIfTheTTLNoHasExpired() throws Exception {
    DataSource<AnyCacheableItem> dataSource = givenAnInMemoryDataSource();
    List<AnyCacheableItem> items = givenSomeItems(ANY_ITEMS_COUNT);

    dataSource.addOrUpdate(items);
    advanceTime(ANY_TTL - 1);

    AnyCacheableItem item = dataSource.getById(ANY_ID);
    assertTrue(dataSource.isValid(item));
  }

  @Test public void shouldReturnInvalidItemsIfTheTTLHasExpired() throws Exception {
    DataSource<AnyCacheableItem> dataSource = givenAnInMemoryDataSource();
    List<AnyCacheableItem> items = givenSomeItems(ANY_ITEMS_COUNT);

    dataSource.addOrUpdate(items);
    advanceTime(ANY_TTL + 1);

    AnyCacheableItem item = dataSource.getById(ANY_ID);
    assertFalse(dataSource.isValid(item));
  }

  private void advanceTime(long time) {
    when(timeProvider.currentTimeMillis()).thenReturn(time);
  }

  private List<AnyCacheableItem> givenSomeItems(int itemsCount) {
    List<AnyCacheableItem> items = new LinkedList<>();
    for (int i = 0; i < itemsCount; i++) {
      AnyCacheableItem item = new AnyCacheableItem(String.valueOf(i));
      items.add(item);
    }
    return items;
  }

  private DataSource<AnyCacheableItem> givenAnInMemoryDataSource() {
    return new InMemoryDataSource<>(timeProvider, ANY_TTL);
  }
}