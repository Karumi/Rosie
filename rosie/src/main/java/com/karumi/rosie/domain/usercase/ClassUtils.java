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

import java.util.HashMap;

/**
 * A set of helper methods related to class comparation.
 */
final class ClassUtils {

  private static HashMap<Class, Class> primitiveWrappers = new HashMap<Class, Class>();

  static {
    primitiveWrappers.put(Boolean.class, boolean.class);
    primitiveWrappers.put(Byte.class, byte.class);
    primitiveWrappers.put(Character.class, char.class);
    primitiveWrappers.put(Short.class, short.class);
    primitiveWrappers.put(Integer.class, int.class);
    primitiveWrappers.put(Long.class, long.class);
    primitiveWrappers.put(Float.class, float.class);
    primitiveWrappers.put(Double.class, double.class);
    primitiveWrappers.put(Void.class, void.class);
  }

  public static boolean canAssign(Class<?> baseClass, Class<?> classToCheck) {
    if (baseClass.isAssignableFrom(classToCheck)) {
      return true;
    } else {
      if (baseClass.isPrimitive()) {
        Class primitizedClass = primitiveWrappers.get(classToCheck);
        if (primitizedClass != null) {
          return baseClass.isAssignableFrom(primitizedClass);
        }
      } else if (classToCheck.isPrimitive()) {
        Class primitizedClass = primitiveWrappers.get(baseClass);
        if (primitizedClass != null) {
          return primitizedClass.isAssignableFrom(classToCheck);
        }
      }
    }

    return false;
  }
}
