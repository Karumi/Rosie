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

package com.karumi.rosie.domain.usecase;

import com.karumi.rosie.domain.usecase.error.ErrorHandler;
import com.karumi.rosie.domain.usecase.error.OnErrorCallback;

/**
 * Invoke methods annotated with UseCase annotation. The RosieUseCase instance will be executed out
 * of the Android main thread and the result of the operation will be provided asynchronously using
 * a callback.
 */
public final class UseCaseHandler {

  private final TaskScheduler taskScheduler;
  private final ErrorHandler errorHandler;

  public UseCaseHandler(TaskScheduler taskScheduler, ErrorHandler errorHandler) {
    this.taskScheduler = taskScheduler;
    this.errorHandler = errorHandler;
  }

  /**
   * Invoke an use case without arguments. This use case will be invoked out of the main thread,
   * and the response will be handled in the main thread.
   *
   * @param useCase the use case to invoke.
   */
  public void execute(RosieUseCase useCase) {
    execute(useCase, (new UseCaseParams.Builder()).build());
  }

  /**
   * Given a class configured with UseCase annotation executes the annotated
   * method out of the UI thread and returns the response, if needed, on the UI thread.
   *
   * @param useCase the use case to invoke.
   * @param useCaseParams params to use on the invocation.
   */
  public void execute(RosieUseCase useCase, UseCaseParams useCaseParams) {
    UseCaseFilter.filter(useCase, useCaseParams);

    useCase.setOnSuccessCallback(useCaseParams.getOnSuccessCallback());
    useCase.setOnErrorCallback(useCaseParams.getOnErrorCallback());
    UseCaseWrapper useCaseWrapper = new UseCaseWrapper(useCase, useCaseParams, errorHandler);
    taskScheduler.execute(useCaseWrapper);
  }

  public void registerGlobalErrorCallback(OnErrorCallback globalError) {
    errorHandler.registerCallback(globalError);
  }

  public void unregisterGlobalErrorCallback(OnErrorCallback globalError) {
    errorHandler.unregisterCallback(globalError);
  }
}
