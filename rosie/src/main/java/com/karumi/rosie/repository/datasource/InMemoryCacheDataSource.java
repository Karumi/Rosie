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
