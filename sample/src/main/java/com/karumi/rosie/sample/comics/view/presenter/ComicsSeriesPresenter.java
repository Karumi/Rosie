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
import com.karumi.rosie.repository.PaginatedCollection;
import com.karumi.rosie.repository.datasource.paginated.Page;
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

  @Override public void update() {
    super.update();
    getView().hideComicSeries();
    showLoading();

    PaginatedCollection<ComicSeries> allComicsInCache = getComicSeriesPage.getAllComicsInCache();
    if (allComicsInCache.getPage().getLimit() == 0) {
      loadComics();
    } else {
      getView().clearComicSeries();
      showComics(allComicsInCache);
      offset = allComicsInCache.getItems().size();
    }
  }

  public void onLoadMore() {
    loadComics();
  }

  public void onComicSeriesClicked(ComicSeriesViewModel comicSeries) {
    getView().openComicSeriesDetails(comicSeries.getKey());
  }

  private void loadComics() {
    createUseCaseCall(getComicSeriesPage).args(
        Page.withOffsetAndLimit(offset, NUMBER_OF_COMIC_SERIES_PER_PAGE))
        .onSuccess(new OnSuccessCallback() {
          @Success public void onComicSeriesLoaded(PaginatedCollection<ComicSeries> comicSeries) {
            showComics(comicSeries);
            offset = comicSeries.getPage().getOffset() + NUMBER_OF_COMIC_SERIES_PER_PAGE;
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

  private void showComics(PaginatedCollection<ComicSeries> comicSeries) {
    List<ComicSeriesViewModel> comicSeriesViewModels =
        mapper.mapComicSeriesToComicSeriesViewModels(comicSeries);
    hideLoading();
    getView().showHasMore(comicSeries.hasMore());
    getView().showComicSeries(comicSeriesViewModels);
  }

  public interface View extends RosiePresenterWithLoading.View {
    void hideComicSeries();

    void clearComicSeries();

    void showComicSeries(List<ComicSeriesViewModel> comicSeries);

    void showHasMore(boolean hasMore);

    void openComicSeriesDetails(int comicSeriesKey);
  }
}
