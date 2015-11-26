/*
 * Copyright (C) 2015 Karumi.
 */

package com.karumi.rosie.sample.characters.view.renderer;

import com.karumi.rosie.sample.characters.view.viewmodel.CharacterViewModel;
import com.pedrogomez.renderers.ListAdapteeCollection;

public class CharactersAdapteeCollection extends ListAdapteeCollection<CharacterViewModel> {

  private boolean showLoadMore = true;

  public void setShowLoadMore(boolean showLoadMore) {
    this.showLoadMore = showLoadMore;
  }

  @Override public int size() {
    int size = super.size();
    return showLoadMore ? size + 1 : size;
  }

  @Override public CharacterViewModel get(int i) {
    CharacterViewModel item = null;
    if (i < super.size()) {
      item = super.get(i);
    }
    return item;
  }
}
