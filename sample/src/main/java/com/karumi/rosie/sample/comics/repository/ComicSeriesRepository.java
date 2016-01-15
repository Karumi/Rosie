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
import com.karumi.rosie.repository.datasource.paginated.PaginatedCacheDataSource;
import com.karumi.rosie.sample.comics.domain.model.Comic;
import com.karumi.rosie.sample.comics.domain.model.ComicSeries;
import com.karumi.rosie.sample.comics.repository.datasource.ComicSeriesDataSource;
import java.util.List;
import javax.inject.Inject;

public class ComicSeriesRepository extends PaginatedRosieRepository<Integer, ComicSeries> {

  private final ComicSeriesDataSource apiDataSource;

  @Inject public ComicSeriesRepository(ComicSeriesDataSourceFactory comicSeriesDataSourceFactory,
      PaginatedCacheDataSource<Integer, ComicSeries> inMemoryPaginatedCache) {

    this.apiDataSource = comicSeriesDataSourceFactory.createDataSource();
    addReadableDataSources(this.apiDataSource);
    addPaginatedReadableDataSources(this.apiDataSource);

    addCacheDataSources(inMemoryPaginatedCache);
    addPaginatedCacheDataSources(inMemoryPaginatedCache);
  }

  public ComicSeries getComicSeriesDetail(Integer key) throws Exception {
    ComicSeries comicSeries = getByKey(key);
    if (!comicSeries.isComplete()) {
      List<Comic> comicBySeries = apiDataSource.getComicBySeries(key);
      comicSeries.setComics(comicBySeries);
      comicSeries.setComplete(true);
    }
    return comicSeries;
  }
}
