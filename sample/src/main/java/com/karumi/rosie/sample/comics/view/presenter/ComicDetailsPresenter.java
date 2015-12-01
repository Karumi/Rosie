/*
 * Copyright (C) 2015 Karumi.
 */

package com.karumi.rosie.sample.comics.view.presenter;

import com.karumi.rosie.domain.usecase.UseCaseHandler;
import com.karumi.rosie.domain.usecase.UseCaseParams;
import com.karumi.rosie.domain.usecase.annotation.Success;
import com.karumi.rosie.domain.usecase.callback.OnSuccessCallback;
import com.karumi.rosie.sample.comics.domain.model.Comic;
import com.karumi.rosie.sample.comics.domain.usecase.GetComicDetails;
import com.karumi.rosie.sample.comics.view.viewmodel.ComicDetailsViewModel;
import com.karumi.rosie.sample.comics.view.viewmodel.mapper.ComicToComicDetailsViewModelMapper;
import com.karumi.rosie.view.loading.RosiePresenterWithLoading;
import javax.inject.Inject;

public class ComicDetailsPresenter extends RosiePresenterWithLoading<ComicDetailsPresenter.View> {

  private final GetComicDetails getComicDetails;
  private final ComicToComicDetailsViewModelMapper mapper;
  private int comicKey;

  @Inject
  public ComicDetailsPresenter(UseCaseHandler useCaseHandler, GetComicDetails getComicDetails,
      ComicToComicDetailsViewModelMapper mapper) {
    super(useCaseHandler);
    this.getComicDetails = getComicDetails;
    this.mapper = mapper;
  }

  public void setComicKey(int comicKey) {
    this.comicKey = comicKey;
  }

  @Override protected void update() {
    super.update();
    showLoading();
    getView().hideComicDetails();
    loadComicDetails();
  }

  private void loadComicDetails() {
    UseCaseParams params =
        new UseCaseParams.Builder().args(comicKey).onSuccess(new OnSuccessCallback() {
          @Success public void onComicDetailsLoaded(Comic comic) {
            ComicDetailsViewModel comicDetailsViewModel =
                mapper.mapComicToComicDetailsViewModel(comic);
            hideLoading();
            getView().showComicDetails(comicDetailsViewModel);
          }
        }).build();
    execute(getComicDetails, params);
  }

  public interface View extends RosiePresenterWithLoading.View {
    void hideComicDetails();

    void showComicDetails(ComicDetailsViewModel comic);
  }
}
