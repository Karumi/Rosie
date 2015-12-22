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

package com.karumi.rosie.sample.comics.view.viewmodel.mapper;

import com.karumi.rosie.repository.PaginatedCollection;
import com.karumi.rosie.sample.comics.domain.model.ComicSeries;
import com.karumi.rosie.sample.comics.view.viewmodel.ComicSeriesViewModel;
import java.util.LinkedList;
import java.util.List;
import javax.inject.Inject;

public class ComicSeriesToComicSeriesViewModelMapper {

  @Inject public ComicSeriesToComicSeriesViewModelMapper() {
  }

  public List<ComicSeriesViewModel> mapComicSeriesToComicSeriesViewModels(
      PaginatedCollection<ComicSeries> comics) {
    List<ComicSeriesViewModel> comicSeriesViewModels = new LinkedList<>();

    for (ComicSeries comicSeries : comics.getItems()) {
      ComicSeriesViewModel comicSeriesViewModel = new ComicSeriesViewModel();
      comicSeriesViewModel.setKey(comicSeries.getKey());
      String titleFormatted =
          String.format("%1$s (%2$s)", comicSeries.getName(), comicSeries.getReleaseYear());
      comicSeriesViewModel.setTitle(titleFormatted);
      comicSeriesViewModel.setCoverUrl(comicSeries.getCoverUrl());
      comicSeriesViewModels.add(comicSeriesViewModel);
    }

    return comicSeriesViewModels;
  }
}
