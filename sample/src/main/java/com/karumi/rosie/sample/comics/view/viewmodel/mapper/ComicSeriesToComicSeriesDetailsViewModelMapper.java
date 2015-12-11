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
import com.karumi.rosie.sample.comics.domain.model.ComicSeries;
import com.karumi.rosie.sample.comics.view.viewmodel.ComicSeriesDetailsViewModel;
import javax.inject.Inject;

public class ComicSeriesToComicSeriesDetailsViewModelMapper {

  @Inject public ComicSeriesToComicSeriesDetailsViewModelMapper() {
  }

  public ComicSeriesDetailsViewModel mapComicSeriesToComicSeriesDetailsViewModel(
      ComicSeries comicSeries) {
    ComicSeriesDetailsViewModel comicSeriesDetailsViewModel = new ComicSeriesDetailsViewModel();

    comicSeriesDetailsViewModel.setTitle(
        comicSeries.getName() + " (" + comicSeries.getReleaseYear() + ") #" + comicSeries.getNumber());
    comicSeriesDetailsViewModel.setCoverUrl(comicSeries.getCoverUrl());
    comicSeriesDetailsViewModel.setDescription(comicSeries.getDescription());
    comicSeriesDetailsViewModel.setRatingNameResourceId(getRatingNameResourceId(comicSeries.getRating()));

    return comicSeriesDetailsViewModel;
  }

  private int getRatingNameResourceId(ComicSeries.Rating rating) {
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
}
