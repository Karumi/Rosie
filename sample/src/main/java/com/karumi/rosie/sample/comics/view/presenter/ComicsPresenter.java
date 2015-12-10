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

package com.karumi.rosie.sample.comics.view.presenter;

import com.karumi.rosie.domain.usecase.UseCaseHandler;
import com.karumi.rosie.domain.usecase.annotation.Success;
import com.karumi.rosie.domain.usecase.callback.OnSuccessCallback;
import com.karumi.rosie.repository.PaginatedCollection;
import com.karumi.rosie.sample.comics.domain.model.Comic;
import com.karumi.rosie.sample.comics.domain.usecase.GetComics;
import com.karumi.rosie.sample.comics.view.viewmodel.ComicViewModel;
import com.karumi.rosie.sample.comics.view.viewmodel.mapper.ComicToComicViewModelMapper;
import com.karumi.rosie.view.loading.RosiePresenterWithLoading;
import java.util.List;
import javax.inject.Inject;

public class ComicsPresenter extends RosiePresenterWithLoading<ComicsPresenter.View> {

  private static final int NUMBER_OF_COMICS_PER_PAGE = 14;
  private final ComicToComicViewModelMapper mapper;
  private final GetComics getComics;
  private int offset = 0;

  @Inject public ComicsPresenter(UseCaseHandler useCaseHandler, ComicToComicViewModelMapper mapper,
      GetComics getComics) {
    super(useCaseHandler);
    this.mapper = mapper;
    this.getComics = getComics;
  }

  @Override protected void update() {
    super.update();
    getView().hideComics();
    showLoading();
    loadComics();
  }

  public void onLoadMore() {
    loadComics();
  }

  public void onComicClicked(ComicViewModel comic) {
    getView().openComicDetails(comic.getKey());
  }

  private void loadComics() {
    createUseCaseCall(getComics).args(offset, NUMBER_OF_COMICS_PER_PAGE)
        .onSuccess(new OnSuccessCallback() {
          @Success public void onComicsLoaded(PaginatedCollection<Comic> comics) {
            List<ComicViewModel> comicViewModels = mapper.mapComicsToComicViewModels(comics);
            hideLoading();
            getView().showHasMore(comics.hasMore());
            getView().showComics(comicViewModels);
            offset = comics.getOffset() + NUMBER_OF_COMICS_PER_PAGE;
          }
        })
        .execute();
  }

  public interface View extends RosiePresenterWithLoading.View {
    void hideComics();

    void showComics(List<ComicViewModel> comics);

    void showHasMore(boolean hasMore);

    void openComicDetails(int comicKey);
  }
}
