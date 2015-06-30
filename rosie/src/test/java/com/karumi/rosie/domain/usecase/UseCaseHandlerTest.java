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
import com.karumi.rosie.domain.usecase.annotation.Success;
import com.karumi.rosie.domain.usecase.annotation.UseCase;
import com.karumi.rosie.domain.usecase.callback.OnSuccessCallback;
import com.karumi.rosie.domain.usecase.error.ErrorHandler;
import com.karumi.rosie.domain.usecase.error.ErrorNotHandledException;

import com.karumi.rosie.doubles.FakeCallbackScheduler;
import com.karumi.rosie.domain.usecase.error.OnErrorCallback;
import com.karumi.rosie.doubles.NetworkError;
import com.karumi.rosie.testutils.FakeTaskScheduler;
import java.util.ArrayList;
import java.util.List;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.only;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;

public class UseCaseHandlerTest extends UnitTest {

  private static final int ANY_RETURN_VALUE = 2;

  @Test public void testExecuteAnyObject() throws Exception {
    TaskScheduler taskScheduler = mock(TaskScheduler.class);
    EmptyUseCase anyUseCase = new EmptyUseCase();
    ErrorHandler errorHandler = new ErrorHandler(new FakeCallbackScheduler());
    UseCaseHandler useCaseHandler = new UseCaseHandler(taskScheduler, errorHandler);

    useCaseHandler.execute(anyUseCase);

    verify(taskScheduler, only()).execute(any(UseCaseWrapper.class));
  }

  @Test public void testExecuteFailNotAnyUseCase() throws Exception {
    TaskScheduler taskScheduler = mock(TaskScheduler.class);
    NoUseCase noUseCase = new NoUseCase();
    ErrorHandler errorHandler = new ErrorHandler(new FakeCallbackScheduler());
    UseCaseHandler useCaseHandler = new UseCaseHandler(taskScheduler, errorHandler);

    boolean exception = false;
    try {
      useCaseHandler.execute(noUseCase);
    } catch (IllegalArgumentException e) {
      exception = true;
    }

    assertTrue(exception);
    verify(taskScheduler, never()).execute(any(UseCaseWrapper.class));
  }

  @Test public void testExecuteWithMethodName() throws Exception {
    TaskScheduler taskScheduler = mock(TaskScheduler.class);
    AnyUseCase anyUseCase = new AnyUseCase();
    ErrorHandler errorHandler = new ErrorHandler(new FakeCallbackScheduler());
    UseCaseHandler useCaseHandler = new UseCaseHandler(taskScheduler, errorHandler);
    UseCaseParams params = new UseCaseParams.Builder().useCaseName("anyExecution").build();

    useCaseHandler.execute(anyUseCase, params);

    verify(taskScheduler, only()).execute(any(UseCaseWrapper.class));
  }

  @Test public void testExecuteFailsWithWrongMethodName() throws Exception {
    TaskScheduler taskScheduler = mock(TaskScheduler.class);
    AnyUseCase anyUseCase = new AnyUseCase();
    ErrorHandler errorHandler = new ErrorHandler(new FakeCallbackScheduler());
    UseCaseHandler useCaseHandler = new UseCaseHandler(taskScheduler, errorHandler);
    UseCaseParams params = new UseCaseParams.Builder().useCaseName("noExistMethod").build();

    boolean error = false;
    try {
      useCaseHandler.execute(anyUseCase, params);
    } catch (IllegalArgumentException e) {
      error = true;
    }

    assertTrue(error);
    verify(taskScheduler, never()).execute(any(UseCaseWrapper.class));
  }

  @Test public void testExecuteWithArgs() throws Exception {
    TaskScheduler taskScheduler = mock(TaskScheduler.class);
    AnyUseCase anyUseCase = new AnyUseCase();
    ErrorHandler errorHandler = new ErrorHandler(new FakeCallbackScheduler());
    UseCaseHandler useCaseHandler = new UseCaseHandler(taskScheduler, errorHandler);
    String anyArg1 = "param1";
    int anyArg2 = 2;
    UseCaseParams paramsWithArgs = new UseCaseParams.Builder().args(anyArg1, anyArg2).build();

    useCaseHandler.execute(anyUseCase, paramsWithArgs);

    verify(taskScheduler, only()).execute(any(UseCaseWrapper.class));
  }

  @Test public void testExecuteAmbigous() throws Exception {
    TaskScheduler taskScheduler = mock(TaskScheduler.class);
    AmbiguousUseCase ambigousUseCase = new AmbiguousUseCase();
    ErrorHandler errorHandler = new ErrorHandler(new FakeCallbackScheduler());
    UseCaseHandler useCaseHandler = new UseCaseHandler(taskScheduler, errorHandler);

    boolean exception = false;
    try {
      UseCaseParams ambiguousParams = new UseCaseParams.Builder().args("anyString", 2).build();
      useCaseHandler.execute(ambigousUseCase, ambiguousParams);
    } catch (IllegalArgumentException e) {
      exception = true;
    }

    assertTrue(exception);
    verify(taskScheduler, never()).execute(any(UseCaseWrapper.class));
  }

  @Test public void testExecuteNoAmbigous() throws Exception {
    TaskScheduler taskScheduler = mock(TaskScheduler.class);
    AmbiguousUseCase ambigousUseCase = new AmbiguousUseCase();
    ErrorHandler errorHandler = new ErrorHandler(new FakeCallbackScheduler());
    UseCaseHandler useCaseHandler = new UseCaseHandler(taskScheduler, errorHandler);
    UseCaseParams ambiguousParams =
        new UseCaseParams.Builder().args("anyString", 2).useCaseName("method1").build();

    useCaseHandler.execute(ambigousUseCase, ambiguousParams);

    verify(taskScheduler, only()).execute(any(UseCaseWrapper.class));
  }

  @Test public void onSuccessCallbackShouldBeCalledWithSuccessArgs() {
    FakeTaskScheduler taskScheduler = new FakeTaskScheduler();
    EmptyResponseUseCase anyUseCase = new EmptyResponseUseCase();
    EmptyOnSuccess onSuccessCallback = new EmptyOnSuccess();
    ErrorHandler errorHandler = new ErrorHandler(new FakeCallbackScheduler());
    UseCaseHandler useCaseHandler = new UseCaseHandler(taskScheduler, errorHandler);
    UseCaseParams useCaseParams = new UseCaseParams.Builder().onSuccess(onSuccessCallback).build();

    useCaseHandler.execute(anyUseCase, useCaseParams);

    assertTrue(onSuccessCallback.isSuccess());
  }

  @Test public void completeCallbackShouldBeCalledWithSuccessArgs() {
    FakeTaskScheduler taskScheduler = new FakeTaskScheduler();
    AnyUseCase anyUseCase = new AnyUseCase();
    AnyOnSuccess onSuccessCallback = new AnyOnSuccess();
    ErrorHandler errorHandler = new ErrorHandler(new FakeCallbackScheduler());
    UseCaseHandler useCaseHandler = new UseCaseHandler(taskScheduler, errorHandler);
    UseCaseParams useCaseParams = new UseCaseParams.Builder().useCaseName("anyExecution")
        .onSuccess(onSuccessCallback)
        .build();

    useCaseHandler.execute(anyUseCase, useCaseParams);

    assertEquals(ANY_RETURN_VALUE, onSuccessCallback.getValue());
  }

  @Test public void onSuccessCallbackShouldBeCalledWithSuccessArgsAndDowncastingResponse() {
    FakeTaskScheduler taskScheduler = new FakeTaskScheduler();
    AnyUseCase anyUseCase = new AnyUseCase();
    AnyOnSuccessWithDowncast onSuccessCallback = new AnyOnSuccessWithDowncast();
    ErrorHandler errorHandler = new ErrorHandler(new FakeCallbackScheduler());
    UseCaseHandler useCaseHandler = new UseCaseHandler(taskScheduler, errorHandler);
    UseCaseParams useCaseParams = new UseCaseParams.Builder().useCaseName("downcastResponse")
        .onSuccess(onSuccessCallback)
        .build();

    useCaseHandler.execute(anyUseCase, useCaseParams);

    assertNotNull(onSuccessCallback.getValue());
  }

  @Test public void completeCallbackShouldNotBeExecuteWhenNotMatchArgs() {
    FakeTaskScheduler taskScheduler = new FakeTaskScheduler();
    AnyUseCase anyUseCase = new AnyUseCase();
    EmptyOnSuccess onSuccessCallback = new EmptyOnSuccess();
    ErrorHandler errorHandler = new ErrorHandler(new FakeCallbackScheduler());
    UseCaseHandler useCaseHandler = new UseCaseHandler(taskScheduler, errorHandler);
    UseCaseParams useCaseParams = new UseCaseParams.Builder().useCaseName("anyExecution")
        .onSuccess(onSuccessCallback)
        .build();

    useCaseHandler.execute(anyUseCase, useCaseParams);

    assertFalse(onSuccessCallback.isSuccess());
  }

  @Test public void shouldCallErrorOnErrorWhenUseCaseInvokeAnError() {
    FakeTaskScheduler taskScheduler = new FakeTaskScheduler();
    ErrorUseCase errorUseCase = new ErrorUseCase();
    ErrorHandler errorHandler = new ErrorHandler(new FakeCallbackScheduler());
    UseCaseHandler useCaseHandler = new UseCaseHandler(taskScheduler, errorHandler);
    OnErrorCallback errorCallback = spy(onErrorCallback);
    UseCaseParams useCaseParams =
        new UseCaseParams.Builder().useCaseName("customError").onError(errorCallback).build();

    useCaseHandler.execute(errorUseCase, useCaseParams);

    verify(errorCallback).onError(any(Error.class));
  }

  @Test public void shouldCallErrorHandlerWhenUseCaseInvokeAnErrorAndDontExistSpecificCallback() {
    FakeTaskScheduler taskScheduler = new FakeTaskScheduler();
    ErrorUseCase errorUseCase = new ErrorUseCase();
    ErrorHandler errorHandler = mock(ErrorHandler.class);
    UseCaseHandler useCaseHandler = new UseCaseHandler(taskScheduler, errorHandler);
    UseCaseParams useCaseParams = new UseCaseParams.Builder().useCaseName("customError").build();

    useCaseHandler.execute(errorUseCase, useCaseParams);

    verify(errorHandler).notifyError(any(ErrorNotHandledException.class));
  }

  @Test
  public void shouldCallErrorHandlerErrorWhenUseCaseInvokeAnErrorAndTheCallbackDoNotHandleThisKindOfMethod() {
    FakeTaskScheduler taskScheduler = new FakeTaskScheduler();
    ErrorUseCase errorUseCase = new ErrorUseCase();
    ErrorHandler errorHandler = mock(ErrorHandler.class);
    UseCaseHandler useCaseHandler = new UseCaseHandler(taskScheduler, errorHandler);
    UseCaseParams useCaseParams = new UseCaseParams.Builder().useCaseName("customError")
        .onError(specificErrorCallback)
        .build();

    useCaseHandler.execute(errorUseCase, useCaseParams);

    verify(errorHandler).notifyError(any(ErrorNotHandledException.class));
  }

  @Test public void shouldCallErrorHandlerErrorWhenUseCaseThrowsAnException() {
    FakeTaskScheduler taskScheduler = new FakeTaskScheduler();
    ErrorUseCase errorUseCase = new ErrorUseCase();
    ErrorHandler errorHandler = mock(ErrorHandler.class);
    UseCaseHandler useCaseHandler = new UseCaseHandler(taskScheduler, errorHandler);
    UseCaseParams useCaseParams =
        new UseCaseParams.Builder().useCaseName("launchException").build();

    useCaseHandler.execute(errorUseCase, useCaseParams);

    verify(errorHandler).notifyError(any(Exception.class));
  }

  private OnErrorCallback onErrorCallback = new OnErrorCallback<Error>() {

    @Override public void onError(Error error) {
    }
  };

  private OnErrorCallback specificErrorCallback = new OnErrorCallback<NetworkError>() {

    @Override public void onError(NetworkError error) {
    }
  };

  private class AnyOnSuccess implements OnSuccessCallback {
    private int value;

    @Success public void onSucess(int value) {
      this.value = value;
    }

    public int getValue() {
      return value;
    }
  }

  private class AnyOnSuccessWithDowncast implements OnSuccessCallback {
    private List<String> value;

    @Success public void onSucess(List<String> value) {
      this.value = value;
    }

    public List<String> getValue() {
      return value;
    }
  }

  private class EmptyOnSuccess implements OnSuccessCallback {
    private boolean success = false;

    @Success public void onSucess() {
      success = true;
    }

    public boolean isSuccess() {
      return success;
    }
  }

  private class AnyUseCase extends RosieUseCase {

    AnyUseCase() {
      setCallbackScheduler(new FakeCallbackScheduler());
    }

    @UseCase(name = "anyExecution") public void anyExecution() {
      notifySuccess(ANY_RETURN_VALUE);
    }

    @UseCase(name = "downcastResponse") public void anyExecutionDowncast() {
      notifySuccess(new ArrayList<String>());
    }

    @UseCase public void methodWithArgs(String arg1, int arg2) {

    }
  }

  private class EmptyUseCase extends RosieUseCase {

    EmptyUseCase() {
    }

    @UseCase public void anyExecution() {

    }
  }

  private class AmbiguousUseCase extends RosieUseCase {
    @UseCase(name = "method1") public void method1(String arg1, int arg2) {

    }

    @UseCase public void method2(String arg1, int arg2) {

    }
  }

  private class NoUseCase extends RosieUseCase {
  }

  private class EmptyResponseUseCase extends RosieUseCase {

    public EmptyResponseUseCase() {
      setCallbackScheduler(new FakeCallbackScheduler());
    }

    @UseCase public void method2() {
      notifySuccess();
    }
  }

  private class ErrorUseCase extends RosieUseCase {

    public ErrorUseCase() {
      setCallbackScheduler(new FakeCallbackScheduler());
    }

    @UseCase(name = "customError") public void errorMethod() throws Exception {
      notifyError(new Error("error network", new Exception()));
    }

    @UseCase(name = "launchException") public void launchExceptionMethod() throws Exception {
      throw new Exception("exception");
    }
  }
}