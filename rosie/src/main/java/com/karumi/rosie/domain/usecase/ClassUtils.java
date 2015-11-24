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
