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

import com.karumi.rosie.UnitTest;
import com.karumi.rosie.domain.usecase.annotation.UseCase;
import com.karumi.rosie.domain.usecase.error.ErrorHandler;
import com.karumi.rosie.domain.usecase.error.ErrorNotHandledException;
import com.karumi.rosie.domain.usecase.error.OnErrorCallback;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class UseCaseWrapperTest extends UnitTest {

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