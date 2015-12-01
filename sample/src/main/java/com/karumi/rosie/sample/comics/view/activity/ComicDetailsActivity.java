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

package com.karumi.rosie.sample.comics.view.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import butterknife.InjectView;
import com.karumi.rosie.sample.R;
import com.karumi.rosie.sample.comics.ComicsModule;
import com.karumi.rosie.sample.comics.view.presenter.ComicDetailsPresenter;
import com.karumi.rosie.sample.comics.view.viewmodel.ComicDetailsViewModel;
import com.karumi.rosie.view.Presenter;
import com.karumi.rosie.view.RosieActivity;
import com.squareup.picasso.Picasso;
import java.util.Arrays;
import java.util.List;
import javax.inject.Inject;

public class ComicDetailsActivity extends RosieActivity implements ComicDetailsPresenter.View {

  private static final String COMIC_KEY_EXTRA = "ComicDetailsActivity.ComicKey";
  private static final int INVALID_COMIC_KEY_EXTRA = -1;

  @InjectView(R.id.ll_comic_detail) View comicView;
  @InjectView(R.id.iv_cover) ImageView coverView;
  @InjectView(R.id.tv_rating) TextView ratingView;
  @InjectView(R.id.tv_description) TextView descriptionView;
  @InjectView(R.id.tv_loading) TextView loadingView;

  @Inject @Presenter ComicDetailsPresenter presenter;

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    int comicKey = getIntent().getIntExtra(COMIC_KEY_EXTRA, INVALID_COMIC_KEY_EXTRA);
    presenter.setComicKey(comicKey);
  }

  @Override protected int getLayoutId() {
    return R.layout.activity_comic_details;
  }

  @Override protected List<Object> getActivityScopeModules() {
    return Arrays.asList((Object) new ComicsModule());
  }

  @Override public void hideLoading() {
    loadingView.setVisibility(View.GONE);
  }

  @Override public void showLoading() {
    loadingView.setVisibility(View.VISIBLE);
  }

  @Override public void hideComicDetails() {
    comicView.setVisibility(View.GONE);
  }

  @Override public void showComicDetails(ComicDetailsViewModel comic) {
    Picasso.with(this).load(comic.getCoverUrl()).fit().centerCrop().into(coverView);
    String rating = getString(comic.getRatingNameResourceId());
    ratingView.setText(getString(R.string.marvel_rating_text, rating));
    descriptionView.setText(comic.getDescription());
    comicView.setVisibility(View.VISIBLE);
  }

  public static void open(Context context, int comicKey) {
    Intent intent = new Intent(context, ComicDetailsActivity.class);
    intent.putExtra(COMIC_KEY_EXTRA, comicKey);
    context.startActivity(intent);
  }
}
