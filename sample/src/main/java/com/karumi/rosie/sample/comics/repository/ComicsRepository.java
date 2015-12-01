/*
 * Copyright (C) 2015 Karumi.
 */

package com.karumi.rosie.sample.comics.repository;

import com.karumi.rosie.repository.PaginatedRosieRepository;
import com.karumi.rosie.repository.datasource.CacheDataSource;
import com.karumi.rosie.repository.datasource.paginated.PaginatedCacheDataSource;
import com.karumi.rosie.sample.comics.domain.model.Comic;
import com.karumi.rosie.sample.comics.repository.datasource.ComicsApiDataSource;
import com.karumi.rosie.sample.main.MainModule;
import javax.inject.Inject;
import javax.inject.Named;

public class ComicsRepository extends PaginatedRosieRepository<Integer, Comic> {

  @Inject public ComicsRepository(ComicsApiDataSource apiDataSource,
      @Named(MainModule.COMICS_PAGE_IN_MEMORY_CACHE)
      PaginatedCacheDataSource<Integer, Comic> inMemoryPaginatedCache,
      @Named(MainModule.COMICS_IN_MEMORY_CACHE) CacheDataSource<Integer, Comic> inMemoryCache) {
    addReadableDataSources(apiDataSource);
    addCacheDataSources(inMemoryCache);
    addPaginatedReadableDataSources(apiDataSource);
    addPaginatedCacheDataSources(inMemoryPaginatedCache);
  }
}
