/*
 * Copyright (C) 2015 Karumi.
 */

package com.karumi.rosie.sample.comics.domain.usecase;

import com.karumi.rosie.domain.usecase.RosieUseCase;
import com.karumi.rosie.domain.usecase.annotation.UseCase;
import com.karumi.rosie.sample.comics.domain.model.Comic;
import com.karumi.rosie.sample.comics.repository.ComicsRepository;
import javax.inject.Inject;

public class GetComicDetails extends RosieUseCase {

  private final ComicsRepository repository;

  @Inject public GetComicDetails(ComicsRepository repository) {
    this.repository = repository;
  }

  @UseCase public void getComicDetails(int comicKey) {
    Comic comic = repository.getByKey(comicKey);
    notifySuccess(comic);
  }
}
