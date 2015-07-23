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

import com.karumi.rosie.domain.usecase.annotation.Success;
import com.karumi.rosie.domain.usecase.callback.CallbackScheduler;
import com.karumi.rosie.domain.usecase.callback.MainThreadCallbackScheduler;
import com.karumi.rosie.domain.usecase.callback.OnSuccessCallback;
import com.karumi.rosie.domain.usecase.error.ErrorNotHandledException;
import com.karumi.rosie.domain.usecase.error.OnErrorCallback;
import java.lang.ref.WeakReference;
import java.lang.reflect.Method;

/**
 * This is the base implementation of a UseCase. Every UseCase implementation has to extend from
 * this class.
 */
public class RosieUseCase {

  private WeakReference<OnSuccessCallback> onSuccess;
  private WeakReference<OnErrorCallback> onErrorCallback;
  private CallbackScheduler callbackScheduler;

  public void setCallbackScheduler(CallbackScheduler callbackScheduler) {
    validateCallbackScheduler(callbackScheduler);
    this.callbackScheduler = callbackScheduler;
  }

  /**
   * Notify to the callback onSuccess that something it's work fine. You can invoke the method as
   * many times as you want. You only need on your onSuccess a method with the same arguments.
   *
   * @param values that will be send to the onSuccess callback. Note: By default this method
   * return the response to the UI Thread.
   */
  protected void notifySuccess(Object... values) {
    Method[] methodsArray = onSuccess.get().getClass().getMethods();
    if (methodsArray.length > 0) {
      Method methodToInvoke =
          UseCaseFilter.filterValidMethodArgs(values, methodsArray, Success.class);
      invokeMethodInTheCallbackScheduler(methodToInvoke, values);
    }
  }

  /**
   * Notify to the error listener that an error happened, if you don't declare an specific error
   * handler for you use case, this error will be manage for the generic error system.
   *
   * @param error the error to send to the callback.
   * @throws ErrorNotHandledException this exception launch when the specific error is not
   * handled. You don't need manage this exception UseCaseHandler do it for you.
   */
  protected void notifyError(final Error error) throws ErrorNotHandledException {
    if (onErrorCallback.get() != null) {
      try {
        getCallbackScheduler().post(new Runnable() {
          @Override public void run() {
            onErrorCallback.get().onError(error);
          }
        });
      } catch (IllegalArgumentException e) {
        throw new ErrorNotHandledException(error);
      }
    } else {
      throw new ErrorNotHandledException(error);
    }
  }

  void setOnSuccess(OnSuccessCallback onSuccess) {
    this.onSuccess = new WeakReference<>(onSuccess);
  }

  void setOnError(OnErrorCallback onErrorCallback) {
    this.onErrorCallback = new WeakReference<OnErrorCallback>(onErrorCallback);
  }

  private void invokeMethodInTheCallbackScheduler(final Method methodToInvoke,
      final Object[] values) {
    getCallbackScheduler().post(new Runnable() {
      @Override public void run() {
        try {
          methodToInvoke.invoke(onSuccess.get(), values);
        } catch (Exception e) {
          throw new RuntimeException("Internal error invoking the success object", e);
        }
      }
    });
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
