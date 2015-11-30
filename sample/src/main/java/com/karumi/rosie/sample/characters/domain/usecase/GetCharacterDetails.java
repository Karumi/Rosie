/*
 * Copyright (C) 2015 Karumi.
 */

package com.karumi.rosie.sample.characters.domain.usecase;

import com.karumi.rosie.domain.usecase.RosieUseCase;
import com.karumi.rosie.domain.usecase.annotation.UseCase;
import com.karumi.rosie.sample.characters.domain.model.Character;
import com.karumi.rosie.sample.characters.repository.CharactersRepository;
import javax.inject.Inject;

public class GetCharacterDetails extends RosieUseCase {

  private final CharactersRepository charactersRepository;

  @Inject public GetCharacterDetails(CharactersRepository charactersRepository) {
    this.charactersRepository = charactersRepository;
  }

  @UseCase public void getCharacterDetails(String characterKey) {
    Character character = charactersRepository.getByKey(characterKey);
    notifySuccess(character);
  }
}
