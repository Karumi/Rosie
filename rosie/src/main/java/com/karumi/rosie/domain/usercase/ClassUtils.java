package com.karumi.rosie.domain.usercase;

import java.util.HashMap;

/**
 *
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
