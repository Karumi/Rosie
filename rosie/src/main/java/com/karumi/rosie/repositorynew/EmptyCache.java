/*
 * Copyright (C) 2015 Karumi.
 */

package com.karumi.rosie.repositorynew;

import java.util.Collection;

public class EmptyCache<K, V extends Keyable<K>> implements Cache<K, V> {

  @Override public V get(K key) {
    return null;
  }

  @Override public Collection<V> getAll() {
    return null;
  }

  @Override public V addOrUpdate(V value) {
    return null;
  }

  @Override public Collection<V> addOrUpdateAll(Collection<V> values) {
    return null;
  }

  @Override public void delete(K key) {

  }

  @Override public void deleteAll() {

  }
}
