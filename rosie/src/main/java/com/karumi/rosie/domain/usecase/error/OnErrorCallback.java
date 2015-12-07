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

package com.karumi.rosie.domain.usecase.error;

/**
 * Callback invoked when an error happens inside of a use case.
 */
public interface OnErrorCallback<T extends Error> {
  /**
   * Method called when an error has occurred while executing a use case.
   *
   * @param error Error thrown by the use case
   * @return true if the error has been consumed, meaning it won't be notified to
   * other registered callbacks
   */
  boolean onError(T error);
}

