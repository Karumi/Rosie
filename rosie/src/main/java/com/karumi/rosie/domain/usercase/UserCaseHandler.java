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

package com.karumi.rosie.domain.usercase;

import com.karumi.rosie.domain.usercase.error.GlobalErrorDispatcher;
import com.karumi.rosie.domain.usercase.error.UseCaseErrorCallback;

/**
 * Invoke methods annotated with UseCase annotation. The RosieUseCase instance will be executed out
 * of the Android main thread and the result of the operation will be provided asynchronously using
 * a callback.
 */
public class UserCaseHandler {
  private static final GlobalErrorDispatcher EMPTY_ERROR_DISPACHER = new GlobalErrorDispatcher();
  private final TaskScheduler taskScheduler;
  private final GlobalErrorDispatcher errorDispatcher;

  public UserCaseHandler(TaskScheduler taskScheduler) {
    this(taskScheduler, EMPTY_ERROR_DISPACHER);
  }

  public UserCaseHandler(TaskScheduler taskScheduler, GlobalErrorDispatcher errorDispatcher) {
    this.taskScheduler = taskScheduler;
    this.errorDispatcher = errorDispatcher;
  }

  /**
   * Invoke an user case without arguments. This user case will invoke outside the main thread, and
   * the response comes back to the main thread.
   *
   * @param useCase the user case to invoke.
   */
  public void execute(RosieUseCase useCase) {
    execute(useCase, (new UserCaseParams.Builder()).build());
  }

  /**
   * Given a class configured with UseCase annotation executes the annotated
   * method out of the UI thread and return the response, if needed it, over the UI thread.
   *
   * @param userCase the user case to invoke.
   * @param userCaseParams params to use on the invokation.
   */
  public void execute(RosieUseCase useCase, UserCaseParams userCaseParams) {
    UserCaseFilter.filter(useCase, userCaseParams);

    useCase.setOnSuccess(userCaseParams.getOnSuccessCallback());
    useCase.setOnError(userCaseParams.getErrorCallback());
    UserCaseWrapper userCaseWrapper = new UserCaseWrapper(useCase, userCaseParams, errorDispatcher);
    taskScheduler.execute(userCaseWrapper);
  }

  public void registerGlobalErrorCallback(UseCaseErrorCallback globalError) {
    errorDispatcher.registerCallback(globalError);
  }

  public void unregisterGlobalErrorCallback(UseCaseErrorCallback globalError) {
    errorDispatcher.unregisterCallback(globalError);
  }
}
