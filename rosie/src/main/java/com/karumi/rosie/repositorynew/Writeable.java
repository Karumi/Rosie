/*
 * Copyright (C) 2015 Karumi.
 */

package com.karumi.rosie.repositorynew;

import java.util.Collection;

public interface Writeable<K, V extends Keyable<K>> {
  V addOrUpdate(V value);

  Collection<V> addOrUpdateAll(Collection<V> values);

  void delete(K key);

  void deleteAll();
}
