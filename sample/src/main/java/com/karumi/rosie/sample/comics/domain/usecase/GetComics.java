/*
 * Copyright (C) 2015 Karumi.
 */

package com.karumi.rosie.sample.comics.domain.usecase;

import com.karumi.rosie.domain.usecase.RosieUseCase;
import com.karumi.rosie.domain.usecase.annotation.UseCase;
import com.karumi.rosie.repository.PaginatedCollection;
import com.karumi.rosie.sample.comics.domain.model.Comic;
import com.karumi.rosie.sample.comics.repository.ComicsRepository;
import javax.inject.Inject;

public class GetComics extends RosieUseCase {

  private final ComicsRepository repository;

  @Inject public GetComics(ComicsRepository repository) {
    this.repository = repository;
  }

  @UseCase public void getComics(int offset, int limit) throws Exception {
    PaginatedCollection<Comic> comics = repository.getPage(offset, limit);
    notifySuccess(comics);
  }
}
