/*
 * https://gist.github.com/RomainPiel/ec10302a4687171a5e1a
 */

package com.karumi.rosie.sample.recyclerview;

import android.support.test.espresso.NoMatchingViewException;
import android.support.test.espresso.PerformException;
import android.support.test.espresso.ViewAssertion;
import android.support.test.espresso.util.HumanReadables;
import android.support.v7.widget.RecyclerView;
import android.view.View;

public class RecyclerItemViewAssertion<A> implements ViewAssertion {

  private int position;
  private A item;
  private RecyclerViewInteraction.ItemViewAssertion<A> itemViewAssertion;

  public RecyclerItemViewAssertion(int position, A item,
      RecyclerViewInteraction.ItemViewAssertion<A> itemViewAssertion) {
    this.position = position;
    this.item = item;
    this.itemViewAssertion = itemViewAssertion;
  }

  @Override public final void check(View view, NoMatchingViewException e) {
    RecyclerView recyclerView = (RecyclerView) view;
    RecyclerView.ViewHolder viewHolderForPosition =
        recyclerView.findViewHolderForLayoutPosition(position);
    if (viewHolderForPosition == null) {
      throw (new PerformException.Builder()).withActionDescription(toString())
          .withViewDescription(HumanReadables.describe(view))
          .withCause(new IllegalStateException("No view holder at position: " + position))
          .build();
    } else {
      View viewAtPosition = viewHolderForPosition.itemView;
      itemViewAssertion.check(item, viewAtPosition, e);
    }
  }
}

