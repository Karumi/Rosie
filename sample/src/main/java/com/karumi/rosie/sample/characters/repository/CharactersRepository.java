/*
 * Copyright (C) 2015 Karumi.
 */

package com.karumi.rosie.sample.characters.repository;

import com.karumi.rosie.repository.PaginatedRosieRepository;
import com.karumi.rosie.repository.datasource.CacheDataSource;
import com.karumi.rosie.repository.datasource.paginated.PaginatedCacheDataSource;
import com.karumi.rosie.sample.characters.domain.model.Character;
import com.karumi.rosie.sample.characters.repository.datasource.CharactersApiDataSource;
import com.karumi.rosie.sample.main.MainModule;
import javax.inject.Inject;
import javax.inject.Named;

public class CharactersRepository extends PaginatedRosieRepository<String, Character> {

  @Inject public CharactersRepository(CharactersApiDataSource apiDataSource,
      @Named(MainModule.CHARACTERS_PAGE_IN_MEMORY_CACHE)
      PaginatedCacheDataSource<String, Character> inMemoryPaginatedCache,
      @Named(MainModule.CHARACTERS_IN_MEMORY_CACHE)
      CacheDataSource<String, Character> inMemoryCache) {
    addReadableDataSources(apiDataSource);
    addCacheDataSources(inMemoryCache);
    addPaginatedReadableDataSources(apiDataSource);
    addPaginatedCacheDataSources(inMemoryPaginatedCache);
  }
}
