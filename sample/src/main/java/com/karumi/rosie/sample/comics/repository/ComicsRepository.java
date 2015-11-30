/*
 * Copyright (C) 2015 Karumi.
 */

package com.karumi.rosie.sample.comics.repository;

import com.karumi.rosie.repository.PaginatedRosieRepository;
import com.karumi.rosie.sample.comics.domain.model.Comic;
import com.karumi.rosie.sample.comics.repository.datasource.ComicsApiDataSource;
import javax.inject.Inject;

public class ComicsRepository extends PaginatedRosieRepository<Integer, Comic> {

  @Inject public ComicsRepository(ComicsApiDataSource apiDataSource) {
    addReadableDataSources(apiDataSource);
    addPaginatedReadableDataSources(apiDataSource);
  }
}
