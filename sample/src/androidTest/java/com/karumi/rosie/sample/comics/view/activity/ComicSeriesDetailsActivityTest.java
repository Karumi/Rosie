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

package com.karumi.rosie.sample.comics.view.activity;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.test.espresso.NoMatchingViewException;
import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.espresso.intent.rule.IntentsTestRule;
import android.support.test.espresso.matcher.ViewMatchers;
import android.view.View;
import com.karumi.marvelapiclient.MarvelApiException;
import com.karumi.rosie.sample.InjectedInstrumentationTest;
import com.karumi.rosie.sample.R;
import com.karumi.rosie.sample.comics.domain.model.Comic;
import com.karumi.rosie.sample.comics.domain.model.ComicSeries;
import com.karumi.rosie.sample.comics.repository.ComicSeriesRepository;
import com.karumi.rosie.sample.comics.view.viewmodel.ComicSeriesDetailViewModel;
import com.karumi.rosie.sample.comics.view.viewmodel.ComicSeriesDetailsViewModel;
import com.karumi.rosie.sample.comics.view.viewmodel.ComicViewModel;
import com.karumi.rosie.sample.comics.view.viewmodel.mapper.ComicSeriesToComicSeriesDetailsViewModelMapper;
import com.karumi.rosie.sample.recyclerview.RecyclerViewInteraction;
import dagger.Module;
import dagger.Provides;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.inject.Inject;
import javax.inject.Singleton;
import org.junit.Rule;
import org.junit.Test;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.hasDescendant;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withEffectiveVisibility;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.not;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ComicSeriesDetailsActivityTest extends InjectedInstrumentationTest {

  private static final int ANY_COMIC_SERIES_ID = 1;
  private static final String ANY_EXCEPTION = "AnyException";
  @Rule public IntentsTestRule<ComicSeriesDetailsActivity> activityRule =
      new IntentsTestRule<>(ComicSeriesDetailsActivity.class, true, false);

  @Inject
  ComicSeriesRepository comicSeriesRepository;

  @Test public void shouldShowComicSeriesDetailWhenComicSeriesIsLoaded() throws Exception {
    ComicSeries comicSeries = givenValidComicSeries();

    startActivity();

    onView(withId(R.id.tv_toolbar_title)).check(matches(withText(comicSeries.getName())));
    onView(withId(R.id.tv_description)).check(matches(withText(comicSeries.getDescription())));
  }

  @Test public void shouldShowComicIfComicSeriesHaveComics() throws Exception {
    ComicSeries comicSeries = givenValidComicSeries();
    List<ComicSeriesDetailViewModel> comics = givenComicData(comicSeries);

    startActivity();
    onView(withId(R.id.rv_comics)).perform(RecyclerViewActions.scrollToPosition(1));

    assertRecyclerViewShowComics(comics);
  }

  private void assertRecyclerViewShowComics(List<ComicSeriesDetailViewModel> comics) {
    RecyclerViewInteraction.<ComicSeriesDetailViewModel>onRecyclerView(
        withId(R.id.rv_comics))
        .withItems(comics)
        .check(new RecyclerViewInteraction.ItemViewAssertion<ComicSeriesDetailViewModel>() {
          @Override public void check(ComicSeriesDetailViewModel comic, View view,
              NoMatchingViewException e) {
            if (comic instanceof ComicViewModel) {
              ComicViewModel comicViewModel = (ComicViewModel) comic;
              matches(hasDescendant(withText(comicViewModel.getTitle()))).check(view, e);
            }
          }
        });
  }

  @Test public void shouldHideLoadingWhenComicSeriesIsLoaded() throws Exception {
    givenValidComicSeries();

    startActivity();

    onView((withId(R.id.loading))).check(matches(not(isDisplayed())));
  }

  @Test public void shouldShowErrorIfSomethingWrongHappens() throws Exception {
    givenExceptionObtainingComicSeries();

    startActivity();

    onView(allOf(withId(android.support.design.R.id.snackbar_text), withText("¯\\_(ツ)_/¯"))).check(
        matches(withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE)));
  }

  @Test public void shouldShowConnectionErrorIfHaveConnectionTroubles() throws Exception {
    givenConnectionExceptionObtainingComicSeries();

    startActivity();

    onView(allOf(withId(android.support.design.R.id.snackbar_text),
        withText("Connection troubles. Ask to Ironman!"))).check(
        matches(withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE)));
  }

  private ComicSeries givenValidComicSeries() throws Exception {
    ComicSeries comicSeries = getComicSeries(ANY_COMIC_SERIES_ID);
    when(comicSeriesRepository.getComicSeriesDetail(anyInt())).thenReturn(
        comicSeries);
    return comicSeries;
  }

  private void givenExceptionObtainingComicSeries() throws Exception {
    when(comicSeriesRepository.getComicSeriesDetail(any(Integer.class))).thenThrow(new Exception());
  }

  private void givenConnectionExceptionObtainingComicSeries() throws Exception {
    when(comicSeriesRepository.getComicSeriesDetail(any(Integer.class))).thenThrow(
        new MarvelApiException(ANY_EXCEPTION, new UnknownHostException()));
  }

  @NonNull private ComicSeries getComicSeries(int id) {
    ComicSeries comicSeries = new ComicSeries();
    comicSeries.setKey(id);
    comicSeries.setDescription("desc - " + id);
    comicSeries.setName("name - " + id);
    comicSeries.setComplete(true);
    comicSeries.setCoverUrl("https://id.annihil.us/u/prod/marvel/id/mg/c/60/55b6a28ef24fa.jpg");
    comicSeries.setRating("R+");
    comicSeries.setComics(givenComics());

    return comicSeries;
  }

  private List<Comic> givenComics() {
    List<Comic> comics = new ArrayList<>();

    Comic comic = new Comic();
    comic.setKey("0");
    comic.setName("comic - 0");
    comic.setThumbnailUrl("https://i.annihil.us/u/prod/marvel/i/mg/c/60/55b6a28ef24fa.jpg");

    comics.add(comic);

    return comics;
  }

  private List<ComicSeriesDetailViewModel> givenComicData(ComicSeries comicSeries) {
    ComicSeriesToComicSeriesDetailsViewModelMapper mapper =
        new ComicSeriesToComicSeriesDetailsViewModelMapper();
    ComicSeriesDetailsViewModel comicSeriesDetailsViewModel = mapper.map(comicSeries);
    return comicSeriesDetailsViewModel.getComicSeriesDetailViewModels();
  }

  private ComicSeriesDetailsActivity startActivity() {
    Intent intent = new Intent();
    intent.putExtra("ComicSeriesDetailsActivity.ComicSeriesKey", ANY_COMIC_SERIES_ID);

    return activityRule.launchActivity(intent);
  }

  @Override public List<Object> getTestModules() {
    return Arrays.asList((Object) new TestModule());
  }

  @Module(overrides = true, library = true, complete = false,
      injects = {
          ComicSeriesDetailsActivity.class, ComicSeriesDetailsActivityTest.class
      }) class TestModule {

    @Provides @Singleton public ComicSeriesRepository provideComicSeriesRepository() {
      return mock(ComicSeriesRepository.class);
    }
  }
}