/*
 * Copyright (C) 2015 Karumi.
 */

package com.karumi.rosie.repositorynew.sample;

import com.karumi.rosie.repository.PaginatedCollection;
import com.karumi.rosie.repositorynew.PaginatedCache;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class SamplePaginatedCache implements PaginatedCache<SampleKey, SampleValue> {

  private static final List<SampleValue> VALUES = new ArrayList<>();

  static {
    VALUES.add(new SampleValue(new SampleKey(0), "Nombre0", "Apellido0", 20));
    VALUES.add(new SampleValue(new SampleKey(1), "Nombre1", "Apellido1", 21));
    VALUES.add(new SampleValue(new SampleKey(2), "Nombre2", "Apellido2", 22));
    VALUES.add(new SampleValue(new SampleKey(3), "Nombre3", "Apellido3", 23));
    VALUES.add(new SampleValue(new SampleKey(4), "Nombre4", "Apellido4", 24));
    VALUES.add(new SampleValue(new SampleKey(5), "Nombre5", "Apellido5", 25));
  }

  @Override public PaginatedCollection<SampleValue> get(int offset, int limit) {
    Collection<SampleValue> items = new ArrayList<>();

    for (int i = offset; i < offset + limit && i < VALUES.size(); i++) {
      items.add(VALUES.get(i));
    }

    PaginatedCollection<SampleValue> paginatedItems = new PaginatedCollection<>(items);
    paginatedItems.setOffset(offset);
    paginatedItems.setLimit(limit);
    paginatedItems.setHasMore(offset + limit >= VALUES.size());
    return paginatedItems;
  }

  @Override public PaginatedCollection<SampleValue> addOrUpdate(int offset, int limit,
      Collection<SampleValue> items, boolean hasMore) {
    int i = offset;
    for (SampleValue value : items) {
      if (i < VALUES.size() && i < offset + limit) {
        VALUES.add(i, value);
      }
      i++;
    }
    return get(offset, limit);
  }
}
