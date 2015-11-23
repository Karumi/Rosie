/*
 * Copyright (C) 2015 Karumi.
 */

package com.karumi.rosie.repositorynew;

import java.util.Collection;

public class EmptyReadable<K, V> implements Readable<K, V> {

  @Override public V get(K key) {
    return null;
  }

  @Override public Collection<V> getAll() {
    return null;
  }
}
