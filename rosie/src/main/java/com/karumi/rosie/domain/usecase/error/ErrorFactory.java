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
