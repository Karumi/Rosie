/*
 * Copyright (C) 2015 Karumi.
 */

package com.karumi.rosie.sample.comics.view.renderer;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import butterknife.InjectView;
import butterknife.OnClick;
import com.karumi.rosie.renderer.RosieRenderer;
import com.karumi.rosie.sample.R;
import com.karumi.rosie.sample.comics.view.presenter.ComicsPresenter;
import com.karumi.rosie.sample.comics.view.viewmodel.ComicViewModel;
import com.squareup.picasso.Picasso;

public class ComicRenderer extends RosieRenderer<ComicViewModel> {

  private final ComicsPresenter presenter;

  @InjectView(R.id.tv_comic_name) TextView nameView;
  @InjectView(R.id.iv_comic_cover) ImageView coverView;

  public ComicRenderer(ComicsPresenter presenter) {
    this.presenter = presenter;
  }

  @Override public void render() {
    super.render();

    ComicViewModel comic = getContent();
    nameView.setText(comic.getName());
    Picasso.with(getRootView().getContext())
        .load(comic.getCoverUrl())
        .fit()
        .centerCrop()
        .into(coverView);
  }

  @Override protected View inflate(LayoutInflater inflater, ViewGroup parent) {
    return inflater.inflate(R.layout.item_comic, parent, false);
  }

  @OnClick(R.id.ll_root) public void onItemClicked() {
    ComicViewModel comic = getContent();
    presenter.onComicClicked(comic);
  }
}
