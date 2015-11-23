/*
 * Copyright (C) 2015 Karumi.
 */

package com.karumi.rosie.repositorynew;

import com.karumi.rosie.repository.PaginatedCollection;
import java.util.Arrays;
import java.util.Collection;
import java.util.EnumSet;
import java.util.LinkedList;

public class PaginatedRepository<K, V extends Keyable<K>> extends Repository<K, V> {

  private Collection<PaginatedReadable<V>> paginatedReadables = new LinkedList<>();
  private Collection<PaginatedCache<K, V>> paginatedCaches = new LinkedList<>();

  public PaginatedCollection<V> get(int offset, int limit) throws Exception {
    return get(offset, limit, ReadPolicy.READ_ALL_AND_POPULATE);
  }

  public PaginatedCollection<V> get(int offset, int limit, EnumSet<ReadPolicy> policies)
      throws Exception {
    PaginatedCollection<V> values = new PaginatedCollection<>();

    if (policies.contains(ReadPolicy.USE_CACHE)) {
      values = getPaginatedValuesFromCaches(offset, limit);
    }

    if (values == null && policies.contains(ReadPolicy.USE_READABLE)) {
      values = getPaginatedValuesFromReadables(offset, limit);
    }

    if (values != null && policies.contains(ReadPolicy.POPULATE_CACHE)) {
      populatePaginatedCaches(offset, limit, values);
    }

    return values;
  }

  @SafeVarargs
  protected final <R extends PaginatedReadable<V>> void addPaginatedReadables(R... readables) {
    this.paginatedReadables.addAll(Arrays.asList(readables));
  }

  @SafeVarargs
  protected final <R extends PaginatedCache<K, V>> void addPaginatedCaches(R... caches) {
    this.paginatedCaches.addAll(Arrays.asList(caches));
  }

  protected PaginatedCollection<V> getPaginatedValuesFromCaches(int offset, int limit) {
    PaginatedCollection<V> values = null;

    for (PaginatedCache<K, V> cache : paginatedCaches) {
      values = cache.get(offset, limit);
      if (!values.getItems().isEmpty()) {
        break;
      }
    }

    return values;
  }

  protected PaginatedCollection<V> getPaginatedValuesFromReadables(int offset, int limit) {
    PaginatedCollection<V> values = null;

    for (PaginatedReadable<V> readable : paginatedReadables) {
      values = readable.get(offset, limit);
      if (!values.getItems().isEmpty()) {
        break;
      }
    }

    return values;
  }

  protected void populatePaginatedCaches(int offset, int limit, PaginatedCollection<V> values) {
    for (PaginatedCache<K, V> cache : paginatedCaches) {
      cache.addOrUpdate(offset, limit, values.getItems(), values.hasMore());
    }
  }
}
