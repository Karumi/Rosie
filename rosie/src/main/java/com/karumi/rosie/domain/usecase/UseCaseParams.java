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

import com.karumi.rosie.domain.usecase.callback.OnSuccessCallback;
import com.karumi.rosie.domain.usecase.error.OnErrorCallback;
import java.lang.ref.WeakReference;

/**
 * The params value to execute with a use case.
 */
public final class UseCaseParams {

  private final String useCaseName;
  private final Object[] args;
  private final WeakReference<OnSuccessCallback> onSuccessCallback;
  private final WeakReference<OnErrorCallback> onErrorCallback;

  UseCaseParams(String useCaseName, Object[] args,
      WeakReference<OnSuccessCallback> onSuccessCallback,
      WeakReference<OnErrorCallback> onErrorCallback) {
    this.useCaseName = useCaseName;
    this.args = args;
    this.onSuccessCallback = onSuccessCallback;
    this.onErrorCallback = onErrorCallback;
  }

  public OnSuccessCallback getOnSuccessCallback() {
    return onSuccessCallback != null ? onSuccessCallback.get() : null;
  }

  public OnErrorCallback getOnErrorCallback() {
    return onErrorCallback != null ? onErrorCallback.get() : null;
  }

  String getUseCaseName() {
    return useCaseName;
  }

  Object[] getArgs() {
    return args;
  }
}
