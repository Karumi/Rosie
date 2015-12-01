/*
 * Copyright (C) 2015 Karumi.
 */

package com.karumi.rosie.sample.main;

import com.karumi.rosie.repository.datasource.CacheDataSource;
import com.karumi.rosie.repository.datasource.InMemoryCacheDataSource;
import com.karumi.rosie.repository.datasource.paginated.InMemoryPaginatedCacheDataSource;
import com.karumi.rosie.repository.datasource.paginated.PaginatedCacheDataSource;
import com.karumi.rosie.sample.characters.domain.model.Character;
import com.karumi.rosie.sample.characters.view.activity.CharacterDetailsActivity;
import com.karumi.rosie.sample.characters.view.fragment.CharactersFragment;
import com.karumi.rosie.sample.comics.domain.model.Comic;
import com.karumi.rosie.sample.comics.view.activity.ComicDetailsActivity;
import com.karumi.rosie.sample.comics.view.fragment.ComicsFragment;
import com.karumi.rosie.sample.main.view.activity.MainActivity;
import com.karumi.rosie.time.TimeProvider;
import dagger.Module;
import dagger.Provides;
import javax.inject.Named;
import javax.inject.Singleton;

import static java.util.concurrent.TimeUnit.MINUTES;

@Module(library = true,
    complete = false,
    injects = {
        MainApplication.class, MainActivity.class, CharactersFragment.class, ComicsFragment.class,
        CharacterDetailsActivity.class, ComicDetailsActivity.class
    }) public class MainModule {

  public static final String CHARACTERS_PAGE_IN_MEMORY_CACHE = "CharactersInMemoryCache";
  public static final String CHARACTERS_IN_MEMORY_CACHE = "CharactersInMemoryCache";

  public static final String COMICS_PAGE_IN_MEMORY_CACHE = "ComicsInMemoryCache";
  public static final String COMICS_IN_MEMORY_CACHE = "ComicsInMemoryCache";

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

  @Provides @Singleton @Named(COMICS_PAGE_IN_MEMORY_CACHE)
  public PaginatedCacheDataSource<Integer, Comic> provideComicsPageInMemoryCache() {
    return new InMemoryPaginatedCacheDataSource<>(new TimeProvider(), COMICS_IN_MEMORY_CACHE_TTL);
  }

  @Provides @Singleton @Named(COMICS_IN_MEMORY_CACHE)
  public CacheDataSource<Integer, Comic> provideComicsInMemoryCache() {
    return new InMemoryCacheDataSource<>(new TimeProvider(), COMICS_IN_MEMORY_CACHE_TTL);
  }
}
