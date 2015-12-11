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
import com.karumi.rosie.sample.comics.domain.model.ComicSeries;
import com.karumi.rosie.sample.comics.domain.usecase.GetComicSeriesPage;
import com.karumi.rosie.sample.comics.view.viewmodel.ComicSeriesViewModel;
import com.karumi.rosie.sample.comics.view.viewmodel.mapper.ComicSeriesToComicSeriesViewModelMapper;
import com.karumi.rosie.view.loading.RosiePresenterWithLoading;
import java.util.List;
import javax.inject.Inject;

public class ComicsSeriesPresenter extends RosiePresenterWithLoading<ComicsSeriesPresenter.View> {

  private static final int NUMBER_OF_COMIC_SERIES_PER_PAGE = 14;
  private final ComicSeriesToComicSeriesViewModelMapper mapper;
  private final GetComicSeriesPage getComicSeriesPage;
  private int offset = 0;

  @Inject public ComicsSeriesPresenter(UseCaseHandler useCaseHandler,
      ComicSeriesToComicSeriesViewModelMapper mapper, GetComicSeriesPage getComicSeriesPage) {
    super(useCaseHandler);
    this.mapper = mapper;
    this.getComicSeriesPage = getComicSeriesPage;
  }

  @Override protected void update() {
    super.update();
    getView().hideComicSeries();
    showLoading();
    loadComics();
  }

  public void onLoadMore() {
    loadComics();
  }

  public void onComicSeriesClicked(ComicSeriesViewModel comicSeries) {
    getView().openComicSeriesDetails(comicSeries.getKey());
  }

  private void loadComics() {
    createUseCaseCall(getComicSeriesPage).args(offset, NUMBER_OF_COMIC_SERIES_PER_PAGE)
        .onSuccess(new OnSuccessCallback() {
          @Success public void onComicSeriesLoaded(PaginatedCollection<ComicSeries> comicSeries) {
            List<ComicSeriesViewModel> comicSeriesViewModels =
                mapper.mapComicSeriesToComicSeriesViewModels(comicSeries);
            hideLoading();
            getView().showHasMore(comicSeries.hasMore());
            getView().showComicSeries(comicSeriesViewModels);
            offset = comicSeries.getOffset() + NUMBER_OF_COMIC_SERIES_PER_PAGE;
          }
        })
        .execute();
  }

  public interface View extends RosiePresenterWithLoading.View {
    void hideComicSeries();

    void showComicSeries(List<ComicSeriesViewModel> comicSeries);

    void showHasMore(boolean hasMore);

    void openComicSeriesDetails(int comicSeriesKey);
  }
}
