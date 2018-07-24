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

import com.karumi.rosie.domain.usecase.annotation.Success;
import com.karumi.rosie.domain.usecase.callback.CallbackScheduler;
import com.karumi.rosie.domain.usecase.callback.MainThreadCallbackScheduler;
import com.karumi.rosie.domain.usecase.callback.OnSuccessCallback;
import com.karumi.rosie.domain.usecase.error.ErrorNotHandledException;
import com.karumi.rosie.domain.usecase.error.OnErrorCallback;
import java.lang.ref.WeakReference;
import java.lang.reflect.Method;

/**
 * This is the base implementation of a UseCase. Every UseCase implementation has to extend this
 * class.
 * To define the method that will be executed for your RosieUseCase implementation annotate it with
 * {@link com.karumi.rosie.domain.usecase.annotation.UseCase}.
 */
public class RosieUseCase {

  private WeakReference<OnSuccessCallback> onSuccessCallback;
  private WeakReference<OnErrorCallback> onErrorCallback;

  private CallbackScheduler callbackScheduler;

  public void setCallbackScheduler(CallbackScheduler callbackScheduler) {
    validateCallbackScheduler(callbackScheduler);
    this.callbackScheduler = callbackScheduler;
  }

  /**
   * Notify to the callback onSuccessCallback that the use case has worked fine. You can invoke the
   * method as many times as you want. You only need on your onSuccessCallback a method with the
   * same arguments.
   *
   * @param values values that will be sent to the onSuccessCallback callback. Note: By default
   * this method returns the response in the UI Thread.
   */
  protected void notifySuccess(Object... values) {
    OnSuccessCallback successCallback = onSuccessCallback.get();
    if (successCallback != null) {
      Method[] methodsArray = successCallback.getClass().getMethods();
      if (methodsArray.length > 0) {
        Method methodToInvoke =
            UseCaseFilter.filterValidMethodArgs(values, methodsArray, Success.class);
        invokeMethodInTheCallbackScheduler(methodToInvoke, values);
      } else {
        throw new IllegalStateException(
            "The OnSuccessCallback instance configured has no methods annotated with the "
                + "@Success annotation.");
      }
    }
  }

  /**
   * Notify to the error listener that an error happened, if you don't declare an specific error
   * handler for your UseCase, this error will be managed by the generic error system.
   *
   * @param error the error to send to the callback.
   * @throws ErrorNotHandledException this exception is thrown when the specific error is not
   * handled. You don't need to manage this exception, UseCaseHandler can do it for you.
   */

  protected void notifyError(final Error error) throws ErrorNotHandledException {
    OnErrorCallback errorCallback = onErrorCallback.get();
    if (errorCallback != null) {
      errorCallback.onError(error);
    }
  }

  /**
   * The OnSuccessCallback passed as argument in this method will be referenced as a
   * WeakReference inside RosieUseCase and UseCaseParams to avoid memory leaks during the
   * Activity lifecycle pause-destroy stage. Remember to keep a strong reference of your
   * OnSuccessCallback instance if needed.
   */
  void setOnSuccessCallback(OnSuccessCallback onSuccessCallback) {
    if (onSuccessCallback != null) {
      this.onSuccessCallback = new WeakReference<>(onSuccessCallback);
    }
  }

  /**
   * The OnErrorCallback passed as argument in this method will be referenced as a
   * WeakReference inside RosieUseCase and UseCaseParams to avoid memory leaks during the
   * Activity lifecycle pause-destroy stage. Remember to keep a strong reference of your
   * OnErrorCallback instance if needed.
   */
  void setOnErrorCallback(OnErrorCallback onErrorCallback) {
    if (onErrorCallback != null) {
      this.onErrorCallback = new WeakReference<>(onErrorCallback);
    }
  }

  private void invokeMethodInTheCallbackScheduler(final Method methodToInvoke,
      final Object[] values) {
    if (onSuccessCallback != null) {
      final OnSuccessCallback callback = onSuccessCallback.get();
      if (callback != null) {
        getCallbackScheduler().post(new Runnable() {
          @Override public void run() {
            try {
              methodToInvoke.invoke(callback, values);
            } catch (Exception e) {
              throw new RuntimeException("Internal error invoking the success object", e);
            }
          }
        });
      }
    }
  }

  private CallbackScheduler getCallbackScheduler() {
    if (callbackScheduler == null) {
      callbackScheduler = new MainThreadCallbackScheduler();
    }
    return callbackScheduler;
  }

  private void validateCallbackScheduler(CallbackScheduler callbackScheduler) {
    if (callbackScheduler == null) {
      throw new IllegalArgumentException("You can't use a null instance as CallbackScheduler.");
    }
  }
}
