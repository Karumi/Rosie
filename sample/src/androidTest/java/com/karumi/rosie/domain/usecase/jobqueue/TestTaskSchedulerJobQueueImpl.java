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

package com.karumi.rosie.domain.usecase.jobqueue;

import com.karumi.rosie.domain.usecase.TaskScheduler;
import com.karumi.rosie.domain.usecase.UseCaseWrapper;
import com.path.android.jobqueue.JobManager;

public class TestTaskSchedulerJobQueueImpl implements TaskScheduler {
  private final JobManager jobManager;

  public TestTaskSchedulerJobQueueImpl(JobManager jobManager) {
    this.jobManager = jobManager;
    this.jobManager.start();
  }

  @Override public void execute(UseCaseWrapper useCaseWrapper) {
    UseCaseWrapperJob useCaseWrapperJob = new TestJob(useCaseWrapper);
    jobManager.addJobInBackground(useCaseWrapperJob);
  }

}
