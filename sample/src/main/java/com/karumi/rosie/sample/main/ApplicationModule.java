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

import com.karumi.rosie.repository.datasource.CacheDataSource;
import com.karumi.rosie.repository.datasource.InMemoryCacheDataSource;
import com.karumi.rosie.repository.datasource.paginated.InMemoryPaginatedCacheDataSource;
import com.karumi.rosie.repository.datasource.paginated.PaginatedCacheDataSource;
import com.karumi.rosie.sample.characters.domain.model.Character;
import com.karumi.rosie.sample.comics.domain.model.ComicSeries;
import com.karumi.rosie.time.TimeProvider;
import dagger.Module;
import dagger.Provides;
import javax.inject.Named;
import javax.inject.Singleton;

import static java.util.concurrent.TimeUnit.MINUTES;

@Module(library = true,
    complete = false,
    injects = {
        MainApplication.class
    }) public class ApplicationModule {

  public static final String CHARACTERS_PAGE_IN_MEMORY_CACHE = "CharactersPageInMemoryCache";
  public static final String CHARACTERS_IN_MEMORY_CACHE = "CharactersInMemoryCache";

  public static final String COMIC_SERIES_PAGE_IN_MEMORY_CACHE = "ComicSeriesPageInMemoryCache";
  public static final String COMIC_SERIES_IN_MEMORY_CACHE = "ComicSeriesInMemoryCache";

  private static final long CHARACTERS_IN_MEMORY_CACHE_TTL = MINUTES.toMillis(5);
  private static final long COMICS_IN_MEMORY_CACHE_TTL = MINUTES.toMillis(5);

  @Provides @Singleton @Named(CHARACTERS_PAGE_IN_MEMORY_CACHE)
  public PaginatedCacheDataSource<String, Character> provideCharactersPageInMemoryCache() {
    return new InMemoryPaginatedCacheDataSource<>(new TimeProvider(),
        CHARACTERS_IN_MEMORY_CACHE_TTL);
  }

  @Provides @Singleton @Named(CHARACTERS_IN_MEMORY_CACHE)
  public CacheDataSource<String, Character> provideCharactersInMemoryCache() {
    return new InMemoryCacheDataSource<>(new TimeProvider(), CHARACTERS_IN_MEMORY_CACHE_TTL);
  }

  @Provides @Singleton @Named(COMIC_SERIES_PAGE_IN_MEMORY_CACHE)
  public PaginatedCacheDataSource<Integer, ComicSeries> provideComicsPageInMemoryCache() {
    return new InMemoryPaginatedCacheDataSource<>(new TimeProvider(), COMICS_IN_MEMORY_CACHE_TTL);
  }

  @Provides @Singleton @Named(COMIC_SERIES_IN_MEMORY_CACHE)
  public CacheDataSource<Integer, ComicSeries> provideComicsInMemoryCache() {
    return new InMemoryCacheDataSource<>(new TimeProvider(), COMICS_IN_MEMORY_CACHE_TTL);
  }
}
