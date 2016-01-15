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

package com.karumi.rosie.sample.comics.domain.usecase;

import com.karumi.rosie.domain.usecase.RosieUseCase;
import com.karumi.rosie.domain.usecase.annotation.UseCase;
import com.karumi.rosie.repository.PaginatedCollection;
import com.karumi.rosie.repository.datasource.paginated.Page;
import com.karumi.rosie.repository.policy.ReadPolicy;
import com.karumi.rosie.sample.comics.domain.model.ComicSeries;
import com.karumi.rosie.sample.comics.repository.ComicSeriesRepository;
import java.util.ArrayList;
import java.util.Collection;
import javax.inject.Inject;

public class GetComicSeriesPage extends RosieUseCase {

  private final ComicSeriesRepository repository;

  @Inject public GetComicSeriesPage(ComicSeriesRepository repository) {
    this.repository = repository;
  }

  public PaginatedCollection<ComicSeries> getAllComicsInCache() {

    Collection<ComicSeries> all;
    try {
      all = repository.getAll(ReadPolicy.CACHE_ONLY);
    } catch (Exception e) {
      all = new ArrayList<>();
    }

    if (all == null) {
      all = new ArrayList<>();
    }

    Page page = Page.withOffsetAndLimit(0, all.size());

    PaginatedCollection<ComicSeries> comics = new PaginatedCollection<>(all);
    comics.setPage(page);
    comics.setHasMore(true);

    return comics;
  }

  @UseCase public void getComics(Page page) throws Exception {
    PaginatedCollection<ComicSeries> comics = repository.getPage(page);
    notifySuccess(comics);
  }
}
