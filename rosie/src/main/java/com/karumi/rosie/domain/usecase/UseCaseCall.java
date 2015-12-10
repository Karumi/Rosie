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

package com.karumi.rosie.domain.usecase;

import com.karumi.rosie.domain.usecase.callback.OnSuccessCallback;
import com.karumi.rosie.domain.usecase.error.OnErrorCallback;

public final class UseCaseCall {
  private final RosieUseCase useCase;
  private final UseCaseHandler useCaseHandler;

  private String useCaseName;
  private Object[] args;
  private OnSuccessCallback onSuccessCallback;
  private OnErrorCallback onErrorCallback;

  public UseCaseCall(RosieUseCase useCase, UseCaseHandler useCaseHandler) {
    this.useCase = useCase;
    this.useCaseHandler = useCaseHandler;
  }

  public UseCaseCall useCaseName(String name) {
    useCaseName = name;
    return this;
  }

  public UseCaseCall args(Object... args) {
    this.args = args;
    return this;
  }

  /**
   * The OnSuccessCallback passed as argument in this method will be referenced as a WeakReference
   * inside RosieUseCase and UseCaseParams to avoid memory leaks during the Activity lifecycle
   * pause-destroy stage. Remember to keep a strong reference of your OnSuccessCallback instance if
   * needed.
   */
  public UseCaseCall onSuccess(OnSuccessCallback onSuccessCallback) {
    if (onSuccessCallback == null) {
      throw new IllegalArgumentException(
          "OnSuccessCallback is null. You can not invoke it with" + " null callback.");
    }

    this.onSuccessCallback = onSuccessCallback;
    return this;
  }

  /**
   * The OnErrorCallback passed as argument in this method will be referenced as a WeakReference
   * inside RosieUseCase and UseCaseParams to avoid memory leaks during the Activity lifecycle
   * pause-destroy stage. Remember to keep a strong reference of your OnErrorCallback instance if
   * needed.
   */
  public UseCaseCall onError(OnErrorCallback errorCallback) {
    if (errorCallback == null) {
      throw new IllegalArgumentException(
          "The onErrorCallback used is null, you can't use a null instance as onError callback.");
    }
    this.onErrorCallback = errorCallback;
    return this;
  }

  public void execute() {
    if (args == null) {
      args = new Object[0];
    }
    UseCaseParams useCaseParams =
        new UseCaseParams(useCaseName, args, onSuccessCallback, onErrorCallback);
    useCaseHandler.execute(useCase, useCaseParams);
  }
}
