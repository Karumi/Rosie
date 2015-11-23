/*
 * Copyright (C) 2015 Karumi.
 */

package com.karumi.rosie.repositorynew.sample;

import com.karumi.rosie.repositorynew.PaginatedRepository;

public class SamplePaginatedRepository extends PaginatedRepository<SampleKey, SampleValue> {

  public SamplePaginatedRepository(SamplePaginatedCache cache) {
    addPaginatedCaches(cache);
  }
}
