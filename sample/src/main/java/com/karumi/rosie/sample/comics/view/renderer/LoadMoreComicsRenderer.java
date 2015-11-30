/*
 * Copyright (C) 2015 Karumi.
 */

package com.karumi.rosie.sample.comics.view.renderer;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.karumi.rosie.renderer.RosieRenderer;
import com.karumi.rosie.sample.R;
import com.karumi.rosie.sample.comics.view.viewmodel.ComicViewModel;

public class LoadMoreComicsRenderer extends RosieRenderer<ComicViewModel> {

  @Override protected View inflate(LayoutInflater inflater, ViewGroup parent) {
    return inflater.inflate(R.layout.item_load_more, parent, false);
  }
}
