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

package com.karumi.rosie.sample.characters.view.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import butterknife.Bind;
import com.karumi.rosie.sample.R;
import com.karumi.rosie.sample.base.view.activity.MarvelActivity;
import com.karumi.rosie.sample.characters.CharactersModule;
import com.karumi.rosie.sample.characters.view.presenter.CharacterDetailsPresenter;
import com.karumi.rosie.sample.characters.view.viewmodel.CharacterDetailViewModel;
import com.karumi.rosie.view.Presenter;
import com.squareup.picasso.Picasso;
import com.victor.loading.rotate.RotateLoading;
import java.util.Arrays;
import java.util.List;
import javax.inject.Inject;

public class CharacterDetailsActivity extends MarvelActivity
    implements CharacterDetailsPresenter.View {

  private static final String CHARACTER_KEY_EXTRA = "CharacterDetailsActivity.CharacterKey";

  @Bind(R.id.tv_toolbar_title) TextView toolbarTitleView;
  @Bind(R.id.iv_character_image) ImageView characterHeaderView;
  @Bind(R.id.ll_character_detail) View characterDetailView;
  @Bind(R.id.tv_character_name) TextView characterNameView;
  @Bind(R.id.tv_description) TextView characterDescriptionView;
  @Bind(R.id.loading) RotateLoading loadingView;

  @Inject @Presenter CharacterDetailsPresenter presenter;

  @Override protected int getLayoutId() {
    return R.layout.activity_character_details;
  }

  @Override protected List<Object> getActivityScopeModules() {
    return Arrays.asList((Object) new CharactersModule());
  }

  @Override protected void onPreparePresenter() {
    super.onPreparePresenter();
    Bundle extras = getIntent().getExtras();
    String characterKey = extras.getString(CHARACTER_KEY_EXTRA);
    presenter.setCharacterKey(characterKey);
  }

  @Override public void hideLoading() {
    loadingView.stop();
    loadingView.setVisibility(View.GONE);
  }

  @Override public void showLoading() {
    loadingView.setVisibility(View.VISIBLE);
    loadingView.start();
  }

  @Override public void hideCharacterDetail() {
    characterDetailView.setVisibility(View.GONE);
  }

  @Override public void showCharacterDetail(CharacterDetailViewModel character) {
    characterDetailView.setVisibility(View.VISIBLE);
    toolbarTitleView.setText(character.getName());
    Picasso.with(this)
        .load(character.getHeaderImage())
        .fit()
        .centerCrop()
        .into(characterHeaderView);
    characterNameView.setText(character.getName());

    if (!character.getDescription().equals("")) {
      characterDescriptionView.setText(character.getDescription());
    } else {
      characterDescriptionView.setText(getString(R.string.no_description));
    }
  }

  public static void open(Context context, String characterKey) {
    Intent intent = new Intent(context, CharacterDetailsActivity.class);
    intent.putExtra(CHARACTER_KEY_EXTRA, characterKey);
    context.startActivity(intent);
  }
}
