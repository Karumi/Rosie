/*
 * Copyright (C) 2015 Karumi.
 */

package com.karumi.rosie.sample.comics.repository.datasource;

import android.support.annotation.NonNull;
import com.karumi.rosie.repository.PaginatedCollection;
import com.karumi.rosie.repository.datasource.EmptyReadableDataSource;
import com.karumi.rosie.repository.datasource.paginated.PaginatedReadableDataSource;
import com.karumi.rosie.sample.comics.domain.model.Comic;
import java.util.Collection;
import java.util.LinkedList;
import java.util.Random;
import javax.inject.Inject;

public class ComicsApiDataSource extends EmptyReadableDataSource<Integer, Comic>
    implements PaginatedReadableDataSource<Comic> {

  private static final int NUMBER_OF_COMICS = 15;
  private static final int GUARDIANS_OF_INFINITY_KEY = 58086;
  private static final int VISION_KEY = 57309;
  private static final int SPIDEY_KEY = 57137;
  private static final int RED_WOLF_KEY = 57075;
  private static final int NOVA_KEY = 57042;
  private static final long SLEEP_TIME_IN_MILLISECONDS = 750;
  private static final Random RANDOM = new Random(System.nanoTime());

  @Inject public ComicsApiDataSource() {
  }

  @Override public Comic getByKey(Integer key) {
    Comic comic;

    fakeDelay();

    switch (key) {
      case GUARDIANS_OF_INFINITY_KEY:
        comic = getGuardiansOfInfinity();
        break;
      case VISION_KEY:
        comic = getVision();
        break;
      case SPIDEY_KEY:
        comic = getSpidey();
        break;
      case RED_WOLF_KEY:
        comic = getRedWolf();
        break;
      case NOVA_KEY:
      default:
        comic = getNova();
        break;
    }

    return comic;
  }

  @Override public PaginatedCollection<Comic> getPage(int offset, int limit) {
    Collection<Comic> comics = new LinkedList<>();

    fakeDelay();

    for (int i = offset; i - offset < limit && i < NUMBER_OF_COMICS; i++) {
      comics.add(getComic(i));
    }

    PaginatedCollection<Comic> comicsPage = new PaginatedCollection<>(comics);
    comicsPage.setOffset(offset);
    comicsPage.setLimit(limit);
    comicsPage.setHasMore(offset < NUMBER_OF_COMICS);
    return comicsPage;
  }

  private Comic getComic(int i) {
    Comic[] comics = {getGuardiansOfInfinity(), getVision(), getSpidey(), getRedWolf(), getNova()};

    Comic comic = comics[RANDOM.nextInt(comics.length)];
    comic.setName(comic.getName() + " " + i);
    return comic;
  }

  @NonNull private Comic getGuardiansOfInfinity() {
    Comic guardiansOfInfinity = new Comic();
    guardiansOfInfinity.setKey(GUARDIANS_OF_INFINITY_KEY);
    guardiansOfInfinity.setName("Guardians of Infinity");
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
    return guardiansOfInfinity;
  }

  @NonNull private Comic getVision() {
    Comic vision = new Comic();
    vision.setKey(VISION_KEY);
    vision.setName("Vision");
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
    return vision;
  }

  @NonNull private Comic getSpidey() {
    Comic spidey = new Comic();
    spidey.setKey(SPIDEY_KEY);
    spidey.setName("Spidey");
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
    return spidey;
  }

  @NonNull private Comic getRedWolf() {
    Comic redWolf = new Comic();
    redWolf.setKey(RED_WOLF_KEY);
    redWolf.setName("Red Wolf");
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
    return redWolf;
  }

  @NonNull private Comic getNova() {
    Comic nova = new Comic();
    nova.setKey(NOVA_KEY);
    nova.setName("Nova");
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
