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

package com.karumi.rosie.sample.comics.view.fragment;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import butterknife.Bind;
import butterknife.ButterKnife;

import com.karumi.dividers.Direction;
import com.karumi.dividers.DividerBuilder;
import com.karumi.dividers.DividerItemDecoration;
import com.karumi.dividers.Layer;
import com.karumi.dividers.LayersBuilder;
import com.karumi.dividers.selector.HeaderSelector;
import com.karumi.rosie.sample.R;
import com.karumi.rosie.sample.comics.view.activity.ComicSeriesDetailsActivity;
import com.karumi.rosie.sample.comics.view.presenter.ComicsSeriesPresenter;
import com.karumi.rosie.sample.comics.view.renderer.ComicSeriesRendererBuilder;
import com.karumi.rosie.sample.comics.view.renderer.ComicsSeriesAdapteeCollection;
import com.karumi.rosie.sample.comics.view.viewmodel.ComicSeriesViewModel;
import com.karumi.rosie.view.Presenter;
import com.karumi.rosie.view.RosieFragment;
import com.karumi.rosie.view.paginated.ScrollToBottomListener;
import com.pedrogomez.renderers.RVRendererAdapter;
import com.pedrogomez.renderers.RendererBuilder;
import com.victor.loading.rotate.RotateLoading;
import java.util.Collection;
import java.util.List;
import javax.inject.Inject;

public class ComicSeriesFragment extends RosieFragment implements ComicsSeriesPresenter.View {

  @Bind(R.id.rv_comics) RecyclerView comicSeriesView;
  @Bind(R.id.loading) RotateLoading loadingView;

  @Inject @Presenter ComicsSeriesPresenter presenter;

  private RVRendererAdapter<ComicSeriesViewModel> comicSeriesAdapter;
  private ComicsSeriesAdapteeCollection comicsCollection;
  private ScrollToBottomListener loadMoreListener;

  @Override protected int getLayoutId() {
    return R.layout.fragment_comic_series;
  }

  @Override
  protected void onPrepareFragment(View view) {
    super.onPrepareFragment(view);
    ButterKnife.bind(this, view);
  }

  @Override public void onViewCreated(View view, Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
    initializeComicSeriesView();
  }

  @Override public void hideLoading() {
    loadingView.stop();
    loadingView.setVisibility(View.GONE);
  }

  @Override public void showLoading() {
    loadingView.setVisibility(View.VISIBLE);
    loadingView.start();
  }

  @Override public void hideComicSeries() {
    comicSeriesView.setVisibility(View.GONE);
  }

  @Override public void clearComicSeries() {
    comicSeriesAdapter.clear();
  }

  @Override public void showComicSeries(List<ComicSeriesViewModel> comicSeries) {
    comicSeriesAdapter.addAll(comicSeries);
    comicSeriesAdapter.notifyDataSetChanged();
    comicSeriesView.setVisibility(View.VISIBLE);
  }

  @Override public void showHasMore(boolean hasMore) {
    comicsCollection.setShowLoadMore(hasMore);
    loadMoreListener.setIsProcessing(false);
    loadMoreListener.setIsEnabled(hasMore);
  }

  @Override public void openComicSeriesDetails(int comicSeriesKey) {
    ComicSeriesDetailsActivity.open(getActivity(), comicSeriesKey);
  }

  private void initializeComicSeriesView() {
    LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
    comicSeriesView.setHasFixedSize(true);
    comicSeriesView.setLayoutManager(layoutManager);
    initializeAdapter();
    comicSeriesView.addItemDecoration(getDivider());
    comicSeriesView.setAdapter(comicSeriesAdapter);
    loadMoreListener =
        new ScrollToBottomListener(layoutManager, new ScrollToBottomListener.Listener() {
          @Override public void onScrolledToBottom() {
            presenter.onLoadMore();
          }
        });
    comicSeriesView.addOnScrollListener(loadMoreListener);
  }

  @NonNull private DividerItemDecoration getDivider() {
    Drawable dividerBackground =
        ContextCompat.getDrawable(getActivity(), R.drawable.dark_blue_divider);
    Collection<Layer> layers = LayersBuilder.with(
        new Layer(DividerBuilder.from(dividerBackground).erase(Direction.getVertical()).build()),
        new Layer(new HeaderSelector(),
            DividerBuilder.fromEmpty().with(dividerBackground, Direction.SOUTH).build())).build();
    return new DividerItemDecoration(layers);
  }

  private void initializeAdapter() {
    RendererBuilder<ComicSeriesViewModel> rendererBuilder =
        new ComicSeriesRendererBuilder(presenter);
    comicsCollection = new ComicsSeriesAdapteeCollection();
    comicSeriesAdapter = new RVRendererAdapter<>(rendererBuilder, comicsCollection);
  }
}
