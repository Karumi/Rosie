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
import com.karumi.rosie.testutils.TestScheduler;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.only;
import static org.mockito.Mockito.verify;

public class UserCaseHandlerTest {

  private static final int ANY_RETURN_VALUE = 2;

  @Test
  public void testExecuteAnyObject() throws Exception {

    TaskScheduler taskScheduler = mock(TaskScheduler.class);
    AnyUserCase anyUserCase = new AnyUserCase();

    UserCaseHandler userCaseHandler = new UserCaseHandler(taskScheduler);

    userCaseHandler.execute(anyUserCase);

    verify(taskScheduler, only()).execute(any(UserCaseWrapper.class));
  }

  @Test
  public void testExecuteFailNotAnyUserCase() throws Exception {
    TaskScheduler taskScheduler = mock(TaskScheduler.class);
    NoUserCase noUserCase = new NoUserCase();

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

    UserCaseParams params = new UserCaseParams.Builder().name("anyExecution").build();

    userCaseHandler.execute(anyUserCase, params);

    verify(taskScheduler, only()).execute(any(UserCaseWrapper.class));
  }

  @Test
  public void testExecuteFailsWithWrongMethodName() throws Exception {
    TaskScheduler taskScheduler = mock(TaskScheduler.class);
    AnyUserCase anyUserCase = new AnyUserCase();
    UserCaseHandler userCaseHandler = new UserCaseHandler(taskScheduler);

    UserCaseParams params = new UserCaseParams.Builder().name("noExistMethod").build();

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
    AmbigousUserCase ambigousUserCase = new AmbigousUserCase();

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
    AmbigousUserCase ambigousUserCase = new AmbigousUserCase();

    UserCaseHandler userCaseHandler = new UserCaseHandler(taskScheduler);

    UserCaseParams ambiguousParams =
        new UserCaseParams.Builder().args("anyString", 2).name("method1").build();
    userCaseHandler.execute(ambigousUserCase, ambiguousParams);

    verify(taskScheduler, only()).execute(any(UserCaseWrapper.class));
  }

  @Test
  public void completeCallbackShouldBeCalledWithoutArgs() {
    TestScheduler taskScheduler = new TestScheduler();
    EmptyResponseUserCase anyUserCase = new EmptyResponseUserCase();

    EmptyOnSuccess onSuccessCallback = new EmptyOnSuccess();

    UserCaseHandler userCaseHandler = new UserCaseHandler(taskScheduler);

    UserCaseParams userCaseParams =
        new UserCaseParams.Builder().onSuccess(onSuccessCallback).build();

    userCaseHandler.execute(anyUserCase, userCaseParams);

    assertTrue(onSuccessCallback.isSuccess());
  }

  @Test
  public void completeCallbackShouldBeCalledWithSuccessArgs() {
    TestScheduler taskScheduler = new TestScheduler();
    AnyUserCase anyUserCase = new AnyUserCase();

    AnyOnSuccess onSuccessCallback = new AnyOnSuccess();

    UserCaseHandler userCaseHandler = new UserCaseHandler(taskScheduler);

    UserCaseParams userCaseParams =
        new UserCaseParams.Builder().onSuccess(onSuccessCallback).build();

    userCaseHandler.execute(anyUserCase, userCaseParams);

    assertEquals(ANY_RETURN_VALUE, onSuccessCallback.getValue());
  }

  @Test
  public void completeCallbackShouldNotBeExecuteWhenNotMatchArgs() {
    TestScheduler taskScheduler = new TestScheduler();
    AnyUserCase anyUserCase = new AnyUserCase();

    EmptyOnSuccess onSuccessCallback = new EmptyOnSuccess();

    UserCaseHandler userCaseHandler = new UserCaseHandler(taskScheduler);

    UserCaseParams userCaseParams =
        new UserCaseParams.Builder().onSuccess(onSuccessCallback).build();

    userCaseHandler.execute(anyUserCase, userCaseParams);

    assertFalse(onSuccessCallback.isSuccess());
  }

  class AnyOnSuccess implements OnSuccessCallback {
    private int value;

    @Success
    public void onSucess(int value) {
      this.value = value;
    }

    public int getValue() {
      return value;
    }
  }

  class EmptyOnSuccess implements OnSuccessCallback {
    private boolean success = false;

    @Success
    public void onSucess() {
      success = true;
    }

    public boolean isSuccess() {
      return success;
    }
  }

  class AnyUserCase extends RosieUseCase {

    AnyUserCase() {
    }

    @UserCase(name = "anyExecution")
    public void anyExecution() {
      notifySuccesss(ANY_RETURN_VALUE);
    }

    @UserCase
    public void methodWithArgs(String arg1, int arg2) {

    }
  }

  class AmbigousUserCase extends RosieUseCase {
    @UserCase(name = "method1")
    public void method1(String arg1, int arg2) {

    }

    @UserCase
    public void method2(String arg1, int arg2) {

    }
  }

  private class NoUserCase extends RosieUseCase {
  }

  private class EmptyResponseUserCase extends RosieUseCase {
    @UserCase
    public void method2() {
      notifySuccesss();
    }
  }
}