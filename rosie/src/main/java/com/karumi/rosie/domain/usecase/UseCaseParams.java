/*
 * Copyright (C) 2015 Karumi.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.karumi.rosie.domain.usecase;

import com.karumi.rosie.domain.usecase.callback.OnSuccessCallback;
import com.karumi.rosie.domain.usecase.error.OnErrorCallback;
import java.lang.ref.WeakReference;

/**
 * The params value to execute with a use case.
 */
final class UseCaseParams {

  private final String useCaseName;
  private final Object[] args;
  private WeakReference<OnSuccessCallback> onSuccessCallback;
  private WeakReference<OnErrorCallback> onErrorCallback;

  UseCaseParams(String useCaseName, Object[] args,
      OnSuccessCallback onSuccessCallback,
      OnErrorCallback onErrorCallback) {
    this.useCaseName = useCaseName;
    this.args = args;
    if (onSuccessCallback != null) {
      this.onSuccessCallback = new WeakReference<>(onSuccessCallback);
    }

    if (onErrorCallback != null) {
      this.onErrorCallback = new WeakReference<>(onErrorCallback);
    }
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
