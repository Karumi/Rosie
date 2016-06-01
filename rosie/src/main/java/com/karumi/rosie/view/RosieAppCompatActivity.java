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

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import com.karumi.rosie.application.RosieApplication;
import com.karumi.rosie.module.RosieActivityModule;
import dagger.ObjectGraph;
import java.util.ArrayList;
import java.util.List;

/**
 * Base Activity created to implement some common functionality for every Activity using this
 * library. All activities in this project should extend from this one to be able to use core
 * features like view injection, dependency injection or Rosie presenters.
 */
public abstract class RosieAppCompatActivity extends AppCompatActivity
    implements RosiePresenter.View, Injectable {

  private ObjectGraph activityScopeGraph;
  private PresenterLifeCycleLinker presenterLifeCycleLinker = new PresenterLifeCycleLinker();

  /**
   * Initializes the object graph associated to the activity scope, and links presenters to the
   * Activity life cycle.
   */
  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    if (shouldInitializeActivityScopeGraph()) {
      injectActivityModules();
    }
    int layoutId = getLayoutId();
    setContentView(layoutId);
    onPrepareActivity();
    onPreparePresenter();
    presenterLifeCycleLinker.initializeLifeCycle(this, this);
  }

  /**
   * Called just after setContentView .
   * Override this method to configure your activity and set up views if needed.
   */
  protected void onPrepareActivity() {

  }

  /**
   * Called before to initialize all the presenter instances linked to the component lifecycle.
   * Override this method to configure your presenter with extra data if needed.
   */
  protected void onPreparePresenter() {

  }

  /**
   * Connects the Activity onResume method with the presenter used in this Activity.
   */
  @Override protected void onResume() {
    super.onResume();
    presenterLifeCycleLinker.updatePresenters(this);
  }

  /**
   * Connects the Activity onPause method with the presenter used in this Activity.
   */
  @Override protected void onPause() {
    super.onPause();
    presenterLifeCycleLinker.pausePresenters();
  }

  /**
   * Connects the Activity onDestroy method with the presenter used in this Activity.
   */
  @Override protected void onDestroy() {
    presenterLifeCycleLinker.destroyPresenters();
    super.onDestroy();
  }

  /**
   * Given an object passed as argument uses the object graph associated to the Activity scope
   * to resolve all the dependencies needed by the object and inject them.
   */
  @Override public final void inject(Object object) {
    if (shouldInitializeActivityScopeGraph()) {
      injectActivityModules();
    }
    activityScopeGraph.inject(object);
    activityScopeGraph.injectStatics();
  }

  /**
   * Indicates if the class has to be injected or not. Override this method and return false to use
   * RosieActivity without inject any dependency.
   */
  protected boolean shouldInjectActivity() {
    return true;
  }

  /**
   * Returns the layout id associated to the layout used in the activity.
   */
  protected abstract int getLayoutId();

  /**
   * Returns a List with the additional modules needed to create the Activity scope
   * graph. Override this method to return the list of modules associated to your Activity
   * graph.
   */
  protected List<Object> getActivityScopeModules() {
    return new ArrayList<>();
  }

  private boolean shouldInitializeActivityScopeGraph() {
    return activityScopeGraph == null;
  }

  /**
   * Registers a presenter to link to this activity.
   */
  protected final void registerPresenter(RosiePresenter presenter) {
    presenterLifeCycleLinker.registerPresenter(presenter);
  }

  private void injectActivityModules() {
    if (!(getApplication() instanceof RosieApplication)) {
      return;
    }
    RosieApplication rosieApplication = (RosieApplication) getApplication();
    List<Object> additionalModules = getActivityScopeModules();
    if (additionalModules == null) {
      additionalModules = new ArrayList<>();
    }
    List<Object> activityScopeModules = new ArrayList<Object>();
    activityScopeModules.add(new RosieActivityModule(this));
    activityScopeModules.addAll(additionalModules);
    activityScopeGraph = rosieApplication.plusGraph(activityScopeModules);
    if (shouldInjectActivity()) {
      inject(this);
    }
  }
}
