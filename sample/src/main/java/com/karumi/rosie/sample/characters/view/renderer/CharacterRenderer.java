/*
 * Copyright (C) 2015 Karumi.
 */

package com.karumi.rosie.sample.characters.view.renderer;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import butterknife.InjectView;
import butterknife.OnClick;
import com.karumi.rosie.renderer.RosieRenderer;
import com.karumi.rosie.sample.R;
import com.karumi.rosie.sample.characters.view.presenter.CharactersPresenter;
import com.karumi.rosie.sample.characters.view.viewmodel.CharacterViewModel;
import com.squareup.picasso.Picasso;

public class CharacterRenderer extends RosieRenderer<CharacterViewModel> {

  private final CharactersPresenter presenter;

  @InjectView(R.id.tv_character_name) TextView nameView;
  @InjectView(R.id.iv_character_thumbnail) ImageView thumbnailView;

  public CharacterRenderer(CharactersPresenter presenter) {
    this.presenter = presenter;
  }

  @Override public void render() {
    super.render();

    CharacterViewModel character = getContent();
    nameView.setText(character.getName());
    Picasso.with(getRootView().getContext())
        .load(character.getThumbnailUrl())
        .fit()
        .centerCrop()
        .into(thumbnailView);
  }

  @Override protected View inflate(LayoutInflater inflater, ViewGroup parent) {
    return inflater.inflate(R.layout.item_character, parent, false);
  }

  @OnClick(R.id.ll_root) public void onItemClicked() {
    CharacterViewModel character = getContent();
    presenter.onCharacterClicked(character);
  }
}
