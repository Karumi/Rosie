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

package com.karumi.rosie.sample.characters.view.renderer;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import butterknife.Bind;
import butterknife.OnClick;
import com.karumi.rosie.sample.R;
import com.karumi.rosie.sample.base.view.renderer.MarvelRenderer;
import com.karumi.rosie.sample.characters.view.presenter.CharactersPresenter;
import com.karumi.rosie.sample.characters.view.viewmodel.CharacterViewModel;
import com.squareup.picasso.Picasso;

public class CharacterRenderer extends MarvelRenderer<CharacterViewModel> {

  private final CharactersPresenter presenter;

  @Bind(R.id.tv_character_name) TextView nameView;
  @Bind(R.id.iv_character_image) ImageView avatarView;

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
        .into(avatarView);
  }

  @Override protected View inflate(LayoutInflater inflater, ViewGroup parent) {
    return inflater.inflate(R.layout.item_character, parent, false);
  }

  @OnClick(R.id.ll_root) public void onItemClicked() {
    CharacterViewModel character = getContent();
    presenter.onCharacterClicked(character);
  }
}
