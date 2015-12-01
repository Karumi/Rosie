/*
 * Copyright (C) 2015 Karumi.
 */

package com.karumi.rosie.sample.characters.view.presenter;

import com.karumi.rosie.domain.usecase.UseCaseHandler;
import com.karumi.rosie.domain.usecase.UseCaseParams;
import com.karumi.rosie.domain.usecase.annotation.Success;
import com.karumi.rosie.domain.usecase.callback.OnSuccessCallback;
import com.karumi.rosie.repository.PaginatedCollection;
import com.karumi.rosie.sample.characters.domain.model.Character;
import com.karumi.rosie.sample.characters.domain.usecase.GetCharacters;
import com.karumi.rosie.sample.characters.view.viewmodel.CharacterViewModel;
import com.karumi.rosie.sample.characters.view.viewmodel.mapper.CharacterToCharacterViewModelMapper;
import com.karumi.rosie.view.loading.RosiePresenterWithLoading;
import java.util.List;
import javax.inject.Inject;

public class CharactersPresenter extends RosiePresenterWithLoading<CharactersPresenter.View> {

  private static final int NUMBER_OF_CHARACTERS_PER_PAGE = 3;

  private final GetCharacters getCharacters;
  private final CharacterToCharacterViewModelMapper mapper;
  private int offset = 0;

  @Inject public CharactersPresenter(UseCaseHandler useCaseHandler, GetCharacters getCharacters,
      CharacterToCharacterViewModelMapper mapper) {
    super(useCaseHandler);
    this.getCharacters = getCharacters;
    this.mapper = mapper;
  }

  @Override protected void update() {
    super.update();
    getView().hideCharacters();
    showLoading();
    loadCharacters();
  }

  public void onCharacterClicked(CharacterViewModel character) {
    String characterKey = character.getKey();
    getView().openCharacterDetails(characterKey);
  }

  public void onLoadMore() {
    loadCharacters();
  }

  private void loadCharacters() {
    UseCaseParams params = new UseCaseParams.Builder().args(offset, NUMBER_OF_CHARACTERS_PER_PAGE)
        .onSuccess(new OnSuccessCallback() {
          @Success public void onCharactersLoaded(PaginatedCollection<Character> characters) {
            List<CharacterViewModel> characterViewModels =
                mapper.mapCharactersToCharacterViewModels(characters);
            getView().showCharacters(characterViewModels);
            getView().showHasMore(characters.hasMore());
            hideLoading();
            offset = characters.getOffset() + NUMBER_OF_CHARACTERS_PER_PAGE;
          }
        })
        .build();
    execute(getCharacters, params);
  }

  public interface View extends RosiePresenterWithLoading.View {
    void hideCharacters();

    void showCharacters(List<CharacterViewModel> characters);

    void showHasMore(boolean hasMore);

    void openCharacterDetails(String characterKey);
  }
}
