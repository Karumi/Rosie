/*
 * Copyright (C) 2015 Karumi.
 */

package com.karumi.rosie.sample.characters.domain.usecase;

import com.karumi.rosie.domain.usecase.RosieUseCase;
import com.karumi.rosie.domain.usecase.annotation.UseCase;
import com.karumi.rosie.repository.PaginatedCollection;
import com.karumi.rosie.sample.characters.domain.model.Character;
import com.karumi.rosie.sample.characters.repository.CharactersRepository;
import javax.inject.Inject;

public class GetCharacters extends RosieUseCase {

  private final CharactersRepository charactersRepository;

  @Inject public GetCharacters(CharactersRepository charactersRepository) {
    this.charactersRepository = charactersRepository;
  }

  @UseCase public void getCharacters(int offset, int limit) throws Exception {
    PaginatedCollection<Character> characters = charactersRepository.getPage(offset, limit);
    notifySuccess(characters);
  }
}
