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

package com.karumi.rosie.view.activity;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import butterknife.ButterKnife;
import com.karumi.rosie.application.RosieApplication;
import com.karumi.rosie.domain.usecase.error.Error;
import com.karumi.rosie.view.presenter.PresenterLifeCycleLinker;
import com.karumi.rosie.view.presenter.RosiePresenter;
import com.karumi.rosie.view.presenter.view.ErrorView;
import dagger.ObjectGraph;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Base Activity created to implement some common functionality to every Activity using this
 * library. All activities in this project should extend from this one to be able to use core
 * features like view injection, dependency injection or Rosie presenters.
 */
public abstract class RosieActivity extends FragmentActivity
    implements ErrorView, RosiePresenter.View {

  private ObjectGraph activityScopeGraph;
  private PresenterLifeCycleLinker presenterLifeCycleLinker = new PresenterLifeCycleLinker();

  /**
   * Initializes the object graph associated to the activity scope, links presenters to the
   * Activity life cycle and initializes view injection using butter knife.
   */
  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    injectActivityModules();
    int layoutId = getLayoutId();
    setContentView(layoutId);
    presenterLifeCycleLinker.addAnnotatedPresenter(getClass().getDeclaredFields(), this);
    ButterKnife.inject(this);
    presenterLifeCycleLinker.setView(this);
    presenterLifeCycleLinker.setErrorView(this);
    onPreparePresenter();
    presenterLifeCycleLinker.initializePresenters();
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
    presenterLifeCycleLinker.updatePresenters();
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
    super.onDestroy();
    presenterLifeCycleLinker.destroyPresenters();
  }

  /**
   * Given an object passed as argument uses the object graph associated to the Activity scope
   * to resolve all the dependencies needed by the object and inject them.
   */
  public final void inject(Object object) {
    activityScopeGraph.inject(object);
    activityScopeGraph.injectStatics();
  }

  @Override public void showError(Error error) {
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
   * Returns a List<Object> with the additional modules needed to create the Activity scope
   * graph. Override this method to return the list of modules associated to your Activity
   * graph.
   */
  protected List<Object> getActivityScopeModules() {
    return new ArrayList<Object>();
  }

  protected void registerPresenter(RosiePresenter presenter) {
    presenterLifeCycleLinker.registerPresenter(presenter);
  }

  private void injectActivityModules() {
    if (shouldInjectActivity()) {
      RosieApplication rosieApplication = (RosieApplication) getApplication();
      List<Object> activityScopeModules = getActivityScopeModules();
      if (activityScopeModules == null) {
        activityScopeModules = Collections.EMPTY_LIST;
      }

      activityScopeGraph = rosieApplication.plusGraph(activityScopeModules);
      inject(this);
    }
  }
}
