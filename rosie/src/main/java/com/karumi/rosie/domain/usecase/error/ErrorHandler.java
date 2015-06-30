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

import java.util.ArrayList;
import java.util.List;

public class ErrorHandler {

  private ErrorFactory errorFactory;
  private List<OnErrorCallback> errorCallbacks = new ArrayList<>();

  public ErrorHandler() {
    this.errorFactory = new ErrorFactory();
  }

  public void setErrorFactory(ErrorFactory errorFactory) {
    if (errorFactory == null) {
      throw new IllegalArgumentException("errorFactory can not be null");
    }
    this.errorFactory = errorFactory;
  }

  public void notifyError(Exception exception) {
    Error error = createError(exception);
    notifyError(error);
  }

  private void notifyError(Error error) {
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
