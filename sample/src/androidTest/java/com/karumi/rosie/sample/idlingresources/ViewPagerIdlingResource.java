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

package com.karumi.rosie.sample.idlingresources;

import android.support.test.espresso.IdlingResource;
import android.support.v4.view.ViewPager;

public class ViewPagerIdlingResource implements IdlingResource {

  private boolean idle = true;

  private ResourceCallback resourceCallback;

  public ViewPagerIdlingResource(ViewPager viewPager) {
    viewPager.addOnPageChangeListener(new ViewPagerListener());
  }

  @Override public String getName() {
    return "View Pager Idling Resource";
  }

  @Override public boolean isIdleNow() {
    return idle;
  }

  @Override public void registerIdleTransitionCallback(ResourceCallback resourceCallback) {
    this.resourceCallback = resourceCallback;
  }

  private class ViewPagerListener extends ViewPager.SimpleOnPageChangeListener {

    @Override public void onPageScrollStateChanged(int state) {
      idle = (state == ViewPager.SCROLL_STATE_IDLE
          // Treat dragging as idle, or Espresso will block itself when swiping.
          || state == ViewPager.SCROLL_STATE_DRAGGING);
      if (idle && resourceCallback != null) {
        resourceCallback.onTransitionToIdle();
      }
    }
  }
}