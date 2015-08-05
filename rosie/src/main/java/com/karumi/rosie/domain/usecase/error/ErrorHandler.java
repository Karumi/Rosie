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

package com.karumi.rosie.domain.usecase.error;

import com.karumi.rosie.domain.usecase.callback.CallbackScheduler;
import com.karumi.rosie.domain.usecase.callback.MainThreadCallbackScheduler;
import java.util.ArrayList;
import java.util.List;
import javax.inject.Inject;

public class ErrorHandler {

  private ErrorFactory errorFactory;
  private CallbackScheduler callbackScheduler;
  private List<OnErrorCallback> errorCallbacks = new ArrayList<>();

  @Inject public ErrorHandler() {
    this(new ErrorFactory(), new MainThreadCallbackScheduler());
  }

  public ErrorHandler(ErrorFactory errorFactory) {
    this(errorFactory, new MainThreadCallbackScheduler());
  }

  public ErrorHandler(CallbackScheduler callbackScheduler) {
    this(new ErrorFactory(), callbackScheduler);
  }

  public ErrorHandler(ErrorFactory errorFactory, CallbackScheduler callbackScheduler) {
    this.errorFactory = errorFactory;
    this.callbackScheduler = callbackScheduler;
  }

  public void setErrorFactory(ErrorFactory errorFactory) {
    if (errorFactory == null) {
      throw new IllegalArgumentException("errorFactory can not be null");
    }
    this.errorFactory = errorFactory;
  }

  public void setCallbackScheduler(CallbackScheduler callbackScheduler) {
    if (callbackScheduler == null) {
      throw new IllegalArgumentException("callbackScheduler can not be null");
    }
    this.callbackScheduler = callbackScheduler;
  }

  public void notifyError(Exception exception, OnErrorCallback useCaseErrorCallback) {
    Error error = createError(exception);
    if (useCaseErrorCallback != null) {
      useCaseErrorCallback.onError(error);
    } else {
      notifyError(error);
    }
  }

  private void notifyError(final Error error) {
    callbackScheduler.post(new Runnable() {
      @Override public void run() {
        for (OnErrorCallback errorCallback : errorCallbacks) {
          errorCallback.onError(error);
        }
      }
    });
  }

  public void registerCallback(OnErrorCallback onErrorCallback) {
    errorCallbacks.add(onErrorCallback);
  }

  public void unregisterCallback(OnErrorCallback onErrorCallback) {
    errorCallbacks.remove(onErrorCallback);
  }

  private Error createError(Exception exception) {
    Error error = null;
    if (errorFactory != null) {
      error = errorFactory.create(exception);

      if (error == null) {
        error = errorFactory.createInternalException(exception);
      }
    }

    if (error == null) {
      error = new Error("Generic Error", exception);
    }

    return error;
  }
}
