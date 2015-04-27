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

import static org.junit.Assert.assertEquals;

public class UserCaseWrapperTest {

  @Test
  public void testExecuteWithArgs() throws Exception {
    //AnyUserCase can be a mock because mockito remove the annotation, for this reason I'm going to
    // do a state test, with the values. I open to future options.
    AnyUserCase anyUserCase = new AnyUserCase();
    UserCaseParams argsParams = new UserCaseParams.Builder().args("anyValue", 2).build();

    UserCaseWrapper userCaseWrapper = new UserCaseWrapper(anyUserCase, argsParams);

    assertEquals("anyValue", anyUserCase.getArg1());
    assertEquals(2, anyUserCase.getArg2());
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