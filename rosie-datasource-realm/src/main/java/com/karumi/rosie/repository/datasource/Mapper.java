/*
 *  Copyright (C) 2015 Karumi.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package com.karumi.rosie.repository.datasource;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public abstract class Mapper<V1, V2> {
  public abstract V2 map(V1 value);

  public abstract V1 reverseMap(V2 value2);

  public final Collection<V2> map(Collection<V1> values) {
    List<V2> valuesV2 = new ArrayList<>();
    for (V1 value : values) {
      V2 valueV2 = map(value);
      valuesV2.add(valueV2);
    }
    return valuesV2;
  }

  public final Collection<V1> reverseMap(Collection<V2> valuesV2) {
    List<V1> values = new ArrayList<>();
    for (V2 valueV2 : valuesV2) {
      V1 value = reverseMap(valueV2);
      values.add(value);
    }
    return values;
  }
}
