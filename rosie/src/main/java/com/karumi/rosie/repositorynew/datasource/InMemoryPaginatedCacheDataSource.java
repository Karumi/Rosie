/*
 * Copyright (C) 2015 Karumi.
 */

package com.karumi.rosie.repositorynew.datasource;

import com.karumi.rosie.repository.PaginatedCollection;
import com.karumi.rosie.time.TimeProvider;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

public class InMemoryPaginatedCacheDataSource<K, V extends Identifiable<K>>
    implements PaginatedCacheDataSource<K, V> {

  private final TimeProvider timeProvider;
  private final long ttlInMillis;
  private final List<V> items;

  private long lastItemsUpdate;
  private boolean hasMore;

  public InMemoryPaginatedCacheDataSource(TimeProvider timeProvider, long ttlInMillis) {
    this.timeProvider = timeProvider;
    this.ttlInMillis = ttlInMillis;
    this.items = new ArrayList<>();
  }

  @Override public PaginatedCollection<V> getPage(int offset, int limit) {
    List<V> result = new LinkedList<>();
    for (int i = offset; i < items.size() && i < offset + limit; i++) {
      V value = items.get(i);
      result.add(value);
    }
    PaginatedCollection<V> paginatedCollection = new PaginatedCollection<>(result);
    paginatedCollection.setOffset(offset);
    paginatedCollection.setLimit(limit);
    paginatedCollection.setHasMore(offset + limit < items.size() || this.hasMore);
    return paginatedCollection;
  }

  @Override
  public PaginatedCollection<V> addOrUpdatePage(int offset, int limit, Collection<V> items,
      boolean hasMore) {
    this.items.addAll(items);
    this.hasMore = hasMore;
    PaginatedCollection<V> paginatedCollection = new PaginatedCollection<>(items);
    paginatedCollection.setOffset(offset);
    paginatedCollection.setLimit(limit);
    paginatedCollection.setHasMore(hasMore);
    lastItemsUpdate = timeProvider.currentTimeMillis();
    return paginatedCollection;
  }

  @Override public void deleteAll() {
    items.clear();
    hasMore = false;
    lastItemsUpdate = 0;
  }

  @Override public boolean isValid(V value) {
    return timeProvider.currentTimeMillis() - lastItemsUpdate < ttlInMillis;
  }
}
