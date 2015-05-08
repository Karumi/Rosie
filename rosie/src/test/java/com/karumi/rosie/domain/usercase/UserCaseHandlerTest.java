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
import com.karumi.rosie.domain.usercase.annotation.UserCase;
import com.karumi.rosie.domain.usercase.callback.OnSuccessCallback;
import com.karumi.rosie.domain.usercase.error.Error;
import com.karumi.rosie.domain.usercase.error.ErrorNotHandledException;
import com.karumi.rosie.domain.usercase.error.GlobalErrorDispatcher;
import com.karumi.rosie.domain.usercase.error.UseCaseErrorCallback;
import com.karumi.rosie.doubles.NetworkError;
import com.karumi.rosie.testutils.FakeScheduler;
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

public class UserCaseHandlerTest {

  private static final int ANY_RETURN_VALUE = 2;

  @Test
  public void testExecuteAnyObject() throws Exception {
    TaskScheduler taskScheduler = mock(TaskScheduler.class);
    EmptyUserCase anyUserCase = new EmptyUserCase();
    UserCaseHandler userCaseHandler = new UserCaseHandler(taskScheduler);

    userCaseHandler.execute(anyUserCase);

    verify(taskScheduler, only()).execute(any(UserCaseWrapper.class));
  }

  @Test
  public void testExecuteFailNotAnyUserCase() throws Exception {
    TaskScheduler taskScheduler = mock(TaskScheduler.class);
    NoUseCase noUserCase = new NoUseCase();
    UserCaseHandler userCaseHandler = new UserCaseHandler(taskScheduler);

    boolean exception = false;
    try {
      userCaseHandler.execute(noUserCase);
    } catch (IllegalArgumentException e) {
      exception = true;
    }

    assertTrue(exception);
    verify(taskScheduler, never()).execute(any(UserCaseWrapper.class));
  }

  @Test
  public void testExecuteWithMethodName() throws Exception {
    TaskScheduler taskScheduler = mock(TaskScheduler.class);
    AnyUserCase anyUserCase = new AnyUserCase();
    UserCaseHandler userCaseHandler = new UserCaseHandler(taskScheduler);
    UserCaseParams params = new UserCaseParams.Builder().useCaseName("anyExecution").build();

    userCaseHandler.execute(anyUserCase, params);

    verify(taskScheduler, only()).execute(any(UserCaseWrapper.class));
  }

  @Test
  public void testExecuteFailsWithWrongMethodName() throws Exception {
    TaskScheduler taskScheduler = mock(TaskScheduler.class);
    AnyUserCase anyUserCase = new AnyUserCase();
    UserCaseHandler userCaseHandler = new UserCaseHandler(taskScheduler);
    UserCaseParams params = new UserCaseParams.Builder().useCaseName("noExistMethod").build();

    boolean error = false;
    try {
      userCaseHandler.execute(anyUserCase, params);
    } catch (IllegalArgumentException e) {
      error = true;
    }

    assertTrue(error);
    verify(taskScheduler, never()).execute(any(UserCaseWrapper.class));
  }

  @Test
  public void testExecuteWithArgs() throws Exception {
    TaskScheduler taskScheduler = mock(TaskScheduler.class);
    AnyUserCase anyUserCase = new AnyUserCase();
    UserCaseHandler userCaseHandler = new UserCaseHandler(taskScheduler);
    String anyArg1 = "param1";
    int anyArg2 = 2;
    UserCaseParams paramsWithArgs = new UserCaseParams.Builder().args(anyArg1, anyArg2).build();

    userCaseHandler.execute(anyUserCase, paramsWithArgs);

    verify(taskScheduler, only()).execute(any(UserCaseWrapper.class));
  }

  @Test
  public void testExecuteAmbigous() throws Exception {
    TaskScheduler taskScheduler = mock(TaskScheduler.class);
    AmbiguousUseCase ambigousUserCase = new AmbiguousUseCase();
    UserCaseHandler userCaseHandler = new UserCaseHandler(taskScheduler);

    boolean exception = false;
    try {
      UserCaseParams ambiguousParams = new UserCaseParams.Builder().args("anyString", 2).build();
      userCaseHandler.execute(ambigousUserCase, ambiguousParams);
    } catch (IllegalArgumentException e) {
      exception = true;
    }

    assertTrue(exception);
    verify(taskScheduler, never()).execute(any(UserCaseWrapper.class));
  }

  @Test
  public void testExecuteNoAmbigous() throws Exception {
    TaskScheduler taskScheduler = mock(TaskScheduler.class);
    AmbiguousUseCase ambigousUserCase = new AmbiguousUseCase();
    UserCaseHandler userCaseHandler = new UserCaseHandler(taskScheduler);
    UserCaseParams ambiguousParams =
        new UserCaseParams.Builder().args("anyString", 2).useCaseName("method1").build();

    userCaseHandler.execute(ambigousUserCase, ambiguousParams);

    verify(taskScheduler, only()).execute(any(UserCaseWrapper.class));
  }

  @Test
  public void onSuccessCallbackShouldBeCalledWithSuccessArgs() {
    FakeScheduler taskScheduler = new FakeScheduler();
    EmptyResponseUseCase anyUserCase = new EmptyResponseUseCase();
    EmptyOnSuccess onSuccessCallback = new EmptyOnSuccess();
    UserCaseHandler userCaseHandler = new UserCaseHandler(taskScheduler);
    UserCaseParams userCaseParams =
        new UserCaseParams.Builder().onSuccess(onSuccessCallback).build();

    userCaseHandler.execute(anyUserCase, userCaseParams);

    assertTrue(onSuccessCallback.isSuccess());
  }

  @Test
  public void completeCallbackShouldBeCalledWithSuccessArgs() {
    FakeScheduler taskScheduler = new FakeScheduler();
    AnyUserCase anyUserCase = new AnyUserCase();
    AnyOnSuccess onSuccessCallback = new AnyOnSuccess();
    UserCaseHandler userCaseHandler = new UserCaseHandler(taskScheduler);
    UserCaseParams userCaseParams = new UserCaseParams.Builder().useCaseName("anyExecution")
        .onSuccess(onSuccessCallback)
        .build();

    userCaseHandler.execute(anyUserCase, userCaseParams);

    assertEquals(ANY_RETURN_VALUE, onSuccessCallback.getValue());
  }

  @Test
  public void onSuccessCallbackShouldBeCalledWithSuccessArgsAndDowncastingResponse() {
    FakeScheduler taskScheduler = new FakeScheduler();
    AnyUserCase anyUserCase = new AnyUserCase();
    AnyOnSuccessWithDowncast onSuccessCallback = new AnyOnSuccessWithDowncast();
    UserCaseHandler userCaseHandler = new UserCaseHandler(taskScheduler);
    UserCaseParams userCaseParams = new UserCaseParams.Builder().useCaseName("downcastResponse")
        .onSuccess(onSuccessCallback)
        .build();

    userCaseHandler.execute(anyUserCase, userCaseParams);

    assertNotNull(onSuccessCallback.getValue());
  }

  @Test
  public void completeCallbackShouldNotBeExecuteWhenNotMatchArgs() {
    FakeScheduler taskScheduler = new FakeScheduler();
    AnyUserCase anyUserCase = new AnyUserCase();
    EmptyOnSuccess onSuccessCallback = new EmptyOnSuccess();
    UserCaseHandler userCaseHandler = new UserCaseHandler(taskScheduler);
    UserCaseParams userCaseParams = new UserCaseParams.Builder().useCaseName("anyExecution")
        .onSuccess(onSuccessCallback)
        .build();

    userCaseHandler.execute(anyUserCase, userCaseParams);

    assertFalse(onSuccessCallback.isSuccess());
  }

  @Test
  public void shouldCallErrorOnErrorWhenUserCaseInvokeAnError() {
    FakeScheduler taskScheduler = new FakeScheduler();
    ErrorUseCase errorUserCase = new ErrorUseCase();
    UserCaseHandler userCaseHandler = new UserCaseHandler(taskScheduler);
    UseCaseErrorCallback errorCallback = spy(useCaseErrorCallback);
    UserCaseParams userCaseParams =
        new UserCaseParams.Builder().useCaseName("customError").onError(errorCallback).build();

    userCaseHandler.execute(errorUserCase, userCaseParams);

    verify(errorCallback).onError(any(Error.class));
  }

  @Test
  public void shouldCallErrorGenericErrorWhenUserCaseInvokeAnErrorAndDontExistSpecificCallback() {
    FakeScheduler taskScheduler = new FakeScheduler();
    ErrorUseCase errorUserCase = new ErrorUseCase();
    GlobalErrorDispatcher errorDispatcher = mock(GlobalErrorDispatcher.class);
    UserCaseHandler userCaseHandler = new UserCaseHandler(taskScheduler, errorDispatcher);
    UserCaseParams userCaseParams = new UserCaseParams.Builder().useCaseName("customError").build();

    userCaseHandler.execute(errorUserCase, userCaseParams);

    verify(errorDispatcher).notifyError(any(ErrorNotHandledException.class));
  }

  @Test
  public void
  shouldCallErrorGenericErrorWhenUserCaseInvokeAnErrorAndTheCallbackDontHandleThisKindOfMethod() {
    FakeScheduler taskScheduler = new FakeScheduler();
    ErrorUseCase errorUserCase = new ErrorUseCase();
    GlobalErrorDispatcher errorDispacher = mock(GlobalErrorDispatcher.class);
    UserCaseHandler userCaseHandler = new UserCaseHandler(taskScheduler, errorDispacher);
    UserCaseParams userCaseParams = new UserCaseParams.Builder().useCaseName("customError")
        .onError(specificErrorCallback)
        .build();

    userCaseHandler.execute(errorUserCase, userCaseParams);

    verify(errorDispacher).notifyError(any(ErrorNotHandledException.class));
  }

  @Test
  public void shouldCallErrorGenericErrorWhenUserCaseThrowsAnException() {
    FakeScheduler taskScheduler = new FakeScheduler();
    ErrorUseCase errorUserCase = new ErrorUseCase();
    GlobalErrorDispatcher errorDispacher = mock(GlobalErrorDispatcher.class);
    UserCaseHandler userCaseHandler = new UserCaseHandler(taskScheduler, errorDispacher);
    UserCaseParams userCaseParams =
        new UserCaseParams.Builder().useCaseName("launchException").build();

    userCaseHandler.execute(errorUserCase, userCaseParams);

    verify(errorDispacher).notifyError(any(Exception.class));
  }

  private UseCaseErrorCallback useCaseErrorCallback = new UseCaseErrorCallback<Error>() {

    @Override public void onError(Error error) {
    }
  };

  private UseCaseErrorCallback specificErrorCallback = new UseCaseErrorCallback<NetworkError>() {

    @Override public void onError(NetworkError error) {
    }
  };

  private class AnyOnSuccess implements OnSuccessCallback {
    private int value;

    @Success
    public void onSucess(int value) {
      this.value = value;
    }

    public int getValue() {
      return value;
    }
  }

  private class AnyOnSuccessWithDowncast implements OnSuccessCallback {
    private List<String> value;

    @Success
    public void onSucess(List<String> value) {
      this.value = value;
    }

    public List<String> getValue() {
      return value;
    }
  }

  private class EmptyOnSuccess implements OnSuccessCallback {
    private boolean success = false;

    @Success
    public void onSucess() {
      success = true;
    }

    public boolean isSuccess() {
      return success;
    }
  }

  private class AnyUserCase extends RosieUseCase {

    AnyUserCase() {
    }

    @UserCase(name = "anyExecution")
    public void anyExecution() {
      notifySuccess(ANY_RETURN_VALUE);
    }

    @UserCase(name = "downcastResponse")
    public void anyExecutionDowncast() {
      notifySuccess(new ArrayList<String>());
    }

    @UserCase
    public void methodWithArgs(String arg1, int arg2) {

    }
  }

  private class EmptyUserCase extends RosieUseCase {

    EmptyUserCase() {
    }

    @UserCase
    public void anyExecution() {

    }
  }

  private class AmbiguousUseCase extends RosieUseCase {
    @UserCase(name = "method1")
    public void method1(String arg1, int arg2) {

    }

    @UserCase
    public void method2(String arg1, int arg2) {

    }
  }

  private class NoUseCase extends RosieUseCase {
  }

  private class EmptyResponseUseCase extends RosieUseCase {
    @UserCase
    public void method2() {
      notifySuccess();
    }
  }

  private class ErrorUseCase extends RosieUseCase {
    @UserCase(name = "customError")
    public void errorMethod() throws Exception {
      notifyError(new Error("error network", new Exception()));
    }

    @UserCase(name = "launchException")
    public void launchExceptionMethod() throws Exception {
      throw new Exception("exception");
    }
  }
}