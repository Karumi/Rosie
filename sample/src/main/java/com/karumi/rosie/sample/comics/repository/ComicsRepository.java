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

package com.karumi.rosie.sample.comics.repository;

import com.karumi.rosie.repository.PaginatedRosieRepository;
import com.karumi.rosie.repository.datasource.CacheDataSource;
import com.karumi.rosie.repository.datasource.paginated.PaginatedCacheDataSource;
import com.karumi.rosie.sample.comics.domain.model.Comic;
import com.karumi.rosie.sample.comics.repository.datasource.ComicsApiDataSource;
import com.karumi.rosie.sample.main.ApplicationModule;
import javax.inject.Inject;
import javax.inject.Named;

public class ComicsRepository extends PaginatedRosieRepository<Integer, Comic> {

  @Inject public ComicsRepository(ComicsApiDataSource apiDataSource,
      @Named(ApplicationModule.COMICS_PAGE_IN_MEMORY_CACHE)
      PaginatedCacheDataSource<Integer, Comic> inMemoryPaginatedCache,
      @Named(ApplicationModule.COMICS_IN_MEMORY_CACHE)
      CacheDataSource<Integer, Comic> inMemoryCache) {
    addReadableDataSources(apiDataSource);
    addCacheDataSources(inMemoryCache);
    addPaginatedReadableDataSources(apiDataSource);
    addPaginatedCacheDataSources(inMemoryPaginatedCache);
  }
}
