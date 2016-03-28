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

package com.karumi.rosie.sample.characters.view.activity;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.test.espresso.intent.rule.IntentsTestRule;
import android.support.test.espresso.matcher.ViewMatchers;
import com.karumi.marvelapiclient.MarvelApiException;
import com.karumi.rosie.sample.InjectedInstrumentationTest;
import com.karumi.rosie.sample.R;
import com.karumi.rosie.sample.characters.domain.model.Character;
import com.karumi.rosie.sample.characters.repository.CharactersRepository;
import dagger.Module;
import dagger.Provides;
import java.net.UnknownHostException;
import java.util.Arrays;
import java.util.List;
import javax.inject.Inject;
import javax.inject.Singleton;
import org.junit.Rule;
import org.junit.Test;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withEffectiveVisibility;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.not;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class CharacterDetailsActivityTest extends InjectedInstrumentationTest {

  private static final int ANY_CHARACTER_ID = 1;
  private static final String ANY_EXCEPTION = "AnyException";
  @Rule public IntentsTestRule<CharacterDetailsActivity> activityRule =
      new IntentsTestRule<>(CharacterDetailsActivity.class, true, false);

  @Inject CharactersRepository charactersRepository;

  @Test public void shouldShowCharacterDetailWhenCharacterIsLoaded() throws Exception {
    Character character = givenAValidCharacter();

    startActivity();

    onView(withId(R.id.tv_character_name)).check(matches(withText(character.getName())));
    onView(withId(R.id.tv_description)).check(matches(withText(character.getDescription())));
  }

  @Test public void shouldHideLoadingWhenCharacterIsLoaded() throws Exception {
    givenAValidCharacter();

    startActivity();

    onView((withId(R.id.loading))).check(matches(not(isDisplayed())));
  }

  @Test public void shouldShowsErrorIfSomethingWrongHappend() throws Exception {
    givenExceptionObtainingACharacter();

    startActivity();

    onView(allOf(withId(android.support.design.R.id.snackbar_text), withText("¯\\_(ツ)_/¯"))).check(
        matches(isDisplayed()));
  }

  @Test public void shouldShowsConnectionErrorIfHasConnectionTroubles() throws Exception {
    givenConnectionExceptionObtainingACharacter();

    startActivity();

    onView(allOf(withId(android.support.design.R.id.snackbar_text),
        withText("Connection troubles. Ask to Ironman!"))).check(
        matches(withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE)));
  }

  private Character givenAValidCharacter() throws Exception {
    Character character = getCharacter(ANY_CHARACTER_ID);
    when(charactersRepository.getByKey(anyString())).thenReturn(character);
    return character;
  }

  private void givenExceptionObtainingACharacter() throws Exception {
    when(charactersRepository.getByKey(anyString())).thenThrow(new Exception());
  }

  private void givenConnectionExceptionObtainingACharacter() throws Exception {
    when(charactersRepository.getByKey(anyString())).thenThrow(
        new MarvelApiException(ANY_EXCEPTION, new UnknownHostException()));
  }

  @NonNull private Character getCharacter(int id) {
    Character character = new Character();
    character.setKey("" + id);
    character.setName("SuperHero - " + id);
    character.setDescription("Description Super Hero - " + id);
    character.setThumbnailUrl("https://id.annihil.us/u/prod/marvel/id/mg/c/60/55b6a28ef24fa.jpg");
    return character;
  }

  private CharacterDetailsActivity startActivity() {
    Intent intent = new Intent();
    intent.putExtra("CharacterDetailsActivity.CharacterKey", ANY_CHARACTER_ID);

    return activityRule.launchActivity(intent);
  }

  @Override public List<Object> getTestModules() {
    return Arrays.asList((Object) new TestModule());
  }

  @Module(overrides = true, library = true, complete = false,
      injects = {
          CharacterDetailsActivity.class, CharacterDetailsActivityTest.class
      }) class TestModule {

    @Provides @Singleton public CharactersRepository provideCharactersRepository() {
      return mock(CharactersRepository.class);
    }
  }
}