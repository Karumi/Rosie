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

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.HashSet;
import java.util.Set;

/**
 * Analyzes an Activity or Fragment with Presenter annotations passed as parameter to obtain a list
 * of Presenter instances to be linked to the source lifecycle.
 */
public final class PresenterLifeCycleLinker {

  private final Set<RosiePresenter> presenters = new HashSet<>();

  public void addAnnotatedPresenter(Field[] declaredFields, Object source) {
    for (Field field : declaredFields) {
      if (field.isAnnotationPresent(Presenter.class)) {
        if (Modifier.isPrivate(field.getModifiers())) {
          throw new RuntimeException(
              "Presenter must be accessible for this class. The visibility modifier used can't be"
                  + " private");
        } else {
          try {
            field.setAccessible(true);
            RosiePresenter presenter = (RosiePresenter) field.get(source);
            presenters.add(presenter);
            field.setAccessible(false);
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

  public void setView(RosiePresenter.View view) {
    for (RosiePresenter presenter : presenters) {
      presenter.setView(view);
    }
  }

  public void initializePresenters() {
    for (RosiePresenter presenter : presenters) {
      presenter.initialize();
    }
  }

  public void updatePresenters() {
    for (RosiePresenter presenter : presenters) {
      presenter.update();
    }
  }

  public void pausePresenters() {
    for (RosiePresenter presenter : presenters) {
      presenter.pause();
      presenter.resetView();
    }
  }

  public void destroyPresenters() {
    for (RosiePresenter presenter : presenters) {
      presenter.destroy();
    }
  }

  public void registerPresenter(RosiePresenter presenter) {
    presenters.add(presenter);
  }
}
