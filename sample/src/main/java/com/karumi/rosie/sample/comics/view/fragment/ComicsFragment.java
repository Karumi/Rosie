/*
 * Copyright (C) 2015 Karumi.
 */

package com.karumi.rosie.sample.comics.view.fragment;

import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import butterknife.InjectView;
import com.karumi.rosie.sample.R;
import com.karumi.rosie.sample.characters.view.renderer.LoadMoreListener;
import com.karumi.rosie.sample.comics.view.presenter.ComicsPresenter;
import com.karumi.rosie.sample.comics.view.renderer.ComicRendererBuilder;
import com.karumi.rosie.sample.comics.view.renderer.ComicsAdapteeCollection;
import com.karumi.rosie.sample.comics.view.viewmodel.ComicViewModel;
import com.karumi.rosie.view.Presenter;
import com.karumi.rosie.view.RosieFragment;
import com.pedrogomez.renderers.RVRendererAdapter;
import com.pedrogomez.renderers.RendererBuilder;
import java.util.List;
import javax.inject.Inject;

public class ComicsFragment extends RosieFragment implements ComicsPresenter.View {

  private static final int NUMBER_OF_COLUMNS = 3;

  @InjectView(R.id.rv_comics) RecyclerView comicsView;
  @InjectView(R.id.tv_loading) TextView loadingView;

  @Inject @Presenter ComicsPresenter presenter;

  private RVRendererAdapter<ComicViewModel> comicsAdapter;
  private ComicsAdapteeCollection comicsCollection;
  private LoadMoreListener loadMoreListener;

  @Override protected int getLayoutId() {
    return R.layout.fragment_comics;
  }

  @Override public void onViewCreated(View view, Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
    initializeComicsView();
  }

  @Override public void hideLoading() {
    loadingView.setVisibility(View.GONE);
  }

  @Override public void showLoading() {
    loadingView.setVisibility(View.VISIBLE);
  }

  @Override public void hideComics() {
    comicsView.setVisibility(View.GONE);
  }

  @Override public void showComics(List<ComicViewModel> comics) {
    comicsAdapter.addAll(comics);
    comicsAdapter.notifyDataSetChanged();
    comicsView.setVisibility(View.VISIBLE);
  }

  private void initializeComicsView() {
    GridLayoutManager layoutManager = new GridLayoutManager(getActivity(), NUMBER_OF_COLUMNS);
    comicsView.setHasFixedSize(true);
    comicsView.setLayoutManager(layoutManager);
    initializeAdapter();
    comicsView.setAdapter(comicsAdapter);
    loadMoreListener = new LoadMoreListener(layoutManager, new LoadMoreListener.Listener() {
      @Override public void onLoadMore() {
        presenter.onLoadMore();
      }
    });
    comicsView.addOnScrollListener(loadMoreListener);
  }

  private void initializeAdapter() {
    LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
    RendererBuilder<ComicViewModel> rendererBuilder = new ComicRendererBuilder(presenter);
    comicsCollection = new ComicsAdapteeCollection();
    comicsAdapter = new RVRendererAdapter<>(layoutInflater, rendererBuilder, comicsCollection);
  }
}
