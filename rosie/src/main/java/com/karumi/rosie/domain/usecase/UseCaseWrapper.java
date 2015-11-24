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

import com.karumi.rosie.domain.usecase.error.ErrorHandler;
import java.lang.reflect.Method;

/**
 * This class wrap the use case to invoke it.
 */
public final class UseCaseWrapper {
  private final RosieUseCase useCase;
  private final UseCaseParams useCaseParams;
  private final ErrorHandler errorHandler;

  UseCaseWrapper(RosieUseCase useCase, UseCaseParams useCaseParams, ErrorHandler errorHandler) {
    this.useCase = useCase;
    this.useCaseParams = useCaseParams;
    this.errorHandler = errorHandler;
  }

  public void execute() {
    try {
      Method methodToInvoke = UseCaseFilter.filter(useCase, useCaseParams);
      methodToInvoke.invoke(useCase, useCaseParams.getArgs());
    } catch (Exception e) {
      notifyError(e);
    }
  }

  private void notifyError(Exception exception) {
    if (errorHandler != null) {
      errorHandler.notifyError(exception, useCaseParams.getOnErrorCallback());
    }
  }
}
