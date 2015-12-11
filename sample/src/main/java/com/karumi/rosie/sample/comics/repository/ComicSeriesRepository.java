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
import com.karumi.rosie.sample.comics.domain.model.ComicSeries;
import com.karumi.rosie.sample.comics.repository.datasource.ComicSeriesApiDataSource;
import com.karumi.rosie.sample.main.ApplicationModule;
import javax.inject.Inject;
import javax.inject.Named;

public class ComicSeriesRepository extends PaginatedRosieRepository<Integer, ComicSeries> {

  @Inject public ComicSeriesRepository(ComicSeriesApiDataSource apiDataSource,
      @Named(ApplicationModule.COMIC_SERIES_PAGE_IN_MEMORY_CACHE)
      PaginatedCacheDataSource<Integer, ComicSeries> inMemoryPaginatedCache,
      @Named(ApplicationModule.COMIC_SERIES_IN_MEMORY_CACHE)
      CacheDataSource<Integer, ComicSeries> inMemoryCache) {
    addReadableDataSources(apiDataSource);
    addCacheDataSources(inMemoryCache);
    addPaginatedReadableDataSources(apiDataSource);
    addPaginatedCacheDataSources(inMemoryPaginatedCache);
  }
}
