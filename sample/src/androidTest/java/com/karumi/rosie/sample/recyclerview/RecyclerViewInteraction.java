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
 */

package com.karumi.rosie.sample.recyclerview;

import android.support.test.espresso.NoMatchingViewException;
import android.view.View;
import java.util.List;
import org.hamcrest.Matcher;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.contrib.RecyclerViewActions.scrollToPosition;

public class RecyclerViewInteraction<A> {
  private Matcher<View> viewMatcher;
  private List<A> items;

  private RecyclerViewInteraction(Matcher<View> viewMatcher) {
    this.viewMatcher = viewMatcher;
  }

  public static <A> RecyclerViewInteraction<A> onRecyclerView(Matcher<View> viewMatcher) {
    return new RecyclerViewInteraction<>(viewMatcher);
  }

  public RecyclerViewInteraction<A> withItems(List<A> items) {
    this.items = items;
    return this;
  }

  public RecyclerViewInteraction<A> check(ItemViewAssertion<A> itemViewAssertion) {
    for (int i = 0; i < items.size(); i++) {
      onView(viewMatcher)
          .perform(scrollToPosition(i))
          .check(new RecyclerItemViewAssertion<>(i, items.get(i), itemViewAssertion));
    }
    return this;
  }

  public interface ItemViewAssertion<A> {
    void check(A item, View view, NoMatchingViewException e);
  }
}
