/*
 * Copyright (C) 2015 Karumi.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.karumi.rosie.sample.characters.repository;

import com.karumi.rosie.repository.PaginatedRosieRepository;
import com.karumi.rosie.repository.datasource.CacheDataSource;
import com.karumi.rosie.repository.datasource.paginated.PaginatedCacheDataSource;
import com.karumi.rosie.sample.characters.domain.model.Character;
import com.karumi.rosie.sample.characters.repository.datasource.CharactersApiDataSource;
import com.karumi.rosie.sample.main.ApplicationModule;
import javax.inject.Inject;
import javax.inject.Named;

public class CharactersRepository extends PaginatedRosieRepository<String, Character> {

  @Inject public CharactersRepository(CharactersApiDataSource apiDataSource,
      @Named(ApplicationModule.CHARACTERS_PAGE_IN_MEMORY_CACHE)
      PaginatedCacheDataSource<String, Character> inMemoryPaginatedCache,
      @Named(ApplicationModule.CHARACTERS_IN_MEMORY_CACHE)
      CacheDataSource<String, Character> inMemoryCache) {
    addReadableDataSources(apiDataSource);
    addCacheDataSources(inMemoryCache);
    addPaginatedReadableDataSources(apiDataSource);
    addPaginatedCacheDataSources(inMemoryPaginatedCache);
  }
}
