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

import java.util.HashMap;

/**
 * A set of helper methods related to class comparison.
 */
final class ClassUtils {

  private static final HashMap<Class, Class> PRIMITIVE_WRAPPERS = new HashMap<>();

  static {
    PRIMITIVE_WRAPPERS.put(Boolean.class, boolean.class);
    PRIMITIVE_WRAPPERS.put(Byte.class, byte.class);
    PRIMITIVE_WRAPPERS.put(Character.class, char.class);
    PRIMITIVE_WRAPPERS.put(Short.class, short.class);
    PRIMITIVE_WRAPPERS.put(Integer.class, int.class);
    PRIMITIVE_WRAPPERS.put(Long.class, long.class);
    PRIMITIVE_WRAPPERS.put(Float.class, float.class);
    PRIMITIVE_WRAPPERS.put(Double.class, double.class);
    PRIMITIVE_WRAPPERS.put(Void.class, void.class);
  }

  static boolean canAssign(Class<?> baseClass, Class<?> classToCheck) {
    if (baseClass.isAssignableFrom(classToCheck)) {
      return true;
    } else {
      if (baseClass.isPrimitive()) {
        Class primitiveClass = PRIMITIVE_WRAPPERS.get(classToCheck);
        if (primitiveClass != null) {
          return baseClass.isAssignableFrom(primitiveClass);
        }
      } else if (classToCheck.isPrimitive()) {
        Class primitiveClass = PRIMITIVE_WRAPPERS.get(baseClass);
        if (primitiveClass != null) {
          return primitiveClass.isAssignableFrom(classToCheck);
        }
      }
    }

    return false;
  }
}
