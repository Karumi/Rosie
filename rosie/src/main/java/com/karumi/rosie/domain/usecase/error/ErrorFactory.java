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

import java.lang.reflect.InvocationTargetException;

/**
 * Provides Error instances given an Exception passed as argument. Create you own factory extending
 * from this class and override create method if needed.
 */
public class ErrorFactory {

  public Error create(Exception exception) {
    return null;
  }

  final Error createInternalException(Exception exception) {
    if (exception instanceof ErrorNotHandledException) {
      return ((ErrorNotHandledException) exception).getError();
    } else if (exception instanceof InvocationTargetException) {
      InvocationTargetException invocationTargetException = (InvocationTargetException) exception;
      Throwable targetException = invocationTargetException.getTargetException();
      if (targetException != null && targetException instanceof ErrorNotHandledException) {
        ErrorNotHandledException errorNotHandledException =
            (ErrorNotHandledException) targetException;
        return createInternalException(errorNotHandledException);
      } else {
        return new Error();
      }
    }
    return new Error("Generic Error", exception);
  }
}
