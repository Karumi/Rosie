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
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * A simple and generic in-memory cache ready to use in your repositories.
 */
public class InMemoryCacheDataSource<K, V extends Identifiable<K>>
    implements CacheDataSource<K, V> {

  protected final TimeProvider timeProvider;
  protected final long ttlInMillis;
  protected final Map<K, V> items;

  protected long lastItemsUpdate;

  public InMemoryCacheDataSource(TimeProvider timeProvider, long ttlInMillis) {
    this.timeProvider = timeProvider;
    this.ttlInMillis = ttlInMillis;
    this.items = new LinkedHashMap<>();
  }

  @Override public synchronized V getByKey(K key) {
    return items.get(key);
  }

  @Override public synchronized Collection<V> getAll() {
    return new ArrayList<>(items.values());
  }

  @Override public synchronized V addOrUpdate(V value) {
    items.put(value.getKey(), value);
    lastItemsUpdate = timeProvider.currentTimeMillis();
    return value;
  }

  @Override public synchronized Collection<V> addOrUpdateAll(Collection<V> values) {
    for (V value : values) {
      addOrUpdate(value);
    }
    return values;
  }

  @Override public synchronized void deleteByKey(K key) {
    items.remove(key);
  }

  @Override public synchronized void deleteAll() {
    items.clear();
    lastItemsUpdate = 0;
  }

  @Override public boolean isValid(V value) {
    return timeProvider.currentTimeMillis() - lastItemsUpdate < ttlInMillis;
  }
}
