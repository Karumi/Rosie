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

package com.karumi.rosie.sample.characters.view.presenter;

import com.karumi.rosie.domain.usecase.UseCaseHandler;
import com.karumi.rosie.domain.usecase.annotation.Success;
import com.karumi.rosie.domain.usecase.callback.OnSuccessCallback;
import com.karumi.rosie.domain.usecase.error.OnErrorCallback;
import com.karumi.rosie.sample.base.view.presenter.MarvelPresenter;
import com.karumi.rosie.sample.characters.domain.model.Character;
import com.karumi.rosie.sample.characters.domain.usecase.GetCharacterDetails;
import com.karumi.rosie.sample.characters.view.viewmodel.CharacterDetailViewModel;
import com.karumi.rosie.sample.characters.view.viewmodel.mapper.CharacterToCharacterDetailViewModelMapper;
import javax.inject.Inject;

public class CharacterDetailsPresenter
    extends MarvelPresenter<CharacterDetailsPresenter.View> {

  private final GetCharacterDetails getCharacterDetails;
  private final CharacterToCharacterDetailViewModelMapper mapper;
  private String characterKey;

  @Inject public CharacterDetailsPresenter(UseCaseHandler useCaseHandler,
      CharacterToCharacterDetailViewModelMapper mapper, GetCharacterDetails getCharacterDetails) {
    super(useCaseHandler);
    this.mapper = mapper;
    this.getCharacterDetails = getCharacterDetails;
  }

  @Override public void update() {
    super.update();
    showLoading();
    loadCharacterDetails();
  }

  public void setCharacterKey(String characterKey) {
    this.characterKey = characterKey;
  }

  private void loadCharacterDetails() {
    getView().hideCharacterDetail();
    createUseCaseCall(getCharacterDetails).args(characterKey).onSuccess(new OnSuccessCallback() {
      @Success public void onCharacterDetailsLoaded(Character character) {
        hideLoading();
        CharacterDetailViewModel characterDetailViewModel =
            mapper.mapCharacterToCharacterDetailViewModel(character);
        getView().showCharacterDetail(characterDetailViewModel);
      }
    })
        .onError(new OnErrorCallback() {
          @Override public boolean onError(Error error) {
            getView().hideLoading();
            return false;
          }
        })
        .execute();
  }

  public interface View extends MarvelPresenter.View {
    void hideCharacterDetail();

    void showCharacterDetail(CharacterDetailViewModel character);
  }
}
