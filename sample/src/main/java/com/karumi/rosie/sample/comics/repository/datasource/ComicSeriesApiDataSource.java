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

package com.karumi.rosie.sample.comics.repository.datasource;

import com.karumi.marvelapiclient.MarvelApiException;
import com.karumi.marvelapiclient.SeriesApiClient;
import com.karumi.marvelapiclient.model.MarvelResponse;
import com.karumi.marvelapiclient.model.SeriesCollectionDto;
import com.karumi.marvelapiclient.model.SeriesDto;
import com.karumi.rosie.repository.PaginatedCollection;
import com.karumi.rosie.repository.datasource.EmptyReadableDataSource;
import com.karumi.rosie.repository.datasource.paginated.Page;
import com.karumi.rosie.repository.datasource.paginated.PaginatedReadableDataSource;
import com.karumi.rosie.sample.characters.repository.datasource.mapper.ComicSeriesToSeriesDtoMapper;
import com.karumi.rosie.sample.comics.domain.model.ComicSeries;
import java.util.Collection;
import javax.inject.Inject;

public class ComicSeriesApiDataSource extends EmptyReadableDataSource<Integer, ComicSeries>
    implements PaginatedReadableDataSource<Integer, ComicSeries> {

  private final SeriesApiClient seriesApiClient;
  private final ComicSeriesToSeriesDtoMapper mapper = new ComicSeriesToSeriesDtoMapper();

  @Inject public ComicSeriesApiDataSource(SeriesApiClient seriesApiClient) {
    this.seriesApiClient = seriesApiClient;
  }

  @Override public ComicSeries getByKey(Integer key) throws Exception {
    MarvelResponse<SeriesDto> seriesResponse = seriesApiClient.getSeriesById(key.toString());
    SeriesDto seriesDto = seriesResponse.getResponse();

    return mapper.reverseMap(seriesDto);
  }

  @Override public PaginatedCollection<ComicSeries> getPage(Page page)
      throws MarvelApiException {
    int offset = page.getOffset();
    int limit = page.getLimit();
    MarvelResponse<SeriesCollectionDto> seriesApiResponse = seriesApiClient.getAll(offset, limit);

    SeriesCollectionDto seriesCollectionDto = seriesApiResponse.getResponse();
    Collection<ComicSeries> comicSeries = mapper.reverseMap(seriesCollectionDto.getSeries());

    PaginatedCollection<ComicSeries> comicSeriesPage = new PaginatedCollection<>(comicSeries);
    comicSeriesPage.setPage(page);
    comicSeriesPage.setHasMore(
        seriesCollectionDto.getOffset() + seriesCollectionDto.getCount()
            < seriesCollectionDto.getTotal());
    return comicSeriesPage;
  }
}
