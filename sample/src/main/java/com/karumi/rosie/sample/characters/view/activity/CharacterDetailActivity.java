/*
 * Copyright (C) 2015 Karumi.
 */

package com.karumi.rosie.sample.characters.view.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import butterknife.InjectView;
import com.karumi.rosie.sample.R;
import com.karumi.rosie.sample.characters.view.presenter.CharacterDetailPresenter;
import com.karumi.rosie.sample.characters.view.viewmodel.CharacterDetailViewModel;
import com.karumi.rosie.view.Presenter;
import com.karumi.rosie.view.RosieActivity;
import com.squareup.picasso.Picasso;
import javax.inject.Inject;

public class CharacterDetailActivity extends RosieActivity
    implements CharacterDetailPresenter.View {

  public static final String CHARACTER_KEY_EXTRA = "CharacterDetailActivity.CharacterKey";

  @InjectView(R.id.iv_character_image) ImageView characterHeaderView;
  @InjectView(R.id.ll_character_detail) View characterDetailView;
  @InjectView(R.id.tv_description) TextView characterDescriptionView;
  @InjectView(R.id.tv_loading) TextView loadingView;

  @Inject @Presenter CharacterDetailPresenter presenter;

  @Override protected int getLayoutId() {
    return R.layout.activity_character_details;
  }

  @Override protected void onPreparePresenter() {
    super.onPreparePresenter();
    Bundle extras = getIntent().getExtras();
    String characterKey = extras.getString(CHARACTER_KEY_EXTRA);
    presenter.setCharacterKey(characterKey);
  }

  @Override public void hideLoading() {
    loadingView.setVisibility(View.GONE);
  }

  @Override public void showLoading() {
    loadingView.setVisibility(View.VISIBLE);
  }

  @Override public void hideCharacterDetail() {
    characterDetailView.setVisibility(View.GONE);
  }

  @Override public void showCharacterDetail(CharacterDetailViewModel character) {
    characterDetailView.setVisibility(View.VISIBLE);
    Picasso.with(this).load(character.getHeaderImage()).into(characterHeaderView);
    characterDescriptionView.setText(character.getDescription());
  }

  public static void open(Context context, String characterKey) {
    Intent intent = new Intent(context, CharacterDetailActivity.class);
    intent.putExtra(CHARACTER_KEY_EXTRA, characterKey);
    context.startActivity(intent);
  }
}
