/*
 * Copyright (C) 2015 Karumi.
 */

package com.karumi.rosie.repositorynew;

import com.karumi.rosie.repository.PaginatedCollection;
import java.util.Collection;

public interface PaginatedWriteable<K, V extends Keyable<K>> {
  PaginatedCollection<V> addOrUpdate(int offset, int limit, Collection<V> items, boolean hasMore);
}
