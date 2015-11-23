/*
 * Copyright (C) 2015 Karumi.
 */

package com.karumi.rosie.repositorynew;

import java.util.Collection;

public class EmptyWriteable<K, V extends Keyable<K>> implements Writeable<K, V> {

  @Override public V addOrUpdate(V value) {
    return value;
  }

  @Override public Collection<V> addOrUpdateAll(Collection<V> values) {
    return values;
  }

  @Override public void delete(K key) {

  }

  @Override public void deleteAll() {

  }
}
