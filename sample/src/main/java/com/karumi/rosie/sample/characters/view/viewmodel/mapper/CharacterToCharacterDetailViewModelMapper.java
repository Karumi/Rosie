/*
 * Copyright (C) 2015 Karumi.
 */

package com.karumi.rosie.sample.characters.view.viewmodel.mapper;

import com.karumi.rosie.sample.characters.domain.model.Character;
import com.karumi.rosie.sample.characters.view.viewmodel.CharacterDetailViewModel;
import javax.inject.Inject;

public class CharacterToCharacterDetailViewModelMapper {

  @Inject public CharacterToCharacterDetailViewModelMapper() {
  }

  public CharacterDetailViewModel mapCharacterToCharacterDetailViewModel(Character character) {
    CharacterDetailViewModel characterViewModel = new CharacterDetailViewModel();

    characterViewModel.setKey(character.getKey());
    characterViewModel.setName(character.getName());
    characterViewModel.setHeaderImage(character.getThumbnailUrl());
    characterViewModel.setDescription(character.getDescription());

    return characterViewModel;
  }
}
