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

import com.karumi.rosie.domain.usecase.callback.OnSuccessCallback;
import com.karumi.rosie.domain.usecase.error.OnErrorCallback;
import java.lang.ref.WeakReference;

/**
 * The params value to execute with a use case.
 */
public final class UseCaseParams {

  private final String useCaseName;
  private final Object[] args;

  private WeakReference<OnSuccessCallback> onSuccessCallback;
  private WeakReference<OnErrorCallback> onErrorCallback;

  private UseCaseParams(String useCaseName, Object[] args, OnSuccessCallback onSuccessCallback,
      OnErrorCallback onErrorCallback) {
    this.args = args;
    this.useCaseName = useCaseName;
    if (onSuccessCallback != null) {
      this.onSuccessCallback = new WeakReference<>(onSuccessCallback);
    }
    if (onErrorCallback != null) {
      this.onErrorCallback = new WeakReference<>(onErrorCallback);
    }
  }

  public OnSuccessCallback getOnSuccessCallback() {
    return onSuccessCallback != null ? onSuccessCallback.get() : null;
  }

  public OnErrorCallback getOnErrorCallback() {
    return onErrorCallback != null ? onErrorCallback.get() : null;
  }

  String getUseCaseName() {
    return useCaseName;
  }

  Object[] getArgs() {
    return args;
  }

  public static class Builder {

    private String useCaseName = "";
    private Object[] args;
    private OnSuccessCallback onSuccess;
    private OnErrorCallback errorCallback;

    public Builder useCaseName(String name) {
      useCaseName = name;
      return this;
    }

    public Builder args(Object... args) {
      this.args = args;
      return this;
    }

    /**
     * The OnSuccessCallback passed as argument in this method will be referenced as a
     * WeakReference inside RosieUseCase and UseCaseParams to avoid memory leaks during the
     * Activity lifecycle pause-destroy stage. Remember to keep a strong reference of your
     * OnSuccessCallback instance if needed.
     */
    public Builder onSuccess(OnSuccessCallback onSuccessCallback) {
      if (onSuccessCallback == null) {
        throw new IllegalArgumentException(
            "OnSuccessCallback is null. You can not invoke it with" + " null callback.");
      }

      this.onSuccess = onSuccessCallback;
      return this;
    }

    /**
     * The OnErrorCallback passed as argument in this method will be referenced as a
     * WeakReference inside RosieUseCase and UseCaseParams to avoid memory leaks during the
     * Activity lifecycle pause-destroy stage. Remember to keep a strong reference of your
     * OnErrorCallback instance if needed.
     */
    public Builder onError(OnErrorCallback errorCallback) {
      if (errorCallback == null) {
        throw new IllegalArgumentException(
            "The onErrorCallback used is null, you can't use a null instance as onError callback.");
      }
      this.errorCallback = errorCallback;
      return this;
    }

    public UseCaseParams build() {
      if (this.args == null) {
        args = new Object[0];
      }
      return new UseCaseParams(useCaseName, args, onSuccess, errorCallback);
    }
  }
}
