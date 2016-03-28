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

import com.karumi.rosie.domain.usecase.annotation.UseCase;
import com.karumi.rosie.domain.usecase.error.ErrorHandler;
import com.karumi.rosie.domain.usecase.error.ErrorNotHandledException;
import com.karumi.rosie.domain.usecase.error.OnErrorCallback;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class) public class UseCaseWrapperTest {

  private static final String ANY_FIRST_ARG = "anyValue";
  private static final int ANY_SECOND_ARG = 2;

  @Test public void testExecuteWithArgs() throws Exception {
    AnyUseCase anyUseCase = new AnyUseCase();
    UseCaseParams argsParams = givenUseCaseParamsWithArgs(ANY_FIRST_ARG, ANY_SECOND_ARG);

    UseCaseWrapper useCaseWrapper = new UseCaseWrapper(anyUseCase, argsParams, null);

    useCaseWrapper.execute();

    assertEquals("anyValue", anyUseCase.getArg1());
    assertEquals(2, anyUseCase.getArg2());
  }

  private UseCaseParams givenUseCaseParamsWithArgs(Object... args) {
    return new UseCaseParams(null, args, null, null);
  }

  @Test public void shouldNotifyErrorNotifiedFromTheUseCase() {
    AnyUseCase anyUseCase = new AnyUseCase();
    anyUseCase.setErrorToNotify(new Error());
    UseCaseParams argsParams = givenUseCaseParamsWithArgs(ANY_FIRST_ARG, ANY_SECOND_ARG);
    ErrorHandler errorHandler = mock(ErrorHandler.class);
    UseCaseWrapper useCaseWrapper = new UseCaseWrapper(anyUseCase, argsParams, errorHandler);

    useCaseWrapper.execute();

    verify(errorHandler).notifyException(any(ErrorNotHandledException.class),
        eq((OnErrorCallback) null));
  }

  private class AnyUseCase extends RosieUseCase {

    private String arg1;
    private int arg2;

    private Error errorToNotify;

    AnyUseCase() {
    }

    public void setErrorToNotify(Error error) {
      this.errorToNotify = error;
    }

    @UseCase public void methodWithArgs(String arg1, int arg2) throws ErrorNotHandledException {
      if (errorToNotify != null) {
        notifyError(errorToNotify);
      } else {
        this.arg1 = arg1;
        this.arg2 = arg2;
      }
    }

    private String getArg1() {
      return arg1;
    }

    private int getArg2() {
      return arg2;
    }
  }
}