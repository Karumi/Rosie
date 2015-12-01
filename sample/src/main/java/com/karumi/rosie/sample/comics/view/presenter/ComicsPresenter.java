/*
 * Copyright (C) 2015 Karumi.
 */

package com.karumi.rosie.sample.comics.view.presenter;

import com.karumi.rosie.domain.usecase.UseCaseHandler;
import com.karumi.rosie.domain.usecase.UseCaseParams;
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
    UseCaseParams params = new UseCaseParams.Builder().args(offset, NUMBER_OF_COMICS_PER_PAGE)
        .onSuccess(new OnSuccessCallback() {
          @Success public void onComicsLoaded(PaginatedCollection<Comic> comics) {
            List<ComicViewModel> comicViewModels = mapper.mapComicsToComicViewModels(comics);
            hideLoading();
            getView().showHasMore(comics.hasMore());
            getView().showComics(comicViewModels);
            offset = comics.getOffset() + NUMBER_OF_COMICS_PER_PAGE;
          }
        })
        .build();
    execute(getComics, params);
  }

  public interface View extends RosiePresenterWithLoading.View {
    void hideComics();

    void showComics(List<ComicViewModel> comics);

    void showHasMore(boolean hasMore);

    void openComicDetails(int comicKey);
  }
}
