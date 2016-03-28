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

  /**
   * Obtains all the presenter instances declared in the source param, configures the associated
   * view and initializes the presenters lifecycle.
   *
   * @param source used to obtain the presenter references.
   * @param view to be configured as presenters view.
   */
  public void initializeLifeCycle(Object source, RosiePresenter.View view) {
    if (source == null) {
      throw new IllegalArgumentException(
          "The source instance used to initialize the presenters can't be null");
    }
    if (view == null) {
      throw new IllegalArgumentException(
          "The view instance used to initialize the presenters can't be null");
    }

    addAnnotatedPresenter(source);
    setView(view);
    initializePresenters();
  }

  /**
   * Initializes all the already registered presenters lifecycle.
   */
  public void initializePresenters() {
    for (RosiePresenter presenter : presenters) {
      presenter.initialize();
    }
  }

  /**
   * Updates all the already registered presenters lifecycle and updates the view instance
   * associated to these presenters.
   *
   * @param view to be updated for every registered presenter.
   */
  public void updatePresenters(RosiePresenter.View view) {
    if (view == null) {
      throw new IllegalArgumentException(
          "The view instance used to update the presenters can't be null");
    }
    for (RosiePresenter presenter : presenters) {
      presenter.setView(view);
      presenter.update();
    }
  }

  /**
   * Pauses all the already registered presenters lifecycle.
   */
  public void pausePresenters() {
    for (RosiePresenter presenter : presenters) {
      presenter.pause();
      presenter.resetView();
    }
  }

  /**
   * Destroys all the already registered presenters lifecycle.
   */
  public void destroyPresenters() {
    for (RosiePresenter presenter : presenters) {
      presenter.destroy();
    }
  }

  /**
   * Registers a presenter instance.
   *
   * @param presenter to be registered
   */
  public void registerPresenter(RosiePresenter presenter) {
    if (presenter == null) {
      throw new IllegalArgumentException("The presenter instance to be registered can't be null");
    }
    presenters.add(presenter);
  }

  /**
   * Updates the view instance associated to all the already registered presenters.
   *
   * @param view to be assigned to the presenters.
   */
  public void setView(RosiePresenter.View view) {
    if (view == null) {
      throw new IllegalArgumentException(
          "The view instance used to configure the presenters can't be null");
    }
    for (RosiePresenter presenter : presenters) {
      presenter.setView(view);
    }
  }

  private void addAnnotatedPresenter(Object source) {
    for (Field field : source.getClass().getDeclaredFields()) {
      if (field.isAnnotationPresent(Presenter.class)) {
        if (Modifier.isPrivate(field.getModifiers())) {
          throw new PresenterNotAccessibleException(
              "Presenter must be accessible for this class. The visibility modifier used can't be"
                  + " private");
        } else {
          try {
            field.setAccessible(true);
            RosiePresenter presenter = (RosiePresenter) field.get(source);
            registerPresenter(presenter);
            field.setAccessible(false);
          } catch (IllegalAccessException e) {
            PresenterNotAccessibleException exception = new PresenterNotAccessibleException(
                "the presenter " + field.getName() + " can not be accessed");
            exception.initCause(e);
            throw exception;
          }
        }
      }
    }
  }
}
