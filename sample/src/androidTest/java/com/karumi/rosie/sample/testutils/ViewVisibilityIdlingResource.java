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

package com.karumi.rosie.sample.testutils;

import android.app.Activity;
import android.support.test.espresso.IdlingResource;
import android.view.View;

public class ViewVisibilityIdlingResource implements IdlingResource {

  private final int expectedVisibility;
  private final Activity activity;
  private final int viewId;

  private boolean mIdle;
  private ResourceCallback mResourceCallback;

  public ViewVisibilityIdlingResource(final Activity activity, int viewId,
      final int expectedVisibility) {
    this.activity = activity;
    this.viewId = viewId;
    this.expectedVisibility = expectedVisibility;
    this.mIdle = false;
    this.mResourceCallback = null;
  }

  @Override
  public final String getName() {
    return ViewVisibilityIdlingResource.class.getSimpleName();
  }

  @Override
  public final boolean isIdleNow() {
    View view = activity.findViewById(viewId);
    if(view == null) {
      return false;
    }

    mIdle = mIdle || view.getVisibility() == expectedVisibility;

    if (mIdle) {
      if (mResourceCallback != null) {
        mResourceCallback.onTransitionToIdle();
      }
    }

    return mIdle;
  }

  @Override
  public void registerIdleTransitionCallback(ResourceCallback resourceCallback) {
    mResourceCallback = resourceCallback;
  }
}
