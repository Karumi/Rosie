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

package com.karumi.rosie.mapper;

import java.util.ArrayList;
import java.util.Collection;

public abstract class Mapper<T1, T2> {
  public abstract T2 map(T1 value);
  public abstract T1 reverseMap(T2 value);

  public Collection<T2> map(Collection<T1> values) {
    Collection<T2> returnValues = new ArrayList<>(values.size());
    for (T1 value : values) {
      returnValues.add(map(value));
    }
    return returnValues;
  }

  public Collection<T1> reverseMap(Collection<T2> values) {
    Collection<T1> returnValues = new ArrayList<>(values.size());
    for (T2 value : values) {
      returnValues.add(reverseMap(value));
    }
    return returnValues;
  }
}
