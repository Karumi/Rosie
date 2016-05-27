/*
 * Copyright (C) 2015 Karumi.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.karumi.rosie.domain.usecase.jobqueue;

import com.karumi.rosie.domain.usecase.UseCaseWrapper;
import com.path.android.jobqueue.Job;
import com.path.android.jobqueue.Params;
import com.path.android.jobqueue.RetryConstraint;

/**
 * Job extension created to be able to execute a use case using android-priority-job-queue.
 */
class UseCaseWrapperJob extends Job {
  private static final int PRIORITY_NORMAL = 3;
  private final UseCaseWrapper useCaseWrapper;

  public UseCaseWrapperJob(UseCaseWrapper useCaseWrapper) {
    super(new Params(PRIORITY_NORMAL));
    this.useCaseWrapper = useCaseWrapper;
  }

  @Override public void onAdded() {

  }

  @Override public void onRun() throws Throwable {
    useCaseWrapper.execute();
  }

  @Override protected void onCancel() {

  }

  @Override protected RetryConstraint shouldReRunOnThrowable(Throwable throwable, int runCount, int maxRunCount) {
    return RetryConstraint.CANCEL;
  }
}
