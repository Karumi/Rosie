/*
 *  Copyright (C) 2015 Karumi.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 *
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

