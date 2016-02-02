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

package com.karumi.rosie.sample.main;

import com.karumi.rosie.domain.usecase.error.ErrorHandler;
import com.karumi.rosie.repository.datasource.paginated.InMemoryPaginatedCacheDataSource;
import com.karumi.rosie.repository.datasource.paginated.PaginatedCacheDataSource;
import com.karumi.rosie.sample.base.view.error.MarvelErrorFactory;
import com.karumi.rosie.sample.characters.domain.model.Character;
import com.karumi.rosie.sample.comics.domain.model.ComicSeries;
import com.karumi.rosie.sample.main.domain.usecase.GetMarvelSettings;
import com.karumi.rosie.time.TimeProvider;
import dagger.Module;
import dagger.Provides;
import javax.inject.Singleton;

import static java.util.concurrent.TimeUnit.MINUTES;

@Module(library = true,
    complete = false,
    injects = {
        MainApplication.class
    }) public class ApplicationModule {

  private static final long CHARACTERS_IN_MEMORY_CACHE_TTL = MINUTES.toMillis(5);
  private static final long COMICS_IN_MEMORY_CACHE_TTL = MINUTES.toMillis(5);

  @Provides @Singleton
  public PaginatedCacheDataSource<String, Character> provideCharactersPageInMemoryCache() {
    return new InMemoryPaginatedCacheDataSource<>(new TimeProvider(),
        CHARACTERS_IN_MEMORY_CACHE_TTL);
  }

  @Provides @Singleton
  public PaginatedCacheDataSource<Integer, ComicSeries> provideComicsPageInMemoryCache() {
    return new InMemoryPaginatedCacheDataSource<>(new TimeProvider(), COMICS_IN_MEMORY_CACHE_TTL);
  }

  @Provides public ErrorHandler providesErrorHandler(MarvelErrorFactory errorFactory) {
    return new ErrorHandler(errorFactory);
  }

  @Provides @Singleton public GetMarvelSettings provideGetMarvelSettings() {
    return new GetMarvelSettings();
  }

}
