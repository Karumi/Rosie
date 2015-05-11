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

import com.karumi.rosie.domain.usecase.annotation.UseCase;
import com.karumi.rosie.domain.usecase.error.MethodNotFoundException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Filters a UseCase instance based on the class structure to find the UseCase method configured to
 * be executed.
 */
class UseCaseFilter {

  static Method filter(Object useCase, UseCaseParams useCaseParams) {
    List<Method> methodsFiltered = getAnnotatedUseCaseMethods(useCase);
    if (methodsFiltered.isEmpty()) {
      throw new IllegalArgumentException("This object doesn't contain any use case to execute."
          + "Do you forget add the @UseCase annotation?");
    }

    methodsFiltered = getMethodMatchingName(useCaseParams, methodsFiltered);
    if (methodsFiltered.isEmpty()) {
      throw new IllegalArgumentException("The target doesn't contains any use case with this name."
          + "Do you forget add the @UseCase annotation?");
    }

    methodsFiltered = getMethodMatchingArguments(useCaseParams, methodsFiltered);
    if (methodsFiltered.isEmpty()) {
      throw new IllegalArgumentException(
          "The target doesn't contains any use case with those args. "
              + "Do you forget add the @UseCase annotation?");
    }

    if (methodsFiltered.size() > 1) {
      throw new IllegalArgumentException(
          "The target contains more than one use cases with the same signature. "
              + "You can use 'name' property in @UseCase and invoke with param name");
    }

    return methodsFiltered.get(0);
  }

  private static List<Method> getAnnotatedUseCaseMethods(Object target) {
    List<Method> useCaseMethods = new ArrayList<>();

    Method[] methods = target.getClass().getMethods();
    for (Method method : methods) {
      UseCase useCaseMethod =
          method.getAnnotation(UseCase.class);

      if (useCaseMethod != null) {
        useCaseMethods.add(method);
      }
    }
    return useCaseMethods;
  }

  private static List<Method> getMethodMatchingArguments(UseCaseParams useCaseParams,
      List<Method> methodsFiltered) {

    Object[] selectedArgs = useCaseParams.getArgs();

    Iterator<Method> iteratorMethods = methodsFiltered.iterator();

    while (iteratorMethods.hasNext()) {
      Method method = iteratorMethods.next();

      Class<?>[] parameters = method.getParameterTypes();
      if (parameters.length == selectedArgs.length) {
        if (!hasValidArguments(parameters, selectedArgs)) {
          iteratorMethods.remove();
        }
      } else {
        iteratorMethods.remove();
      }
    }

    return methodsFiltered;
  }

  private static boolean hasValidArguments(Class<?>[] parameters, Object[] selectedArgs) {
    for (int i = 0; i < parameters.length; i++) {
      Class<?> targetClass = selectedArgs[i].getClass();
      Class<?> parameterClass = parameters[i];
      if (!ClassUtils.canAssign(targetClass, parameterClass)) {
        return false;
      }
    }
    return true;
  }

  private static boolean hasValidArgumentsForReturn(Class<?>[] parameters, Object[] selectedArgs) {
    for (int i = 0; i < parameters.length; i++) {
      Class<?> targetClass = selectedArgs[i].getClass();
      Class<?> parameterClass = parameters[i];
      if (!ClassUtils.canAssign(parameterClass, targetClass)) {
        return false;
      }
    }
    return true;
  }

  private static List<Method> getMethodMatchingName(UseCaseParams useCaseParams,
      List<Method> methodsFiltered) {
    String nameUseCase = useCaseParams.getUseCaseName();
    if (nameUseCase == null || nameUseCase.equals("")) {
      return methodsFiltered;
    }

    Iterator<Method> iteratorMethods = methodsFiltered.iterator();
    while (iteratorMethods.hasNext()) {
      Method method = iteratorMethods.next();
      UseCase annotation = method.getAnnotation(UseCase.class);
      if (!(annotation.name().equals(nameUseCase))) {
        iteratorMethods.remove();
      }
    }

    return methodsFiltered;
  }

  static Method filterValidMethodArgs(Object[] argsToSend, Method[] methods, Class typeAnnotation) {

    List<Method> methodsFiltered = new ArrayList<>();

    for (Method method : methods) {
      Annotation annotationValid = method.getAnnotation(typeAnnotation);
      if (annotationValid != null) {
        Class<?>[] parameters = method.getParameterTypes();
        if (parameters.length == argsToSend.length) {
          if (hasValidArgumentsForReturn(parameters, argsToSend)) {
            methodsFiltered.add(method);
          }
        }
      }
    }

    if (methodsFiltered.isEmpty()) {
      throw new MethodNotFoundException("Not exist any method on this success with this signature");
    }

    if (methodsFiltered.size() > 1) {
      throw new RuntimeException(
          "This success has more than one method with this signature. Remove the ambiguity.");
    }

    return methodsFiltered.get(0);
  }
}
