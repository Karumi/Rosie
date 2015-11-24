/*
 * Copyright (C) 2015 Karumi.
 */

package com.karumi.rosie.repositorynew.datasource;

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
