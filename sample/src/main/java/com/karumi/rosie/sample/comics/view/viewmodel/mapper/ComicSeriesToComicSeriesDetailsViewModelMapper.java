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

import com.karumi.rosie.sample.R;
import com.karumi.rosie.sample.comics.domain.model.Comic;
import com.karumi.rosie.sample.comics.domain.model.ComicSeries;
import com.karumi.rosie.sample.comics.view.viewmodel.ComicSeriesDetailViewModel;
import com.karumi.rosie.sample.comics.view.viewmodel.ComicSeriesDetailsViewModel;
import com.karumi.rosie.sample.comics.view.viewmodel.ComicSeriesHeaderDetailViewModel;
import com.karumi.rosie.sample.comics.view.viewmodel.ComicViewModel;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import javax.inject.Inject;

public class ComicSeriesToComicSeriesDetailsViewModelMapper {

  @Inject public ComicSeriesToComicSeriesDetailsViewModelMapper() {
  }

  public ComicSeriesDetailsViewModel mapComicSeriesToComicSeriesDetailsViewModel(
      ComicSeries comicSeries) {
    ComicSeriesDetailsViewModel comicSeriesDetailsViewModel = new ComicSeriesDetailsViewModel();

    comicSeriesDetailsViewModel.setTitle(comicSeries.getName());
    List<ComicSeriesDetailViewModel> comicSeriesDetailViewModels = new LinkedList<>();
    comicSeriesDetailViewModels.add(mapComicSeriesToComicSeriesHeaderDetailViewModel(comicSeries));
    comicSeriesDetailViewModels.addAll(mapComicsToComicViewModels(comicSeries.getComics()));
    comicSeriesDetailsViewModel.setComicSeriesDetailViewModels(comicSeriesDetailViewModels);

    return comicSeriesDetailsViewModel;
  }

  private ComicSeriesHeaderDetailViewModel mapComicSeriesToComicSeriesHeaderDetailViewModel(
      ComicSeries comicSeries) {
    ComicSeriesHeaderDetailViewModel comicSeriesHeaderDetailViewModel =
        new ComicSeriesHeaderDetailViewModel();

    comicSeriesHeaderDetailViewModel.setTitle(
        comicSeries.getName() + " (" + comicSeries.getReleaseYear() + ") #"
            + comicSeries.getNumber());
    comicSeriesHeaderDetailViewModel.setCoverUrl(comicSeries.getCoverUrl());
    comicSeriesHeaderDetailViewModel.setDescription(comicSeries.getDescription());
    comicSeriesHeaderDetailViewModel.setRatingNameResourceId(
        mapRatingToRatingNameResourceId(comicSeries.getRating()));

    return comicSeriesHeaderDetailViewModel;
  }

  private int mapRatingToRatingNameResourceId(ComicSeries.Rating rating) {
    int ratingNameResourceId;
    switch (rating) {
      case ALL_AGES:
        ratingNameResourceId = R.string.marvel_rating_all_ages;
        break;
      case T:
        ratingNameResourceId = R.string.marvel_rating_t;
        break;
      case TEENS_AND_UP:
        ratingNameResourceId = R.string.marvel_rating_teens_and_up;
        break;
      case PARENTAL_ADVISORY:
        ratingNameResourceId = R.string.marvel_rating_parental_advisory;
        break;
      case EXPLICIT_CONTENT:
      default:
        ratingNameResourceId = R.string.marvel_rating_explicit_content;
        break;
    }

    return ratingNameResourceId;
  }

  private List<ComicViewModel> mapComicsToComicViewModels(List<Comic> comics) {
    List<ComicViewModel> comicViewModels = new ArrayList<>();

    for (Comic comic : comics) {
      ComicViewModel comicViewModel = new ComicViewModel();
      comicViewModel.setKey(comic.getKey());
      comicViewModel.setTitle(comic.getName());
      comicViewModel.setThumbnailUrl(comic.getThumbnailUrl());
      comicViewModels.add(comicViewModel);
    }

    return comicViewModels;
  }
}
