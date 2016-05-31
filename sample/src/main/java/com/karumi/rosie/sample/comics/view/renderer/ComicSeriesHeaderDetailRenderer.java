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

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import butterknife.Bind;
import com.karumi.rosie.sample.R;
import com.karumi.rosie.sample.base.view.renderer.MarvelRenderer;
import com.karumi.rosie.sample.comics.view.viewmodel.ComicSeriesDetailViewModel;
import com.karumi.rosie.sample.comics.view.viewmodel.ComicSeriesHeaderDetailViewModel;
import com.squareup.picasso.Picasso;

public class ComicSeriesHeaderDetailRenderer extends MarvelRenderer<ComicSeriesDetailViewModel> {

  @Bind(R.id.iv_cover) ImageView coverView;
  @Bind(R.id.tv_rating) TextView ratingView;
  @Bind(R.id.tv_description) TextView descriptionView;

  @Override public void render() {
    super.render();

    Context context = getRootView().getContext();
    ComicSeriesHeaderDetailViewModel comicSeries = (ComicSeriesHeaderDetailViewModel) getContent();

    Picasso.with(context).load(comicSeries.getCoverUrl()).fit().centerCrop().into(coverView);
    ratingView.setText(context.getString(R.string.marvel_rating_text, comicSeries.getRating()));

    if (comicSeries.getDescription() != null && !comicSeries.getDescription().isEmpty()) {
      descriptionView.setText(comicSeries.getDescription());
    } else {
      descriptionView.setText(getRootView().getResources().getString(R.string.no_description));
    }
  }

  @Override protected View inflate(LayoutInflater inflater, ViewGroup parent) {
    return inflater.inflate(R.layout.item_comic_series_header, parent, false);
  }
}
