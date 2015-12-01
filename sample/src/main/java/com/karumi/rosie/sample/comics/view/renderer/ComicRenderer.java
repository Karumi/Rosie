/*
 * Copyright (C) 2015 Karumi.
 */

package com.karumi.rosie.sample.comics.view.renderer;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import butterknife.InjectView;
import butterknife.OnClick;
import com.karumi.rosie.renderer.RosieRenderer;
import com.karumi.rosie.sample.R;
import com.karumi.rosie.sample.comics.view.presenter.ComicsPresenter;
import com.karumi.rosie.sample.comics.view.viewmodel.ComicViewModel;

public class ComicRenderer extends RosieRenderer<ComicViewModel> {

  private final ComicsPresenter presenter;

  @InjectView(R.id.tv_comic_name) TextView nameView;

  public ComicRenderer(ComicsPresenter presenter) {
    this.presenter = presenter;
  }

  @Override public void render() {
    super.render();

    ComicViewModel comic = getContent();
    nameView.setText(comic.getTitle());
  }

  @Override protected View inflate(LayoutInflater inflater, ViewGroup parent) {
    return inflater.inflate(R.layout.item_comic, parent, false);
  }

  @OnClick(R.id.ll_root) public void onItemClicked() {
    ComicViewModel comic = getContent();
    presenter.onComicClicked(comic);
  }
}
