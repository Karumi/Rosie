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

package com.karumi.rosie.view.presenter;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.HashSet;
import java.util.Set;

/**
 * This class give access to the presenters and unify the access to this one.
 */
public class PresenterLifeCycleHooker {
  Set<Presenter> presenters = new HashSet<>();

  public void addPresentedAnnotated(Field[] declaredFields, Object source) {
    for (Field field : declaredFields) {
      if (field.isAnnotationPresent(com.karumi.rosie.view.presenter.annotation.Presenter.class)) {
        if (!Modifier.isPublic(field.getModifiers())) {
          throw new RuntimeException(
              "Presenter must be accessible for this class. Change visibility to public");

        } else {
          try {
            Presenter presenter = (Presenter) field.get(source);
            presenters.add(presenter);
          } catch (IllegalAccessException e) {
            IllegalStateException runtimeException = new IllegalStateException(
                "the presenter " + field.getName() + " can not be access");
            runtimeException.initCause(e);
            throw runtimeException;
          }
        }
      }
    }
  }

  public void presentersInitialize() {
    for (Presenter presenter : presenters) {
      presenter.initialize();
    }
  }

  public void presentersUpdate() {
    for (Presenter presenter : presenters) {
      presenter.update();
    }
  }

  public void presentersPause() {
    for (Presenter presenter : presenters) {
      presenter.pause();
    }
  }

  public void presentersDestroy() {
    for (Presenter presenter : presenters) {
      presenter.destroy();
    }
  }

  public void registerPresenter(com.karumi.rosie.view.presenter.Presenter presenter) {
    //this method doesn't override the presenter
    presenters.add(presenter);
  }
}
