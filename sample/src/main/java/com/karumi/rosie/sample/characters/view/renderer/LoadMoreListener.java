/*
 * Copyright (C) 2015 Karumi.
 */

package com.karumi.rosie.sample.characters.view.renderer;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

public class LoadMoreListener extends RecyclerView.OnScrollListener {

  private final LinearLayoutManager layoutManager;
  private final Listener listener;
  private boolean enabled;
  private boolean loading;

  public LoadMoreListener(LinearLayoutManager layoutManager, Listener listener) {
    this.layoutManager = layoutManager;
    this.listener = listener;
  }

  public void setEnabled(boolean enabled) {
    this.enabled = enabled;
  }

  public void setLoading(boolean loading) {
    this.loading = loading;
  }

  @Override public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
    int visibleItemCount = layoutManager.getChildCount();
    int totalItemCount = layoutManager.getItemCount();
    int pastVisibleItems = layoutManager.findFirstVisibleItemPosition();

    if (enabled && !loading) {
      if ((visibleItemCount + pastVisibleItems) >= totalItemCount) {
        if (listener != null) {
          listener.onLoadMore();
          loading = true;
        }
      }
    }
  }

  public interface Listener {
    void onLoadMore();
  }
}
