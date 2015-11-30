/*
 * Copyright (C) 2015 Karumi.
 */

package com.karumi.rosie.sample.comics.view.viewmodel.mapper;

import com.karumi.rosie.repository.PaginatedCollection;
import com.karumi.rosie.sample.comics.domain.model.Comic;
import com.karumi.rosie.sample.comics.view.viewmodel.ComicViewModel;
import java.util.LinkedList;
import java.util.List;
import javax.inject.Inject;

public class ComicToComicViewModelMapper {

  @Inject public ComicToComicViewModelMapper() {
  }

  public List<ComicViewModel> mapComicsToComicViewModels(PaginatedCollection<Comic> comics) {
    List<ComicViewModel> comicViewModels = new LinkedList<>();

    for (Comic comic : comics.getItems()) {
      ComicViewModel comicViewModel = new ComicViewModel();
      comicViewModel.setKey(comic.getKey());
      comicViewModel.setName(comic.getName());
      comicViewModel.setCoverUrl(comic.getCoverUrl());
      comicViewModels.add(comicViewModel);
    }

    return comicViewModels;
  }
}
