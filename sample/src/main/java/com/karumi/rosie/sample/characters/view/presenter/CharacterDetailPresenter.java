/*
 * Copyright (C) 2015 Karumi.
 */

package com.karumi.rosie.sample.characters.view.presenter;

import com.karumi.rosie.domain.usecase.UseCaseHandler;
import com.karumi.rosie.domain.usecase.UseCaseParams;
import com.karumi.rosie.domain.usecase.annotation.Success;
import com.karumi.rosie.domain.usecase.callback.OnSuccessCallback;
import com.karumi.rosie.sample.characters.domain.model.Character;
import com.karumi.rosie.sample.characters.domain.usecase.GetCharacterDetails;
import com.karumi.rosie.sample.characters.view.viewmodel.CharacterDetailViewModel;
import com.karumi.rosie.sample.characters.view.viewmodel.mapper.CharacterToCharacterDetailViewModelMapper;
import com.karumi.rosie.view.RosiePresenter;
import javax.inject.Inject;

public class CharacterDetailPresenter extends RosiePresenter<CharacterDetailPresenter.View> {

  private final GetCharacterDetails getCharacterDetails;
  private final CharacterToCharacterDetailViewModelMapper mapper;
  private String characterKey;

  @Inject public CharacterDetailPresenter(UseCaseHandler useCaseHandler,
      CharacterToCharacterDetailViewModelMapper mapper, GetCharacterDetails getCharacterDetails) {
    super(useCaseHandler);
    this.mapper = mapper;
    this.getCharacterDetails = getCharacterDetails;
  }

  @Override protected void update() {
    super.update();
    getView().showLoading();
    loadCharacterDetails();
  }

  public void setCharacterKey(String characterKey) {
    this.characterKey = characterKey;
  }

  private void loadCharacterDetails() {
    getView().hideCharacterDetail();
    UseCaseParams params =
        new UseCaseParams.Builder().args(characterKey).onSuccess(new OnSuccessCallback() {
          @Success public void onCharacterDetailsLoaded(Character character) {
            getView().hideLoading();
            CharacterDetailViewModel characterDetailViewModel =
                mapper.mapCharacterToCharacterDetailViewModel(character);
            getView().showCharacterDetail(characterDetailViewModel);
          }
        }).build();
    execute(getCharacterDetails, params);
  }

  public interface View extends RosiePresenter.View {
    void hideLoading();

    void showLoading();

    void hideCharacterDetail();

    void showCharacterDetail(CharacterDetailViewModel character);
  }
}
