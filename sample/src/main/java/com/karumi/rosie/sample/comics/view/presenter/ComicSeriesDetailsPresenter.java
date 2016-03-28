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
import com.karumi.rosie.domain.usecase.error.OnErrorCallback;
import com.karumi.rosie.sample.base.view.presenter.MarvelPresenter;
import com.karumi.rosie.sample.comics.domain.model.ComicSeries;
import com.karumi.rosie.sample.comics.domain.usecase.GetComicSeriesDetails;
import com.karumi.rosie.sample.comics.view.viewmodel.ComicSeriesDetailsViewModel;
import com.karumi.rosie.sample.comics.view.viewmodel.mapper.ComicSeriesToComicSeriesDetailsViewModelMapper;
import javax.inject.Inject;

public class ComicSeriesDetailsPresenter
    extends MarvelPresenter<ComicSeriesDetailsPresenter.View> {

  private final GetComicSeriesDetails getComicSeriesDetails;
  private final ComicSeriesToComicSeriesDetailsViewModelMapper mapper;
  private int comicSeriesKey;

  @Inject public ComicSeriesDetailsPresenter(UseCaseHandler useCaseHandler,
      GetComicSeriesDetails getComicSeriesDetails,
      ComicSeriesToComicSeriesDetailsViewModelMapper mapper) {
    super(useCaseHandler);
    this.getComicSeriesDetails = getComicSeriesDetails;
    this.mapper = mapper;
  }

  public void setComicSeriesKey(int comicKey) {
    this.comicSeriesKey = comicKey;
  }

  @Override public void update() {
    super.update();
    showLoading();
    getView().hideComicSeriesDetails();
    loadComicDetails();
  }

  private void loadComicDetails() {
    createUseCaseCall(getComicSeriesDetails).args(comicSeriesKey)
        .onSuccess(new OnSuccessCallback() {
          @Success public void onComicSeriesDetailsLoaded(ComicSeries comicSeries) {
            ComicSeriesDetailsViewModel comicSeriesDetailsViewModel =
                mapper.map(comicSeries);
            hideLoading();
            getView().showComicSeriesDetails(comicSeriesDetailsViewModel);
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
    void hideComicSeriesDetails();

    void showComicSeriesDetails(ComicSeriesDetailsViewModel comicSeries);
  }
}
