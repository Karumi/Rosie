/*
 * The MIT License (MIT) Copyright (c) 2014 karumi Permission is hereby granted, free of charge,
 * to any person obtaining a copy of this software and associated documentation files (the
 * "Software"), to deal in the Software without restriction, including without limitation the
 * rights to use, copy, modify, merge, publish, distribute, sublicense,
 * and/or sell copies of the Software, and to permit persons to whom the Software is furnished to
  * do so, subject to the following conditions: The above copyright notice and this permission
  * notice shall be included in all copies or substantial portions of the Software. THE SOFTWARE
  * IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED,
  * INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
  * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR
  * COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY,
  * WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
  * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package com.karumi.rosie.domain.usercase.jobqueue;

import com.karumi.rosie.domain.usercase.TaskScheduler;
import com.karumi.rosie.domain.usercase.UserCaseWrapper;
import com.path.android.jobqueue.JobManager;

/**
 * This is an implementation for a TaskScheduler based on PriorityJobQueue
 */
public class TaskSchedulerJobQueue implements TaskScheduler {
  private final JobManager jobManager;

  public TaskSchedulerJobQueue(JobManager jobManager) {
    this.jobManager = jobManager;
  }

  @Override public void execute(UserCaseWrapper userCaseWrapper) {
    UserCaseWrapperJob userCaseWrapperJob = new UserCaseWrapperJob(userCaseWrapper);
    jobManager.addJobInBackground(userCaseWrapperJob);
  }
}
