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
      notifyException(e);
    }
  }

  private void notifyException(Exception exception) {
    if (errorHandler != null) {
      errorHandler.notifyException(exception, useCaseParams.getOnErrorCallback());
    }
  }
}
