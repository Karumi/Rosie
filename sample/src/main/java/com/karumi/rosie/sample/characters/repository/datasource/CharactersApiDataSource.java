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

package com.karumi.rosie.sample.characters.repository.datasource;

import com.karumi.marvelapiclient.CharacterApiClient;
import com.karumi.marvelapiclient.MarvelApiException;
import com.karumi.marvelapiclient.model.CharacterDto;
import com.karumi.marvelapiclient.model.CharactersDto;
import com.karumi.marvelapiclient.model.MarvelResponse;
import com.karumi.rosie.repository.PaginatedCollection;
import com.karumi.rosie.repository.datasource.paginated.Page;
import com.karumi.rosie.sample.characters.domain.model.Character;
import com.karumi.rosie.sample.characters.repository.datasource.mapper.MapperCharacterToCharacterDto;
import java.util.Collection;
import java.util.Random;
import javax.inject.Inject;

public class CharactersApiDataSource extends CharacterDataSource {

  private static final int NUMBER_OF_CHARACTERS = 45;
  private static final long SLEEP_TIME_IN_MILLISECONDS = 1000;
  private static final Random RANDOM = new Random(System.nanoTime());
  private static final String SPIDERMAN_KEY = "54";
  private static final String CAPTAIN_MARVEL_KEY = "9";
  private static final String HULK_KEY = "25";
  private static final String THOR_KEY = "60";
  private static final String IRON_MAN_KEY = "29";
  private final CharacterApiClient characterApiClient;
  private final MapperCharacterToCharacterDto mapper = new MapperCharacterToCharacterDto();

  @Inject public CharactersApiDataSource(CharacterApiClient characterApiClient) {
    this.characterApiClient = characterApiClient;
  }

  @Override public Character getByKey(String key) throws Exception {
    MarvelResponse<CharacterDto> characterResponse = characterApiClient.getCharacter(key);
    CharacterDto characterDto = characterResponse.getResponse();

    return mapper.reverseMap(characterDto);
  }

  @Override public PaginatedCollection<Character> getPage(Page page)
      throws MarvelApiException {
    int offset = page.getOffset();
    int limit = page.getLimit();
    MarvelResponse<CharactersDto> charactersApiResponse = characterApiClient.getAll(offset, limit);

    CharactersDto charactersDto = charactersApiResponse.getResponse();
    Collection<Character> characters = mapper.reverseMap(charactersDto.getCharacters());

    PaginatedCollection<Character> charactersPage = new PaginatedCollection<>(characters);
    charactersPage.setPage(page);
    charactersPage.setHasMore(
        charactersDto.getOffset() + charactersDto.getCount() < charactersDto.getTotal());
    return charactersPage;
  }
}
