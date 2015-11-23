/*
 * Copyright (C) 2015 Karumi.
 */

package com.karumi.rosie.repositorynew;

import java.util.Collection;

public interface Readable<K, V> {
  V get(K key);

  Collection<V> getAll();
}
