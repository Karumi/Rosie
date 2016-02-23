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

package com.karumi.rosie.sample;

import android.support.test.espresso.matcher.BoundedMatcher;
import android.view.View;
import android.view.ViewParent;
import org.hamcrest.Description;
import org.hamcrest.Matcher;

public class AncestorMatcher {

  public static Matcher<Object> withAncestor(final Matcher<View> viewMatcher) {
    return new BoundedMatcher<Object, View>(View.class) {
      @Override public boolean matchesSafely(View view) {
        boolean found = false;
        ViewParent parent = view.getParent();
        while (!found &&  parent != null) {
          found = viewMatcher.matches(parent);
          parent = parent.getParent();
        }
        return found;
      }

      @Override public void describeTo(Description description) {
        description.appendText("with view ancestor: ");
        viewMatcher.describeTo(description);
      }
    };
  }
}