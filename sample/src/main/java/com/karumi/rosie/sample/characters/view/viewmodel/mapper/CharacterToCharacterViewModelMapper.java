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

package com.karumi.rosie.sample.characters.view.viewmodel.mapper;

import com.karumi.rosie.repository.PaginatedCollection;
import com.karumi.rosie.sample.characters.domain.model.Character;
import com.karumi.rosie.sample.characters.view.viewmodel.CharacterViewModel;
import java.util.LinkedList;
import java.util.List;
import javax.inject.Inject;

public class CharacterToCharacterViewModelMapper {

  @Inject public CharacterToCharacterViewModelMapper() {
  }

  public List<CharacterViewModel> mapCharactersToCharacterViewModels(
      PaginatedCollection<Character> characters) {
    List<CharacterViewModel> characterViewModels = new LinkedList<>();

    for (Character character : characters.getItems()) {
      CharacterViewModel characterViewModel = new CharacterViewModel();
      characterViewModel.setKey(character.getKey());
      characterViewModel.setName(character.getName());
      characterViewModel.setThumbnailUrl(character.getThumbnailUrl());
      characterViewModels.add(characterViewModel);
    }

    return characterViewModels;
  }
}
