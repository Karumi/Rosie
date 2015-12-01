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

import com.karumi.rosie.time.TimeProvider;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

public class InMemoryCacheDataSource<K, V extends Identifiable<K>>
    implements CacheDataSource<K, V> {

  private final TimeProvider timeProvider;
  private final long ttlInMillis;
  private final List<V> items;

  private long lastItemsUpdate;

  public InMemoryCacheDataSource(TimeProvider timeProvider, long ttlInMillis) {
    this.timeProvider = timeProvider;
    this.ttlInMillis = ttlInMillis;
    this.items = new ArrayList<>();
  }

  @Override public V getByKey(K key) {
    V value = null;
    for (V item : items) {
      if (item.getKey().equals(key)) {
        value = item;
        break;
      }
    }
    return value;
  }

  @Override public Collection<V> getAll() {
    return items;
  }

  @Override public V addOrUpdate(V value) {
    items.add(value);
    lastItemsUpdate = timeProvider.currentTimeMillis();
    return value;
  }

  @Override public Collection<V> addOrUpdateAll(Collection<V> values) {
    for (V value : values) {
      addOrUpdate(value);
    }
    return values;
  }

  @Override public void deleteByKey(K key) {
    Iterator<V> iterator = items.iterator();
    while (iterator.hasNext()) {
      V next = iterator.next();
      if (next.getKey().equals(key)) {
        iterator.remove();
      }
    }
  }

  @Override public void deleteAll() {
    items.clear();
    lastItemsUpdate = 0;
  }

  @Override public boolean isValid(V value) {
    return timeProvider.currentTimeMillis() - lastItemsUpdate < ttlInMillis;
  }
}
