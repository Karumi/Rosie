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
