/*
 * Copyright (C) 2015 Karumi.
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
      characterViewModel.setName(character.getName());
      characterViewModel.setThumbnailUrl(character.getThumbnailUrl());
      characterViewModels.add(characterViewModel);
    }

    return characterViewModels;
  }
}
