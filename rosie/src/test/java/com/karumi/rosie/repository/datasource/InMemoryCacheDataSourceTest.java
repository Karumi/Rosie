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

import com.karumi.rosie.doubles.AnyRepositoryKey;
import com.karumi.rosie.doubles.AnyRepositoryValue;
import com.karumi.rosie.time.TimeProvider;
import java.util.LinkedList;
import java.util.List;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class) public class InMemoryCacheDataSourceTest {

  private static final long ANY_TTL = 10;
  private static final AnyRepositoryKey ANY_KEY = new AnyRepositoryKey(2);
  private static final AnyRepositoryValue ANY_VALUE = new AnyRepositoryValue(ANY_KEY);
  private static final int ANY_ITEMS_COUNT = 5;

  @Mock private TimeProvider timeProvider;

  @Test public void shouldAddItem() throws Exception {
    CacheDataSource<AnyRepositoryKey, AnyRepositoryValue> cache = givenAnInMemoryCacheDataSource();

    cache.addOrUpdate(ANY_VALUE);

    assertEquals(ANY_VALUE, cache.getByKey(ANY_KEY));
  }

  @Test public void shouldAddItems() throws Exception {
    CacheDataSource<AnyRepositoryKey, AnyRepositoryValue> cache = givenAnInMemoryCacheDataSource();
    List<AnyRepositoryValue> values = givenSomeValues(ANY_ITEMS_COUNT);

    cache.addOrUpdateAll(values);

    assertEquals(values, cache.getAll());
  }

  @Test public void shouldBeAbleToRetrieveItemsById() throws Exception {
    CacheDataSource<AnyRepositoryKey, AnyRepositoryValue> cache = givenAnInMemoryCacheDataSource();
    List<AnyRepositoryValue> values = givenSomeValues(ANY_ITEMS_COUNT);

    cache.addOrUpdateAll(values);

    assertEquals(values.get(2), cache.getByKey(new AnyRepositoryKey(2)));
  }

  @Test public void shouldDeleteItemById() throws Exception {
    CacheDataSource<AnyRepositoryKey, AnyRepositoryValue> cache = givenAnInMemoryCacheDataSource();
    List<AnyRepositoryValue> values = givenSomeValues(ANY_ITEMS_COUNT);

    cache.addOrUpdateAll(values);
    cache.deleteByKey(ANY_KEY);

    assertFalse(cache.getAll().contains(new AnyRepositoryValue(ANY_KEY)));
  }

  @Test public void shouldDeleteAllItems() throws Exception {
    CacheDataSource<AnyRepositoryKey, AnyRepositoryValue> cache = givenAnInMemoryCacheDataSource();
    List<AnyRepositoryValue> values = givenSomeValues(ANY_ITEMS_COUNT);

    cache.addOrUpdateAll(values);
    cache.deleteAll();

    assertTrue(cache.getAll().isEmpty());
  }

  @Test public void shouldReturnInvalidValuesIfTheTTLNoHasExpired() throws Exception {
    CacheDataSource<AnyRepositoryKey, AnyRepositoryValue> cache = givenAnInMemoryCacheDataSource();
    List<AnyRepositoryValue> values = givenSomeValues(ANY_ITEMS_COUNT);

    cache.addOrUpdateAll(values);
    advanceTime(ANY_TTL - 1);

    AnyRepositoryValue value = cache.getByKey(ANY_KEY);
    assertTrue(cache.isValid(value));
  }

  @Test public void shouldReturnInvalidValuesIfTheTTLHasExpired() throws Exception {
    CacheDataSource<AnyRepositoryKey, AnyRepositoryValue> cache = givenAnInMemoryCacheDataSource();
    List<AnyRepositoryValue> values = givenSomeValues(ANY_ITEMS_COUNT);

    cache.addOrUpdateAll(values);
    advanceTime(ANY_TTL + 1);

    AnyRepositoryValue value = cache.getByKey(ANY_KEY);
    assertFalse(cache.isValid(value));
  }

  private CacheDataSource<AnyRepositoryKey, AnyRepositoryValue> givenAnInMemoryCacheDataSource() {
    return new InMemoryCacheDataSource<>(timeProvider, ANY_TTL);
  }

  private List<AnyRepositoryValue> givenSomeValues(int itemsCount) {
    List<AnyRepositoryValue> values = new LinkedList<>();
    for (int i = 0; i < itemsCount; i++) {
      AnyRepositoryValue value = new AnyRepositoryValue(new AnyRepositoryKey(i));
      values.add(value);
    }
    return values;
  }

  private void advanceTime(long time) {
    when(timeProvider.currentTimeMillis()).thenReturn(time);
  }
}
