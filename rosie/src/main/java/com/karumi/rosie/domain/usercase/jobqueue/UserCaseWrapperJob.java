package com.karumi.rosie.domain.usercase.jobqueue;

import android.util.Log;
import com.karumi.rosie.domain.usercase.UserCaseWrapper;
import com.path.android.jobqueue.Job;
import com.path.android.jobqueue.Params;

/**
 * This class is an implementation of a job for priorityjobqueue. Manage the usercase inside
 * the queue system.
 */
class UserCaseWrapperJob extends Job {
  private static final int PRIORITY_NORMAL = 3;
  private static final String TAG = "UserCaseWrapperJob";
  private final UserCaseWrapper userCaseWrapper;

  public UserCaseWrapperJob(UserCaseWrapper userCaseWrapper) {
    super(new Params(PRIORITY_NORMAL));
    this.userCaseWrapper = userCaseWrapper;
  }

  @Override public void onAdded() {

  }

  @Override public void onRun() throws Throwable {
    try {
      userCaseWrapper.execute();
    } catch (Exception e) {
      Log.d(TAG, "unhandled exception from the usercase", e);
    }
  }

  @Override protected void onCancel() {

  }

  @Override protected boolean shouldReRunOnThrowable(Throwable throwable) {
    return false;
  }
}
