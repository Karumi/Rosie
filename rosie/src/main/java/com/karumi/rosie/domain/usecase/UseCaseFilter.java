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
          + "Did you forget to add the @UseCase annotation?");
    }

    methodsFiltered = getMethodMatchingName(useCaseParams, methodsFiltered);
    if (methodsFiltered.isEmpty()) {
      throw new IllegalArgumentException("The target doesn't contain any use case with this name."
          + "Did you forget to add the @UseCase annotation?");
    }

    methodsFiltered = getMethodMatchingArguments(useCaseParams, methodsFiltered);
    if (methodsFiltered.isEmpty()) {
      throw new IllegalArgumentException("The target doesn't contain any use case with those args. "
          + "Did you forget to add the @UseCase annotation?");
    }

    if (methodsFiltered.size() > 1) {
      throw new IllegalArgumentException(
          "The target contains more than one use case with the same signature. "
              + "You can use the 'name' property in @UseCase and invoke it with a param name");
    }

    return methodsFiltered.get(0);
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
      throw new MethodNotFoundException(
          "No public @Success-annotated method exists with this signature");
    }

    if (methodsFiltered.size() > 1) {
      throw new RuntimeException(
          "This success has more than one method with this signature. Remove the ambiguity.");
    }

    return methodsFiltered.get(0);
  }

  private static List<Method> getAnnotatedUseCaseMethods(Object target) {
    List<Method> useCaseMethods = new ArrayList<>();

    Method[] methods = target.getClass().getMethods();
    for (Method method : methods) {
      UseCase useCaseMethod = method.getAnnotation(UseCase.class);

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
      Object argument = selectedArgs[i];
      if (argument != null) {
        Class<?> targetClass = argument.getClass();
        Class<?> parameterClass = parameters[i];
        if (!ClassUtils.canAssign(targetClass, parameterClass)) {
          return false;
        }
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
}
