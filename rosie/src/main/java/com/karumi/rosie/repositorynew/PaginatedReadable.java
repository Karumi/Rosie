/*
 * Copyright (C) 2015 Karumi.
 */

package com.karumi.rosie.repositorynew;

import com.karumi.rosie.repository.PaginatedCollection;

public interface PaginatedReadable<V> {
  PaginatedCollection<V> get(int offset, int limit);
}
