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

import android.support.test.espresso.IdlingResource;

import com.karumi.rosie.domain.usecase.jobqueue.TestJob;
import java.util.concurrent.atomic.AtomicBoolean;

public class PriorityJobQueueIdleMonitor implements IdlingResource {

  private AtomicBoolean mWasIdle = new AtomicBoolean(true);
  private volatile ResourceCallback mResourceCallback;

  @Override
  public String getName() {
    return PriorityJobQueueIdleMonitor.class.getName();
  }

  @Override
  public boolean isIdleNow() {
    boolean isIdle = TestJob.COUNTER.get() == 0;
    boolean wasIdle = mWasIdle.getAndSet(isIdle);
    if (isIdle && !wasIdle && mResourceCallback != null) {
      mResourceCallback.onTransitionToIdle();
      mResourceCallback = null;
    }
    return isIdle;
  }

  @Override
  public void registerIdleTransitionCallback(final ResourceCallback callback) {
    mResourceCallback = callback;
  }
}
