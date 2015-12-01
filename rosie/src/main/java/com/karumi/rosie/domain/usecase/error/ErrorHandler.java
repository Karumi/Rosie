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

package com.karumi.rosie.domain.usecase.error;

import com.karumi.rosie.domain.usecase.callback.CallbackScheduler;
import com.karumi.rosie.domain.usecase.callback.MainThreadCallbackScheduler;
import java.util.ArrayList;
import java.util.List;
import javax.inject.Inject;

public class ErrorHandler {

  private final List<OnErrorCallback> errorCallbacks = new ArrayList<>();
  private ErrorFactory errorFactory;
  private CallbackScheduler callbackScheduler;

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

  public void notifyError(Exception exception, final OnErrorCallback useCaseErrorCallback) {
    final Error error = createError(exception);
    callbackScheduler.post(new Runnable() {
      @Override public void run() {
        if (useCaseErrorCallback != null) {
          useCaseErrorCallback.onError(error);
        } else {
          notifyError(error);
        }
      }
    });
  }

  private void notifyError(final Error error) {
    for (OnErrorCallback errorCallback : errorCallbacks) {
      errorCallback.onError(error);
    }
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
