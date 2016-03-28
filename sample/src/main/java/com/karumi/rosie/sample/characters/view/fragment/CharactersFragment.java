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

package com.karumi.rosie.sample.characters.view.fragment;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import butterknife.Bind;
import com.karumi.dividers.Direction;
import com.karumi.dividers.DividerBuilder;
import com.karumi.dividers.DividerItemDecoration;
import com.karumi.dividers.Layer;
import com.karumi.dividers.LayersBuilder;
import com.karumi.dividers.selector.HeaderSelector;
import com.karumi.rosie.sample.R;
import com.karumi.rosie.sample.base.view.fragment.MarvelFragment;
import com.karumi.rosie.sample.characters.view.activity.CharacterDetailsActivity;
import com.karumi.rosie.sample.characters.view.presenter.CharactersPresenter;
import com.karumi.rosie.sample.characters.view.renderer.CharacterRendererBuilder;
import com.karumi.rosie.sample.characters.view.renderer.CharactersAdapteeCollection;
import com.karumi.rosie.sample.characters.view.viewmodel.CharacterViewModel;
import com.karumi.rosie.view.Presenter;
import com.karumi.rosie.view.paginated.ScrollToBottomListener;
import com.pedrogomez.renderers.RVRendererAdapter;
import com.pedrogomez.renderers.RendererBuilder;
import com.victor.loading.rotate.RotateLoading;
import java.util.Collection;
import java.util.List;
import javax.inject.Inject;

public class CharactersFragment extends MarvelFragment implements CharactersPresenter.View {

  @Bind(R.id.rv_characters) RecyclerView charactersView;
  @Bind(R.id.loading) RotateLoading loadingView;

  @Inject @Presenter CharactersPresenter presenter;

  private RVRendererAdapter<CharacterViewModel> charactersAdapter;
  private CharactersAdapteeCollection charactersCollection;
  private ScrollToBottomListener loadMoreListener;

  @Override protected int getLayoutId() {
    return R.layout.fragment_characters;
  }

  @Override public void onViewCreated(View view, Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
    initializeCharactersView();
  }

  @Override public void hideLoading() {
    loadingView.stop();
    loadingView.setVisibility(View.GONE);
  }

  @Override public void showLoading() {
    loadingView.setVisibility(View.VISIBLE);
    loadingView.start();
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
    loadMoreListener.setIsProcessing(false);
    loadMoreListener.setIsEnabled(hasMore);
  }

  @Override public void openCharacterDetails(String characterKey) {
    CharacterDetailsActivity.open(getActivity(), characterKey);
  }

  @Override public void clearCharacters() {
    charactersAdapter.clear();
  }

  private void initializeCharactersView() {
    LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
    charactersView.setHasFixedSize(true);
    charactersView.setLayoutManager(layoutManager);
    initializeAdapter();
    charactersView.addItemDecoration(getDivider());
    charactersView.setAdapter(charactersAdapter);
    loadMoreListener =
        new ScrollToBottomListener(layoutManager, new ScrollToBottomListener.Listener() {
          @Override public void onScrolledToBottom() {
            presenter.onLoadMore();
          }
        });
    charactersView.addOnScrollListener(loadMoreListener);
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
    RendererBuilder<CharacterViewModel> rendererBuilder = new CharacterRendererBuilder(presenter);
    charactersCollection = new CharactersAdapteeCollection();
    charactersAdapter = new RVRendererAdapter<>(rendererBuilder, charactersCollection);
  }
}
