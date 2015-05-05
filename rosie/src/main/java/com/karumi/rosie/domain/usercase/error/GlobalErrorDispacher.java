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

package com.karumi.rosie.domain.usercase.error;

import java.util.ArrayList;
import java.util.List;

/**
 *
 */
public class GlobalErrorDispacher {

  private ErrorFactory errorFactory;
  private List<UseCaseErrorCallback> errorCallbacks = new ArrayList<>();

  public GlobalErrorDispacher() {
  }

  public void setErrorFactory(ErrorFactory errorFactory) {
    this.errorFactory = errorFactory;
  }

  public void notifyError(Exception exception) {
    DomainError domainError = createError(exception);
    notifyError(domainError);
  }

  private void notifyError(DomainError domainError) {
    for (UseCaseErrorCallback errorCallback : errorCallbacks) {
      errorCallback.onError(domainError);
    }
  }

  public void registerCallback(UseCaseErrorCallback useCaseErrorCallback) {
    errorCallbacks.add(useCaseErrorCallback);
  }

  public void unregisterCallback(UseCaseErrorCallback useCaseErrorCallback) {
    errorCallbacks.remove(useCaseErrorCallback);
  }

  private DomainError createError(Exception exception) {
    DomainError domainError = null;
    if (errorFactory != null) {
      domainError = errorFactory.create(exception);

      if (domainError == null) {
        domainError = errorFactory.createInternalException(exception);
      }
    }



    if (domainError == null) {
      domainError = new GenericError("Generic Error", exception);
    }

    return domainError;
  }
}
