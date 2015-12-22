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

package com.karumi.rosie.sample.characters.repository.datasource.mapper;

import com.karumi.marvelapiclient.model.CharacterDto;
import com.karumi.marvelapiclient.model.MarvelImage;
import com.karumi.rosie.mapper.Mapper;
import com.karumi.rosie.sample.characters.domain.model.Character;

public class CharacterToCharacterDtoMapper extends Mapper<Character, CharacterDto> {

  @Override public CharacterDto map(Character value) {
    throw new UnsupportedOperationException();
  }

  @Override public Character reverseMap(CharacterDto value) {
    Character character = new Character();
    character.setKey(value.getId());
    character.setName(value.getName());
    character.setDescription(value.getDescription());
    character.setThumbnailUrl(value.getThumbnail().getImageUrl(MarvelImage.Size.PORTRAIT_UNCANNY));
    return character;
  }
}
