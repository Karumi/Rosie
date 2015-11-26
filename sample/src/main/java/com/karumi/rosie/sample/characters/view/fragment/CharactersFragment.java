/*
 * Copyright (C) 2015 Karumi.
 */

package com.karumi.rosie.sample.characters.view.fragment;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import butterknife.InjectView;
import com.karumi.rosie.view.Presenter;
import com.karumi.rosie.view.RosieFragment;
import com.karumi.rosie.sample.R;
import com.karumi.rosie.sample.characters.view.presenter.CharactersPresenter;
import com.karumi.rosie.sample.characters.view.renderer.CharacterRendererBuilder;
import com.karumi.rosie.sample.characters.view.viewmodel.CharacterViewModel;
import com.pedrogomez.renderers.ListAdapteeCollection;
import com.pedrogomez.renderers.RVRendererAdapter;
import com.pedrogomez.renderers.RendererBuilder;
import java.util.List;
import javax.inject.Inject;

public class CharactersFragment extends RosieFragment implements CharactersPresenter.View {

  @InjectView(R.id.rv_characters) RecyclerView charactersView;

  @Inject @Presenter CharactersPresenter presenter;

  private RVRendererAdapter<CharacterViewModel> charactersAdapter;

  @Override protected int getLayoutId() {
    return R.layout.fragment_characters;
  }

  @Override public void onViewCreated(View view, Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
    initializeCharactersView();
  }

  @Override public void showCharacters(List<CharacterViewModel> characters) {
    charactersAdapter.clear();
    charactersAdapter.addAll(characters);
    charactersAdapter.notifyDataSetChanged();
  }

  private void initializeCharactersView() {
    RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
    charactersView.setHasFixedSize(true);
    charactersView.setLayoutManager(layoutManager);
    initializeAdapter();
    charactersView.setAdapter(charactersAdapter);
  }

  private void initializeAdapter() {
    LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
    RendererBuilder<CharacterViewModel> rendererBuilder = new CharacterRendererBuilder(presenter);
    ListAdapteeCollection<CharacterViewModel> adapteeCollection = new ListAdapteeCollection<>();
    charactersAdapter = new RVRendererAdapter<>(layoutInflater, rendererBuilder, adapteeCollection);
  }
}
