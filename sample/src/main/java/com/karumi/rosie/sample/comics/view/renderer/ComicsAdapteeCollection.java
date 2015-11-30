/*
 * Copyright (C) 2015 Karumi.
 */

package com.karumi.rosie.sample.comics.view.renderer;

import com.karumi.rosie.sample.comics.view.viewmodel.ComicViewModel;
import com.pedrogomez.renderers.ListAdapteeCollection;

public class ComicsAdapteeCollection extends ListAdapteeCollection<ComicViewModel> {

  private boolean showLoadMore = true;

  public void setShowLoadMore(boolean showLoadMore) {
    this.showLoadMore = showLoadMore;
  }

  @Override public int size() {
    int size = super.size();
    return showLoadMore ? size + 1 : size;
  }

  @Override public ComicViewModel get(int i) {
    ComicViewModel item = null;
    if (i < super.size()) {
      item = super.get(i);
    }
    return item;
  }
}
