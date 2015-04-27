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