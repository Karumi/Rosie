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

package com.karumi.rosie.view;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.LinkedList;
import java.util.List;

class RosieViewProxyGenerator<T> {

  private static final InvocationHandler EMPTY_HANDLER = new InvocationHandler() {
    @Override public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
      return null;
    }
  };

  /**
   * Generates a proxy instance for classes implementing RosiePresenter.View. The instance
   * generated uses a default handler so it will generate any side effect when invoking this
   * instance but it will be equivalent from the type point of view.
   */
  T generate(Object view) {
    final Class[] viewClasses = getViewInterfaceClasses(view);
    ClassLoader classLoader = viewClasses[0].getClassLoader();
    return (T) Proxy.newProxyInstance(classLoader, viewClasses, EMPTY_HANDLER);
  }

  private Class<?>[] getViewInterfaceClasses(Object view) {
    List<Class<?>> interfaceClasses = new LinkedList<>();
    Class<?>[] interfaces = view.getClass().getInterfaces();
    for (int i = 0; i < interfaces.length; i++) {
      Class<?> interfaceCandidate = interfaces[i];
      if (RosiePresenter.View.class.isAssignableFrom(interfaceCandidate)) {
        interfaceClasses.add(interfaceCandidate);
      }
    }
    return interfaceClasses.toArray(new Class[interfaceClasses.size()]);
  }
}
