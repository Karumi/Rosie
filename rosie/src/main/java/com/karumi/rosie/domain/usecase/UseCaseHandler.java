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

package com.karumi.rosie.domain.usecase;

import com.karumi.rosie.domain.usecase.error.ErrorHandler;
import com.karumi.rosie.domain.usecase.error.OnErrorCallback;

/**
 * Invoke methods annotated with UseCase annotation. The RosieUseCase instance will be executed out
 * of the Android main thread and the result of the operation will be provided asynchronously using
 * a callback.
 */
public class UseCaseHandler {

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
