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

import com.karumi.rosie.domain.usecase.callback.OnSuccessCallback;
import com.karumi.rosie.domain.usecase.error.OnErrorCallback;
import java.lang.ref.WeakReference;

/**
 * The params value to execute with the use case.
 */
public class UseCaseParams {

  private final String useCaseName;
  private final Object[] args;

  private WeakReference<OnSuccessCallback> onSuccessCallback;
  private WeakReference<OnErrorCallback> onErrorCallback;

  public UseCaseParams(String useCaseName, Object[] args, OnSuccessCallback onSuccessCallback,
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

  String getUseCaseName() {
    return useCaseName;
  }

  public Object[] getArgs() {
    return args;
  }

  public OnSuccessCallback getOnSuccessCallback() {
    return onSuccessCallback != null ? onSuccessCallback.get() : null;
  }

  public OnErrorCallback getOnErrorCallback() {
    return onErrorCallback != null ? onErrorCallback.get() : null;
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
