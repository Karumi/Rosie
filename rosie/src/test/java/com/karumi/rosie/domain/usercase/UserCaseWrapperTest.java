package com.karumi.rosie.domain.usercase;

import com.karumi.rosie.domain.usercase.annotation.UserCase;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class UserCaseWrapperTest {

  @Test
  public void testExecuteWithArgs() throws Exception {
    //AnyUserCase can be a mock because mockito remove the annotation, for this reason I'm going to
    // do a state test, with the values. I open to future options.
    AnyUserCase anyUserCase = new AnyUserCase();
    UserCaseParams argsParams = new UserCaseParams.Builder().args("anyValue", 2).build();

    UserCaseWrapper userCaseWrapper = new UserCaseWrapper(anyUserCase, argsParams);
    
    assertEquals("anyValue",anyUserCase.getArg1());
    assertEquals(2,anyUserCase.getArg2());

  }

  class AnyUserCase {

    private String arg1;
    private int arg2;

    AnyUserCase() {
    }

    @UserCase
    public void methodWithArgs(String arg1, int arg2) {
      this.arg1 = arg1;
      this.arg2 = arg2;
    }

    private String getArg1() {
      return arg1;
    }

    private int getArg2() {
      return arg2;
    }
  }
}