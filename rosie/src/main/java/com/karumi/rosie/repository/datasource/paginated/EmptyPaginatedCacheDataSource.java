/*
 * Copyright (C) 2015 Karumi.
 */

package com.karumi.rosie.repository.datasource.paginated;

import com.karumi.rosie.repository.PaginatedCollection;
import com.karumi.rosie.repository.datasource.Identifiable;
import java.util.Collection;

public class EmptyPaginatedCacheDataSource<K, V extends Identifiable<K>>
    implements PaginatedCacheDataSource<K, V> {

  @Override public boolean isValid(V value) {
    return false;
  }

  @Override public PaginatedCollection<V> getPage(int offset, int limit) {
    return null;
  }

  @Override
  public PaginatedCollection<V> addOrUpdatePage(int offset, int limit, Collection<V> items,
      boolean hasMore) {
    return null;
  }

  @Override public void deleteAll() {

  }
}
