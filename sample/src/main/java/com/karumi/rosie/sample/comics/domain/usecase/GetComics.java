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
