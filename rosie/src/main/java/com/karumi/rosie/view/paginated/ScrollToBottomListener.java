/*
 * Copyright (C) 2015 Karumi.
 */

package com.karumi.rosie.view.paginated;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

/**
 * RecyclerView listener to be notified whenever the user scrolls to the bottom of the view.
 */
public class ScrollToBottomListener extends RecyclerView.OnScrollListener {

  private final LinearLayoutManager layoutManager;
  private final Listener listener;
  private boolean isEnabled;
  private boolean isProcessing;

  public ScrollToBottomListener(LinearLayoutManager layoutManager, Listener listener) {
    this.layoutManager = layoutManager;
    this.listener = listener;
  }

  public void setIsEnabled(boolean isEnabled) {
    this.isEnabled = isEnabled;
  }

  public void setIsProcessing(boolean isProcessing) {
    this.isProcessing = isProcessing;
  }

  @Override public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
    int visibleItemCount = layoutManager.getChildCount();
    int totalItemCount = layoutManager.getItemCount();
    int pastVisibleItems = layoutManager.findFirstVisibleItemPosition();

    if (isEnabled && !isProcessing) {
      if ((visibleItemCount + pastVisibleItems) >= totalItemCount) {
        if (listener != null) {
          listener.onScrolledToBottom();
          isProcessing = true;
        }
      }
    }
  }

  public interface Listener {
    void onScrolledToBottom();
  }
}
