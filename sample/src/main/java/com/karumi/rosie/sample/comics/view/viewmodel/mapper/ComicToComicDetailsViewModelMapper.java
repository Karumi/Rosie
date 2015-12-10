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
import com.karumi.rosie.sample.comics.view.viewmodel.ComicDetailsViewModel;
import javax.inject.Inject;

public class ComicToComicDetailsViewModelMapper {

  @Inject public ComicToComicDetailsViewModelMapper() {
  }

  public ComicDetailsViewModel mapComicToComicDetailsViewModel(Comic comic) {
    ComicDetailsViewModel comicDetailsViewModel = new ComicDetailsViewModel();

    comicDetailsViewModel.setTitle(
        comic.getName() + " (" + comic.getReleaseYear() + ") #" + comic.getNumber());
    comicDetailsViewModel.setCoverUrl(comic.getCoverUrl());
    comicDetailsViewModel.setDescription(comic.getDescription());
    comicDetailsViewModel.setRatingNameResourceId(getRatingNameResourceId(comic.getRating()));

    return comicDetailsViewModel;
  }

  private int getRatingNameResourceId(Comic.Rating rating) {
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
