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

package com.karumi.rosie.doubles;

import com.karumi.rosie.repository.datasource.Identifiable;

public class AnyRepositoryValue implements Identifiable<AnyRepositoryKey> {

  private final AnyRepositoryKey key;

  public AnyRepositoryValue(AnyRepositoryKey key) {
    this.key = key;
  }

  @Override public AnyRepositoryKey getKey() {
    return key;
  }

  @Override public boolean equals(Object o) {
    if (this == o) {
      return true;
    }

    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    AnyRepositoryValue that = (AnyRepositoryValue) o;

    return !(key != null ? !key.equals(that.key) : that.key != null);
  }

  @Override public int hashCode() {
    return key != null ? key.hashCode() : 0;
  }
}
