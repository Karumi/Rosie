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

import com.karumi.rosie.domain.usercase.annotation.Success;
import com.karumi.rosie.domain.usercase.callback.OnSuccessCallback;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * This is the base implementation of user case, all user cases must inherate from this one.
 */
public class RosieUseCase {
  private OnSuccessCallback onSuccess;

  protected void notifySuccesss(Object... values) {

    Method[] methodsArray = onSuccess.getClass().getMethods();
    if (methodsArray.length > 0) {
      List<Method> methodsToInvoke = filterValidMethodArgs(values, methodsArray, Success.class);

      if (methodsToInvoke.isEmpty()) {
        throw new RuntimeException("Not exist any method on this success with this signature");
      }

      if (methodsToInvoke.size() > 1) {
        throw new RuntimeException(
            "This success has more than one method with this signature." + "Remove the ambiguity.");
      }

      Method methodToInvoke = methodsToInvoke.get(0);

      try {
        methodToInvoke.invoke(onSuccess, values);
      } catch (Exception e) {
        throw new RuntimeException("internal error invoking the sucess object", e);
      }
    }
  }

  void setOnSuccess(OnSuccessCallback onSuccess) {
    this.onSuccess = onSuccess;
  }

  private List<Method> filterValidMethodArgs(Object[] argsToSend, Method[] methods,
      Class typeAnnotation) {

    List<Method> methodsFiltered = new ArrayList<>();

    for (Method method : methods) {
      Annotation annotationValid = method.getAnnotation(typeAnnotation);
      if (annotationValid != null) {
        Class<?>[] parameters = method.getParameterTypes();
        if (parameters.length == argsToSend.length) {
          if (isValidArguments(parameters, argsToSend)) {
            methodsFiltered.add(method);
          }
        }
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
}
