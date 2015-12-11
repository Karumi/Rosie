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

package com.karumi.rosie.sample.comics.repository.datasource;

import android.support.annotation.NonNull;
import com.karumi.rosie.repository.PaginatedCollection;
import com.karumi.rosie.repository.datasource.EmptyReadableDataSource;
import com.karumi.rosie.repository.datasource.paginated.PaginatedReadableDataSource;
import com.karumi.rosie.sample.comics.domain.model.ComicSeries;
import java.util.Collection;
import java.util.LinkedList;
import java.util.Random;
import javax.inject.Inject;

public class ComicSeriesApiDataSource extends EmptyReadableDataSource<Integer, ComicSeries>
    implements PaginatedReadableDataSource<ComicSeries> {

  private static final int NUMBER_OF_COMIC_SERIES = 80;
  private static final int GUARDIANS_OF_INFINITY_KEY = 58086;
  private static final int VISION_KEY = 57309;
  private static final int SPIDEY_KEY = 57137;
  private static final int RED_WOLF_KEY = 57075;
  private static final int NOVA_KEY = 57042;
  private static final long SLEEP_TIME_IN_MILLISECONDS = 750;
  private static final Random RANDOM = new Random(System.nanoTime());

  @Inject public ComicSeriesApiDataSource() {
  }

  @Override public ComicSeries getByKey(Integer key) {
    ComicSeries comicSeries;

    fakeDelay();

    switch (key) {
      case GUARDIANS_OF_INFINITY_KEY:
        comicSeries = getGuardiansOfInfinity();
        break;
      case VISION_KEY:
        comicSeries = getVision();
        break;
      case SPIDEY_KEY:
        comicSeries = getSpidey();
        break;
      case RED_WOLF_KEY:
        comicSeries = getRedWolf();
        break;
      case NOVA_KEY:
      default:
        comicSeries = getNova();
        break;
    }

    return comicSeries;
  }

  @Override public PaginatedCollection<ComicSeries> getPage(int offset, int limit) {
    Collection<ComicSeries> comicSeries = new LinkedList<>();

    fakeDelay();

    for (int i = offset; i - offset < limit && i < NUMBER_OF_COMIC_SERIES; i++) {
      comicSeries.add(getComicSeries(i));
    }

    PaginatedCollection<ComicSeries> comicSeriesPage = new PaginatedCollection<>(comicSeries);
    comicSeriesPage.setOffset(offset);
    comicSeriesPage.setLimit(limit);
    comicSeriesPage.setHasMore(offset + comicSeries.size() < NUMBER_OF_COMIC_SERIES);
    return comicSeriesPage;
  }

  private ComicSeries getComicSeries(int i) {
    ComicSeries[] allComicSeries =
        {getGuardiansOfInfinity(), getVision(), getSpidey(), getRedWolf(), getNova()};

    ComicSeries comicSeries = allComicSeries[RANDOM.nextInt(allComicSeries.length)];
    comicSeries.setName(comicSeries.getName() + " " + i);
    return comicSeries;
  }

  @NonNull private ComicSeries getGuardiansOfInfinity() {
    ComicSeries guardiansOfInfinity = new ComicSeries();
    guardiansOfInfinity.setKey(GUARDIANS_OF_INFINITY_KEY);
    guardiansOfInfinity.setName("Guardians of Infinity");
    guardiansOfInfinity.setNumber(2);
    guardiansOfInfinity.setCoverUrl(
        "http://i.annihil.us/u/prod/marvel/i/mg/e/70/5655d14851998/detail.jpg");
    guardiansOfInfinity.setDescription(
        "WHO ARE THE GUARDIANS 1000?! Defending the galaxy is a good gig, but Drax isn’t thrilled"
            + " with the combat hours (not enough of them) and Rocket isn’t thrilled with the"
            + " compensation (not enough of it). They’re off on an adventure to supplement both,"
            + " and Groot is happy to come along. But instead of the perks they want, they’re about"
            + " to get more trouble than they can handle. Good thing the Guardians 3000 are on hand"
            + " to help! But what about…THE GUARDIANS 1000??!! Also in this issue, Latour and"
            + " Cheung tell a one-off story of Ben Grimm and Rocket Raccoon, stuck on a planet"
            + " where culture is based on Earth-style professional wrestling…but the stakes are"
            + " life and death! THIS AIN’T KAYFABE, BROTHER! ");
    guardiansOfInfinity.setReleaseYear(2015);
    guardiansOfInfinity.setRating(ComicSeries.Rating.ALL_AGES);
    return guardiansOfInfinity;
  }

  @NonNull private ComicSeries getVision() {
    ComicSeries vision = new ComicSeries();
    vision.setKey(VISION_KEY);
    vision.setName("Vision");
    vision.setNumber(1);
    vision.setCoverUrl("http://i.annihil.us/u/prod/marvel/i/mg/4/10/5655d4822c8d8/detail.jpg");
    vision.setDescription(
        "WHO ARE THE GUARDIANS 1000?! Defending the galaxy is a good gig, but Drax isn’t thrilled"
            + " with the combat hours (not enough of them) and Rocket isn’t thrilled with the"
            + " compensation (not enough of it). They’re off on an adventure to supplement both,"
            + " and Groot is happy to come along. But instead of the perks they want, they’re about"
            + " to get more trouble than they can handle. Good thing the Guardians 3000 are on hand"
            + " to help! But what about…THE GUARDIANS 1000??!! Also in this issue, Latour and"
            + " Cheung tell a one-off story of Ben Grimm and Rocket Raccoon, stuck on a planet"
            + " where culture is based on Earth-style professional wrestling…but the stakes are"
            + " life and death! THIS AIN’T KAYFABE, BROTHER! ");
    vision.setReleaseYear(2015);
    vision.setRating(ComicSeries.Rating.T);
    return vision;
  }

  @NonNull private ComicSeries getSpidey() {
    ComicSeries spidey = new ComicSeries();
    spidey.setKey(SPIDEY_KEY);
    spidey.setName("Spidey");
    spidey.setNumber(1);
    spidey.setCoverUrl("http://i.annihil.us/u/prod/marvel/i/mg/6/50/5655d3a0e63d5/detail.jpg");
    spidey.setDescription(
        "WHO ARE THE GUARDIANS 1000?! Defending the galaxy is a good gig, but Drax isn’t thrilled"
            + " with the combat hours (not enough of them) and Rocket isn’t thrilled with the"
            + " compensation (not enough of it). They’re off on an adventure to supplement both,"
            + " and Groot is happy to come along. But instead of the perks they want, they’re about"
            + " to get more trouble than they can handle. Good thing the Guardians 3000 are on hand"
            + " to help! But what about…THE GUARDIANS 1000??!! Also in this issue, Latour and"
            + " Cheung tell a one-off story of Ben Grimm and Rocket Raccoon, stuck on a planet"
            + " where culture is based on Earth-style professional wrestling…but the stakes are"
            + " life and death! THIS AIN’T KAYFABE, BROTHER! ");
    spidey.setReleaseYear(2015);
    spidey.setRating(ComicSeries.Rating.TEENS_AND_UP);
    return spidey;
  }

  @NonNull private ComicSeries getRedWolf() {
    ComicSeries redWolf = new ComicSeries();
    redWolf.setKey(RED_WOLF_KEY);
    redWolf.setName("Red Wolf");
    redWolf.setNumber(2);
    redWolf.setCoverUrl("http://x.annihil.us/u/prod/marvel/i/mg/4/10/5655d353959ad/detail.jpg");
    redWolf.setDescription(
        "WHO ARE THE GUARDIANS 1000?! Defending the galaxy is a good gig, but Drax isn’t thrilled"
            + " with the combat hours (not enough of them) and Rocket isn’t thrilled with the"
            + " compensation (not enough of it). They’re off on an adventure to supplement both,"
            + " and Groot is happy to come along. But instead of the perks they want, they’re about"
            + " to get more trouble than they can handle. Good thing the Guardians 3000 are on hand"
            + " to help! But what about…THE GUARDIANS 1000??!! Also in this issue, Latour and"
            + " Cheung tell a one-off story of Ben Grimm and Rocket Raccoon, stuck on a planet"
            + " where culture is based on Earth-style professional wrestling…but the stakes are"
            + " life and death! THIS AIN’T KAYFABE, BROTHER! ");
    redWolf.setReleaseYear(2015);
    redWolf.setRating(ComicSeries.Rating.EXPLICIT_CONTENT);
    return redWolf;
  }

  @NonNull private ComicSeries getNova() {
    ComicSeries nova = new ComicSeries();
    nova.setKey(NOVA_KEY);
    nova.setName("Nova");
    nova.setNumber(1);
    nova.setCoverUrl("http://i.annihil.us/u/prod/marvel/i/mg/6/10/5655d2a8ba225/detail.jpg");
    nova.setDescription(
        "WHO ARE THE GUARDIANS 1000?! Defending the galaxy is a good gig, but Drax isn’t thrilled"
            + " with the combat hours (not enough of them) and Rocket isn’t thrilled with the"
            + " compensation (not enough of it). They’re off on an adventure to supplement both,"
            + " and Groot is happy to come along. But instead of the perks they want, they’re about"
            + " to get more trouble than they can handle. Good thing the Guardians 3000 are on hand"
            + " to help! But what about…THE GUARDIANS 1000??!! Also in this issue, Latour and"
            + " Cheung tell a one-off story of Ben Grimm and Rocket Raccoon, stuck on a planet"
            + " where culture is based on Earth-style professional wrestling…but the stakes are"
            + " life and death! THIS AIN’T KAYFABE, BROTHER! ");
    nova.setReleaseYear(2015);
    nova.setRating(ComicSeries.Rating.PARENTAL_ADVISORY);
    return nova;
  }

  private void fakeDelay() {
    try {
      Thread.sleep(SLEEP_TIME_IN_MILLISECONDS);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
  }
}
