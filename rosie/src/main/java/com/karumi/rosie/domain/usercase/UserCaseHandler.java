package com.karumi.rosie.domain.usercase;

import java.lang.reflect.Method;

/**
 * this is the handler for user cases, in you want to invoke an user case you need call to this
 * class with a valid user case. A valid usercase is this one that have an @usercase annotation.

 */
public class UserCaseHandler {
  private final UserCaseFilter userCaseFilter;
  private TaskScheduler taskScheduler;

  public UserCaseHandler(TaskScheduler taskScheduler) {
    this.taskScheduler = taskScheduler;
    userCaseFilter = new UserCaseFilter();
  }

  /**
   * Invoke an user case without arguments. This user case will invoke outside the main thread, and
   * the response come back on the main thread
   * @param userCase the user case to invoke
   */
  public void execute(Object userCase) {
    execute(userCase, (new UserCaseParams.Builder()).build());
  }

  /**
   * Invoke an user case with arguments. If you don't change it on the params this user case will
   * be invoked outside the main thread and the response come back to the ui thread.
   * @param userCase the
   * @param userCaseParams
   */
  public void execute(Object userCase, UserCaseParams userCaseParams) {

    Method methodsFiltered = userCaseFilter.filter(userCase, userCaseParams);

    if (methodsFiltered != null) {
      UserCaseWrapper userCaseWrapper = new UserCaseWrapper(userCase, userCaseParams);
      taskScheduler.execute(userCaseWrapper);
    }
  }
}
