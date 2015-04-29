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

import com.karumi.rosie.domain.usercase.annotation.UserCase;
import org.junit.Test;

import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.only;
import static org.mockito.Mockito.verify;

public class UserCaseHandlerTest {

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

  class AnyUserCase {
    @UserCase(name = "anyExecution")
    public void anyExecution() {

    }

    @UserCase
    public void methodWithArgs(String arg1, int arg2) {

    }
  }

  class AmbigousUserCase {
    @UserCase(name = "method1")
    public void method1(String arg1, int arg2) {

    }

    @UserCase
    public void method2(String arg1, int arg2) {

    }
  }

  private class NoUserCase {
  }
}