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

package com.karumi.rosie.sample.comics.view.renderer;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import butterknife.Bind;
import butterknife.OnClick;
import com.karumi.rosie.sample.R;
import com.karumi.rosie.sample.base.view.renderer.MarvelRenderer;
import com.karumi.rosie.sample.comics.view.presenter.ComicsSeriesPresenter;
import com.karumi.rosie.sample.comics.view.viewmodel.ComicSeriesViewModel;

public class ComicSeriesRenderer extends MarvelRenderer<ComicSeriesViewModel> {

  private final ComicsSeriesPresenter presenter;

  @Bind(R.id.tv_comic_series_name) TextView nameView;

  public ComicSeriesRenderer(ComicsSeriesPresenter presenter) {
    this.presenter = presenter;
  }

  @Override public void render() {
    super.render();
    ComicSeriesViewModel comicSeries = getContent();
    nameView.setText(comicSeries.getTitle());
  }

  @Override protected View inflate(LayoutInflater inflater, ViewGroup parent) {
    return inflater.inflate(R.layout.item_comic_series, parent, false);
  }

  @OnClick(R.id.ll_root) public void onItemClicked() {
    ComicSeriesViewModel comicSeries = getContent();
    presenter.onComicSeriesClicked(comicSeries);
  }
}
