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

/**
 * Created by flipper83 on 21/04/15.
 */
public class UserCaseParams {
  private String userCaseName = "";
  private Object[] args;

  private UserCaseParams(String userCaseName, Object[] args) {
    this.args = args;
    this.userCaseName = userCaseName;
  }

  String getUserCaseName() {
    return userCaseName;
  }

  Object[] getArgs() {
    return args;
  }

  public static class Builder {
    private String userCaseName = "";
    private Object[] args;

    public Builder name(String name) {
      userCaseName = name;
      return this;
    }

    public Builder args(Object... args) {
      this.args = args;
      return this;
    }

    public UserCaseParams build() {
      if (this.args == null) {
        args = new Object[0];
      }
      return new UserCaseParams(userCaseName, args);
    }
  }
}
