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

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

/**
 * Invoke methods annotated with UseCase annotation. The RosieUseCase instance will be executed out
 * of the Android main thread and the result of the operation will be provided asynchronously using
 * a callback.
 */
public class UseCaseHandler {

  private final TaskScheduler taskScheduler;
  private final ErrorHandler errorHandler;

  // This list is used to retain error adapters in a non-weak reference associated with this use
  // case handler
  private final List<OnErrorCallback> errorCallbacks = new ArrayList<>();

  public UseCaseHandler(TaskScheduler taskScheduler, ErrorHandler errorHandler) {
    this.taskScheduler = taskScheduler;
    this.errorHandler = errorHandler;
  }

  /**
   * Given a class configured with UseCase annotation executes the annotated
   * method out of the UI thread and returns the response, if needed, on the UI thread.
   *
   * @param useCase the use case to invoke.
   * @param useCaseParams params to use on the invocation.
   */
  void execute(RosieUseCase useCase, UseCaseParams useCaseParams) {
    UseCaseFilter.filter(useCase, useCaseParams);

    useCase.setOnSuccessCallback(useCaseParams.getOnSuccessCallback());

    OnErrorCallback onErrorCallback =
        new OnErrorCallbackToErrorHandlerAdapter(errorHandler, useCaseParams.getOnErrorCallback());
    errorCallbacks.add(onErrorCallback);
    useCase.setOnErrorCallback(onErrorCallback);

    UseCaseWrapper useCaseWrapper = new UseCaseWrapper(useCase, useCaseParams, errorHandler);
    taskScheduler.execute(useCaseWrapper);
  }

  public void registerGlobalErrorCallback(OnErrorCallback globalError) {
    errorHandler.registerCallback(globalError);
  }

  public void unregisterGlobalErrorCallback(OnErrorCallback globalError) {
    errorHandler.unregisterCallback(globalError);
  }

  /**
   * Inner class responsible for routing the errors thrown from the use case to the error handler
   */
  private static class OnErrorCallbackToErrorHandlerAdapter implements OnErrorCallback {
    private final WeakReference<ErrorHandler> errorHandler;
    private final WeakReference<OnErrorCallback> useCaseOnErrorCallback;

    public OnErrorCallbackToErrorHandlerAdapter(ErrorHandler errorHandler,
        OnErrorCallback useCaseOnErrorCallback) {
      this.errorHandler = new WeakReference<>(errorHandler);
      this.useCaseOnErrorCallback = new WeakReference<>(useCaseOnErrorCallback);
    }

    @Override public boolean onError(Error error) {
      ErrorHandler errorHandler = this.errorHandler.get();
      if (errorHandler != null) {
        errorHandler.notifyError(error, useCaseOnErrorCallback.get());
        return true;
      }
      return false;
    }
  }
}
