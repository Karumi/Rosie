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

import com.karumi.rosie.domain.usercase.annotation.UserCase;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * This class filter an usercase based on the params. If something was wrong throws and error.
 */
class UserCaseFilter {

  public Method filter(Object userCase, UserCaseParams userCaseParams) {
    List<Method> methodsFiltered = filterValidUserCases(userCase);
    if (methodsFiltered.isEmpty()) {
      throw new IllegalArgumentException("This object doesn't contain any user case to execute."
          + "Do you forget add the @UserCase annotation?");
    }

    methodsFiltered = filterValidUserCaseName(userCaseParams, methodsFiltered);
    if (methodsFiltered.isEmpty()) {
      throw new IllegalArgumentException("the target doesn't contains any usercase with this name."
          + "Do you forget add the @UserCase annotation?");
    }

    methodsFiltered = filterValidUserCaseArgs(userCaseParams, methodsFiltered);
    if (methodsFiltered.isEmpty()) {
      throw new IllegalArgumentException(
          "the target doesn't contains any usercase with those args. "
              + "Do you forget add the @UserCase annotation?");
    }



    if (methodsFiltered.size() > 1) {
      throw new IllegalArgumentException(
          "the target contains more than one usercases with the same signature. "
              + "You can use 'name' property in @UserCase and invoke with param name");
    }

    return methodsFiltered.get(0);
  }

  private List<Method> filterValidUserCases(Object target) {
    List<Method> userCaseMethods = new ArrayList<>();

    Method[] methods = target.getClass().getMethods();
    for (Method method : methods) {
      com.karumi.rosie.domain.usercase.annotation.UserCase userCaseMethod =
          method.getAnnotation(com.karumi.rosie.domain.usercase.annotation.UserCase.class);

      if (userCaseMethod != null) {
        userCaseMethods.add(method);
      }
    }
    return userCaseMethods;
  }

  private List<Method> filterValidUserCaseArgs(UserCaseParams userCaseParams,
      List<Method> methodsFiltered) {

    Object[] selectedArgs = userCaseParams.getArgs();

    Iterator<Method> iteratorMethods = methodsFiltered.iterator();

    while (iteratorMethods.hasNext()) {
      Method method = iteratorMethods.next();

      Class<?>[] parameters = method.getParameterTypes();
      if (parameters.length == selectedArgs.length) {
        if (!isValidArguments(parameters, selectedArgs)) {
          iteratorMethods.remove();
        }
      } else {
        iteratorMethods.remove();
      }
    }

    return methodsFiltered;
  }

  private boolean isValidArguments(Class<?>[] parameters, Object[] selectedArgs) {
    for (int i = 0; i < parameters.length; i++) {
      Class<?> targetClass = selectedArgs[i].getClass();
      Class<?> parameterClass = parameters[i];
      if (!ClassUtils.canAssign(targetClass, parameterClass)) {
        return false;
      }
    }
    return true;
  }

  private List<Method> filterValidUserCaseName(UserCaseParams userCaseParams,
      List<Method> methodsFiltered) {
    String nameUserCase = userCaseParams.getUserCaseName();
    if (nameUserCase == null || nameUserCase.equals("")) {
      return methodsFiltered;
    }

    Iterator<Method> iteratorMethods = methodsFiltered.iterator();
    while (iteratorMethods.hasNext()) {
      Method method = iteratorMethods.next();
      UserCase annotation = method.getAnnotation(UserCase.class);
      if (!(annotation.name().equals(nameUserCase))) {
        iteratorMethods.remove();
      }
    }

    return methodsFiltered;
  }
}
