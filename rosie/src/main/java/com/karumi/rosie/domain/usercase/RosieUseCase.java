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

package com.karumi.rosie.domain.usercase;

import com.karumi.rosie.domain.usercase.annotation.Success;
import com.karumi.rosie.domain.usercase.callback.OnSuccessCallback;
import com.karumi.rosie.domain.usercase.error.DomainError;
import com.karumi.rosie.domain.usercase.error.UseCaseErrorCallback;
import com.karumi.rosie.domain.usercase.error.UseCaseInternalException;
import java.lang.reflect.Method;

/**
 * This is the base implementation of a UseCase. Every UseCase implementation has to extend from
 * this class.
 */
public class RosieUseCase {
  private OnSuccessCallback onSuccess;
  private UseCaseErrorCallback useCaseErrorCallback;

  protected void notifySuccess(Object... values) {

    Method[] methodsArray = onSuccess.getClass().getMethods();
    if (methodsArray.length > 0) {
      Method methodToInvoke =
          UserCaseFilter.filterValidMethodArgs(values, methodsArray, Success.class);

      try {
        methodToInvoke.invoke(onSuccess, values);
      } catch (Exception e) {
        throw new RuntimeException("internal error invoking the success object", e);
      }
    }
  }

  protected void notifyError(DomainError domainError) throws UseCaseInternalException {

    if (useCaseErrorCallback != null) {
      try {
        useCaseErrorCallback.onError(domainError);
      } catch (IllegalArgumentException e) {
        throw new UseCaseInternalException(domainError);
      }
    } else {
      throw new UseCaseInternalException(domainError);
    }
  }

  void setOnSuccess(OnSuccessCallback onSuccess) {
    this.onSuccess = onSuccess;
  }

  void setOnError(UseCaseErrorCallback useCaseErrorCallback) {
    this.useCaseErrorCallback = useCaseErrorCallback;
  }
}
