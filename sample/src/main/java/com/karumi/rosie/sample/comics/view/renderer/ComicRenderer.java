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
import android.widget.ImageView;
import android.widget.TextView;
import butterknife.Bind;
import com.karumi.rosie.sample.R;
import com.karumi.rosie.sample.base.view.renderer.MarvelRenderer;
import com.karumi.rosie.sample.comics.view.viewmodel.ComicSeriesDetailViewModel;
import com.karumi.rosie.sample.comics.view.viewmodel.ComicViewModel;
import com.squareup.picasso.Picasso;

public class ComicRenderer extends MarvelRenderer<ComicSeriesDetailViewModel> {

  @Bind(R.id.iv_thumbnail) ImageView thumbnailView;
  @Bind(R.id.tv_title) TextView titleView;

  @Override public void render() {
    super.render();

    ComicViewModel comic = (ComicViewModel) getContent();
    titleView.setText(comic.getTitle());
    Picasso.with(getRootView().getContext())
        .load(comic.getThumbnailUrl())
        .fit()
        .centerCrop()
        .into(thumbnailView);
  }

  @Override protected View inflate(LayoutInflater inflater, ViewGroup parent) {
    return inflater.inflate(R.layout.item_comic, parent, false);
  }
}
