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

package com.karumi.rosie.sample.characters.repository.datasource;

import android.support.annotation.NonNull;
import com.karumi.marvelapiclient.MarvelApiException;
import com.karumi.rosie.repository.PaginatedCollection;
import com.karumi.rosie.repository.datasource.EmptyPaginatedReadableDataSource;
import com.karumi.rosie.repository.datasource.paginated.Page;
import com.karumi.rosie.sample.characters.domain.model.Character;
import java.util.Collection;
import java.util.LinkedList;
import javax.inject.Inject;

public class CharactersFakeDataSource extends EmptyPaginatedReadableDataSource<String, Character> {

  private static final long SLEEP_TIME_IN_MILLISECONDS = 1000;
  private static final String SPIDERMAN_KEY = "54";
  private static final String CAPTAIN_MARVEL_KEY = "9";
  private static final String HULK_KEY = "25";
  private static final String THOR_KEY = "60";
  private static final String IRON_MAN_KEY = "29";
  private static final String SCARLET_KEY = "40";
  private static final String WOLVERINE_KEY = "70";
  private static final String STORM_KEY = "80";

  private Character[] characters;

  @Inject public CharactersFakeDataSource() {
    initCharacters();
  }

  @Override public Character getByKey(String key) throws Exception {
    Character character;
    fakeDelay();

    switch (key) {
      case SPIDERMAN_KEY:
        character = getSpiderman();
        break;
      case CAPTAIN_MARVEL_KEY:
        character = getCaptainMarvel();
        break;
      case HULK_KEY:
        character = getHulk();
        break;
      case THOR_KEY:
        character = getThor();
        break;
      case SCARLET_KEY:
        character = getScarletWitch();
        break;
      case WOLVERINE_KEY:
        character = getWolverine();
        break;
      case STORM_KEY:
        character = getStorm();
        break;
      case IRON_MAN_KEY:
      default:
        character = getIronMan();
        break;
    }

    return character;
  }

  @Override public PaginatedCollection<Character> getPage(Page page)
      throws MarvelApiException {
    int offset = page.getOffset();
    int limit = page.getLimit();

    Collection<Character> charactersToReturn = new LinkedList<>();
    fakeDelay();

    if (offset < characters.length) {
      for (int i = offset; i - offset < limit && i < characters.length; i++) {
        charactersToReturn.add(characters[i]);
      }
    }

    PaginatedCollection<Character> charactersPage = new PaginatedCollection<>(charactersToReturn);
    charactersPage.setPage(page);
    charactersPage.setHasMore(offset + charactersToReturn.size() < characters.length);
    return charactersPage;
  }

  private void initCharacters() {
    characters =
        new Character[] {getSpiderman(),
            getCaptainMarvel(),
            getHulk(),
            getThor(),
            getIronMan(),
            getScarletWitch(),
            getWolverine(),
            getStorm()
        };
  }

  @NonNull private Character getSpiderman() {
    Character spiderman = new Character();
    spiderman.setKey(SPIDERMAN_KEY);
    spiderman.setName("Spiderman");
    spiderman.setThumbnailUrl("http://x.annihil.us/u/prod/marvel/i/mg/6/60/538cd3628a05e.jpg");
    spiderman.setDescription(
        "Bitten by a radioactive spider, high school student Peter Parker gained the speed, "
            + "strength and powers of a spider. Adopting the name Spider-Man, Peter hoped to start"
            + " a career using his new abilities. Taught that with great power comes great"
            + " responsibility, Spidey has vowed to use his powers to help people.");
    return spiderman;
  }

  @NonNull private Character getCaptainMarvel() {
    Character captainMarvel = new Character();
    captainMarvel.setKey(CAPTAIN_MARVEL_KEY);
    captainMarvel.setName("Captain Marvel");
    captainMarvel.setThumbnailUrl("http://x.annihil.us/u/prod/marvel/i/mg/6/30/537ba61b764b4.jpg");
    captainMarvel.setDescription(
        "Carol Danvers entered the Air Force upon graduating from high school to pursue her love"
            + " of aircrafts and her dreams of flying.");
    return captainMarvel;
  }

  @NonNull private Character getHulk() {
    Character hulk = new Character();
    hulk.setKey(HULK_KEY);
    hulk.setName("Hulk");
    hulk.setThumbnailUrl("http://x.annihil.us/u/prod/marvel/i/mg/e/e0/537bafa34baa9.jpg");
    hulk.setDescription(
        "Caught in a gamma bomb explosion while trying to save the life of a teenager, Dr. Bruce"
            + " Banner was transformed into the incredibly powerful creature called the Hulk. An"
            + " all too often misunderstood hero, the angrier the Hulk gets, the stronger the Hulk"
            + " gets.");
    return hulk;
  }

  @NonNull private Character getThor() {
    Character hulk = new Character();
    hulk.setKey(THOR_KEY);
    hulk.setName("Thor");
    hulk.setThumbnailUrl("http://x.annihil.us/u/prod/marvel/i/mg/7/10/537bc71e9286f.jpg");
    hulk.setDescription(
        "As the Norse God of thunder and lightning, Thor wields one of the greatest weapons ever"
            + " made, the enchanted hammer Mjolnir. While others have described Thor as an"
            + " over-muscled, oafish imbecile, he's quite smart and compassionate."
            + " He's self-assured, and he would never, ever stop fighting for a worthwhile cause.");
    return hulk;
  }

  @NonNull private Character getScarletWitch() {
    Character scarlet = new Character();
    scarlet.setKey(SCARLET_KEY);
    scarlet.setName("Scarlet Witch");
    scarlet.setThumbnailUrl("https://i.annihil.us/u/prod/marvel/i/mg/9/b0/537bc2375dfb9.jpg");
    scarlet.setDescription(
        "Born at the Wundagore base of the High Evolutionary, in proximity to the eldritch energies"
            + " of the Elder God Chthon; Wanda and her brother Pietro's mother, Magda, fled "
            + "Wundagore shortly after their birth. Although not known at the time, Chthon formed "
            + "a mystic bond with Wanda, who, it was later learned, was destined to serve the role "
            + "of Nexus Being, a living focal point for the Earth dimension's mystical energy.");
    return scarlet;
  }

  @NonNull private Character getWolverine() {
    Character wolverine = new Character();
    wolverine.setKey(WOLVERINE_KEY);
    wolverine.setName("Wolverine");
    wolverine.setThumbnailUrl("https://i.annihil.us/u/prod/marvel/i/mg/9/00/537bcb1133fd7.jpg");
    wolverine.setDescription(
        "Born the second son of wealthy landowners John and Elizabeth Howlett in Alberta, Canada "
            + "during the late 19th Century, James Howlett was a frail boy of poor health. James "
            + "was largely neglected by his mother, who was institutionalized for a time "
            + "following the death of her first son, John Jr., in 1897. He spent most of "
            + "his early years on the estate grounds and had two playmates that lived on the "
            + "Howlett estate with him: Rose, a red-headed girl who was brought in from town "
            + "to be a companion to young James, and a boy nicknamed \"Dog\" who was the son of "
            + "the groundskeeper, Thomas Logan. Thomas Logan was an alcoholic and was extremely "
            + "abusive to his son. The children were close friends but as they reached young "
            + "adulthood, the abuse inflicted upon Dog warped his mind. His actions would lead to"
            + " a tragic chain of events. that started as the three neared their adolescent years "
            + "when Dog made unwanted advances toward Rose and James reported it to his father."
            + " In retaliation Dog killed James's pet dog. This in turn resulted in the expulsion "
            + "of Thomas Logan and Dog Logan from the estate.");
    return wolverine;
  }

  @NonNull private Character getStorm() {
    Character storm = new Character();
    storm.setKey(STORM_KEY);
    storm.setName("Storm");
    storm.setThumbnailUrl("https://x.annihil.us/u/prod/marvel/i/mg/c/b0/537bc5f8a8df0.jpg");
    storm.setDescription(
        "Ororo Monroe is the descendant of an ancient line of African priestesses, all of whom "
            + "have white hair, blue eyes, and the potential to wield magic. Her mother, N'dare, "
            + "was an African princess who married American photojournalist David Monroe and moved "
            + "with him to Manhattan, where Ororo was born. When Ororo was six months old, she and "
            + "her parents moved to Cairo, Egypt. Five years later, during the Arab-Israeli "
            + "conflict, a plane crashed into their home. Ororo's parents were killed, but she "
            + "survived, buried under rubble near her mother's body. The resultant trauma left "
            + "Ororo with severe claustrophobia that still affects her today.");
    return storm;
  }

  @NonNull private Character getIronMan() {
    Character hulk = new Character();
    hulk.setKey(IRON_MAN_KEY);
    hulk.setName("Iron Man");
    hulk.setThumbnailUrl("http://i.annihil.us/u/prod/marvel/i/mg/c/60/55b6a28ef24fa.jpg");
    hulk.setDescription(
        "Wounded, captured and forced to build a weapon by his enemies, billionaire industrialist"
            + " Tony Stark instead created an advanced suit of armor to save his life and escape"
            + " captivity. Now with a new outlook on life, Tony uses his money and intelligence"
            + " to make the world a safer, better place as Iron Man.");
    return hulk;
  }

  private void fakeDelay() {
    try {
      Thread.sleep(SLEEP_TIME_IN_MILLISECONDS);
    } catch (InterruptedException e) {
    }
  }
}
