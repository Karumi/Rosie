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

import com.karumi.rosie.domain.usercase.callback.OnSuccessCallback;
import com.karumi.rosie.domain.usercase.error.UseCaseErrorCallback;

/**
 * The params value to execute with the user case.
 */
public class UserCaseParams {
  private final OnSuccessCallback onSuccessCallback;
  private final String useCaseName;
  private final Object[] args;
  private final UseCaseErrorCallback errorCallback;

  public UserCaseParams(String useCaseName, Object[] args, OnSuccessCallback onSuccess,
      UseCaseErrorCallback errorCallback) {
    this.args = args;
    this.useCaseName = useCaseName;
    this.onSuccessCallback = onSuccess;
    this.errorCallback = errorCallback;
  }

  String getUseCaseName() {
    return useCaseName;
  }

  Object[] getArgs() {
    return args;
  }

  public OnSuccessCallback getOnSuccessCallback() {
    return onSuccessCallback;
  }

  public UseCaseErrorCallback getErrorCallback() {
    return errorCallback;
  }

  public static class Builder {
    private final static OnSuccessCallback EMPTY_SUCESS = new OnSuccessCallback() {
    };

    private String useCaseName = "";
    private Object[] args;
    private OnSuccessCallback onSuccess = EMPTY_SUCESS;
    private UseCaseErrorCallback errorCallback;

    public Builder useCaseName(String name) {
      useCaseName = name;
      return this;
    }

    public Builder args(Object... args) {
      this.args = args;
      return this;
    }

    public Builder onSuccess(OnSuccessCallback onSuccessCallback) {
      if (onSuccessCallback == null) {
        throw new IllegalArgumentException(
            "OnSuccessCallback is null. You can not invoke it with" + " null callback.");
      }

      this.onSuccess = onSuccessCallback;
      return this;
    }

    public Builder onError(UseCaseErrorCallback errorCallback) {
      if (errorCallback == null) {
        throw new IllegalArgumentException(
            "The errorCallback used is null, you can't use a null instance as onError callback.");
      }
      this.errorCallback = errorCallback;
      return this;
    }

    public UserCaseParams build() {
      if (this.args == null) {
        args = new Object[0];
      }
      return new UserCaseParams(useCaseName, args, onSuccess, errorCallback);
    }
  }
}
