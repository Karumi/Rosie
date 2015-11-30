/*
 * Copyright (C) 2015 Karumi.
 */

package com.karumi.rosie.sample.characters.view.fragment;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import butterknife.InjectView;
import com.karumi.rosie.sample.R;
import com.karumi.rosie.sample.characters.view.activity.CharacterDetailActivity;
import com.karumi.rosie.sample.characters.view.presenter.CharactersPresenter;
import com.karumi.rosie.sample.characters.view.renderer.CharacterRendererBuilder;
import com.karumi.rosie.sample.characters.view.renderer.CharactersAdapteeCollection;
import com.karumi.rosie.sample.characters.view.renderer.LoadMoreListener;
import com.karumi.rosie.sample.characters.view.viewmodel.CharacterViewModel;
import com.karumi.rosie.view.Presenter;
import com.karumi.rosie.view.RosieFragment;
import com.pedrogomez.renderers.RVRendererAdapter;
import com.pedrogomez.renderers.RendererBuilder;
import java.util.List;
import javax.inject.Inject;

public class CharactersFragment extends RosieFragment implements CharactersPresenter.View {

  @InjectView(R.id.rv_characters) RecyclerView charactersView;
  @InjectView(R.id.tv_loading) TextView loadingView;

  @Inject @Presenter CharactersPresenter presenter;

  private RVRendererAdapter<CharacterViewModel> charactersAdapter;
  private CharactersAdapteeCollection charactersCollection;
  private LoadMoreListener loadMoreListener;

  @Override protected int getLayoutId() {
    return R.layout.fragment_characters;
  }

  @Override public void onViewCreated(View view, Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
    initializeCharactersView();
  }

  @Override public void hideLoading() {
    loadingView.setVisibility(View.GONE);
  }

  @Override public void showLoading() {
    loadingView.setVisibility(View.VISIBLE);
  }

  @Override public void hideCharacters() {
    charactersView.setVisibility(View.GONE);
  }

  @Override public void showCharacters(List<CharacterViewModel> characters) {
    charactersAdapter.addAll(characters);
    charactersAdapter.notifyDataSetChanged();
    charactersView.setVisibility(View.VISIBLE);
  }

  @Override public void showHasMore(boolean hasMore) {
    charactersCollection.setShowLoadMore(hasMore);
    loadMoreListener.setLoading(false);
    loadMoreListener.setEnabled(hasMore);
  }

  @Override public void openCharacterDetails(String characterKey) {
    CharacterDetailActivity.open(getActivity(), characterKey);
  }

  private void initializeCharactersView() {
    LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
    charactersView.setHasFixedSize(true);
    charactersView.setLayoutManager(layoutManager);
    initializeAdapter();
    charactersView.setAdapter(charactersAdapter);
    loadMoreListener = new LoadMoreListener(layoutManager, new LoadMoreListener.Listener() {
      @Override public void onLoadMore() {
        presenter.onLoadMore();
      }
    });
    charactersView.addOnScrollListener(loadMoreListener);
  }

  private void initializeAdapter() {
    LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
    RendererBuilder<CharacterViewModel> rendererBuilder = new CharacterRendererBuilder(presenter);
    charactersCollection = new CharactersAdapteeCollection();
    charactersAdapter =
        new RVRendererAdapter<>(layoutInflater, rendererBuilder, charactersCollection);
  }
}
