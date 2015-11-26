/*
 * Copyright (C) 2015 Karumi.
 */

package com.karumi.rosie.sample.main;

import com.karumi.rosie.repository.datasource.paginated.InMemoryPaginatedCacheDataSource;
import com.karumi.rosie.repository.datasource.paginated.PaginatedCacheDataSource;
import com.karumi.rosie.time.TimeProvider;
import com.karumi.rosie.sample.characters.domain.model.Character;
import com.karumi.rosie.sample.characters.view.fragment.CharactersFragment;
import com.karumi.rosie.sample.comics.view.fragment.ComicsFragment;
import com.karumi.rosie.sample.main.view.activity.MainActivity;
import dagger.Module;
import dagger.Provides;
import javax.inject.Named;
import javax.inject.Singleton;

import static java.util.concurrent.TimeUnit.MINUTES;

@Module(library = true,
    complete = false,
    injects = {
        MainApplication.class, MainActivity.class, CharactersFragment.class, ComicsFragment.class
    }) public class MainModule {

  public static final String CHARACTERS_IN_MEMORY_CACHE = "CharactersInMemoryCache";

  private static final long CHARACTERS_IN_MEMORY_CACHE_TTL = MINUTES.toMillis(5);

  @Provides @Singleton @Named(CHARACTERS_IN_MEMORY_CACHE)
  public PaginatedCacheDataSource<String, Character> provideCharactersInMemoryCache() {
    return new InMemoryPaginatedCacheDataSource<>(new TimeProvider(),
        CHARACTERS_IN_MEMORY_CACHE_TTL);
  }
}
