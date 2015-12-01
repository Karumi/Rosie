/*
 * Copyright (C) 2015 Karumi.
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
