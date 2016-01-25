/*
 *  Copyright (C) 2015 Karumi.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package com.karumi.rosie.sample.main.view.activity;

import android.support.test.espresso.intent.rule.IntentsTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.test.suitebuilder.annotation.LargeTest;
import com.karumi.rosie.repository.PaginatedCollection;
import com.karumi.rosie.repository.datasource.paginated.Page;
import com.karumi.rosie.repository.policy.ReadPolicy;
import com.karumi.rosie.sample.InjectedInstrumentationTest;
import com.karumi.rosie.sample.R;
import com.karumi.rosie.sample.characters.domain.model.Character;
import com.karumi.rosie.sample.characters.repository.CharactersRepository;
import com.karumi.rosie.sample.characters.view.fragment.CharactersFragment;
import com.karumi.rosie.sample.comics.domain.model.ComicSeries;
import com.karumi.rosie.sample.comics.repository.ComicSeriesRepository;
import com.karumi.rosie.sample.comics.view.fragment.ComicSeriesFragment;
import dagger.Module;
import dagger.Provides;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.inject.Inject;
import javax.inject.Singleton;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static com.karumi.rosie.sample.AncestorMatcher.withAncestor;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.not;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(AndroidJUnit4.class) @LargeTest public class MainActivityTest
    extends InjectedInstrumentationTest {

  @Inject CharactersRepository charactersRepository;
  @Inject ComicSeriesRepository comicSeriesRepository;

  @Rule public IntentsTestRule<MainActivity> activityRule =
      new IntentsTestRule<>(MainActivity.class, true, false);

  @Before public void setUp() {
    super.setUp();
  }

  @Test public void shouldHideLoadingWhenDataIsLoaded() throws Exception {
    givenEmptyCharacters();
    givenEmptyComicSeries();
    startActivity();

    onView(allOf(withId(R.id.loading), withAncestor(withId(R.id.fragment_characters)))).check(
        matches(not(isDisplayed())));
  }

  private void givenEmptyCharacters() throws Exception {
    when(charactersRepository.getAll(ReadPolicy.CACHE_ONLY)).thenReturn(new ArrayList<Character>());
    PaginatedCollection<Character> emptyPage = new PaginatedCollection<>();
    emptyPage.setPage(Page.withOffsetAndLimit(0, 0));
    emptyPage.setHasMore(false);
    when(charactersRepository.getPage(any(Page.class))).thenReturn(emptyPage);
  }

  private void givenEmptyComicSeries() throws Exception {
    when(comicSeriesRepository.getAll(ReadPolicy.CACHE_ONLY)).thenReturn(
        new ArrayList<ComicSeries>());
    PaginatedCollection<ComicSeries> emptyPage = new PaginatedCollection<>();
    emptyPage.setPage(Page.withOffsetAndLimit(0, 0));
    emptyPage.setHasMore(false);
    when(comicSeriesRepository.getPage(any(Page.class))).thenReturn(emptyPage);
  }

  private MainActivity startActivity() {
    return activityRule.launchActivity(null);
  }

  @Override public List<Object> getTestModules() {
    return Arrays.asList((Object) new TestModule());
  }

  @Module(overrides = true, library = true, complete = false,
      injects = {
          MainActivity.class, CharactersFragment.class, ComicSeriesFragment.class,
          MainActivityTest.class
      }) class TestModule {

    @Provides
    @Singleton
    public CharactersRepository provideCharactersRepository() {
      return mock(CharactersRepository.class);
    }

    @Provides
    @Singleton
    public ComicSeriesRepository provideComicSeriesRepository() {
      return mock(ComicSeriesRepository.class);
    }
  }
}