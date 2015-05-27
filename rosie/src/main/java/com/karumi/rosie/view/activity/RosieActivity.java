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
import android.view.View;
import android.view.ViewGroup;
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
<<<<<<< HEAD
 * BaseActivity created to implement some common functionality to activities using this library.
 * All
 * activities in this project should extend from this one.
=======
 * Base Activity created to implement some common functionality to every Activity using this
 * library. All activities in this project should extend from this one to be able to use core
 * features like view injection, dependency injection or Rosie presenters.
>>>>>>> #5-generify-rosie-presenters
 */
public class RosieActivity extends FragmentActivity implements ErrorView, RosiePresenter.View {

  private ObjectGraph activityScopeGraph;
  private PresenterLifeCycleLinker presenterLifeCycleLinker = new PresenterLifeCycleLinker();
  private boolean layoutSet = false;

  /**
   * Initializes the object graph associated to the activity scope, links presenters to the
   * Activity life cycle and initializes view injection using butter knife. Classes extending from
   * RosieActivity should call {@link #setContentView(int)}, {@link #setContentView(View)} or
   * {@link#setContentView(View, ViewGroup.LayoutParams)} before this method calls super.
   *
   * @throws IllegalStateException if setContentView is not called before this method.
   */
  @Override protected void onCreate(Bundle savedInstanceState) {
    if (!layoutSet) {
      throw new IllegalStateException("You need call setContentView(...) before call onCreate.");
    }

    super.onCreate(savedInstanceState);
    injectActivityModules();
    presenterLifeCycleLinker.addAnnotatedPresenter(getClass().getDeclaredFields(), this);
    ButterKnife.inject(this);
    presenterLifeCycleLinker.initializePresenters();
    presenterLifeCycleLinker.setView(this);
    presenterLifeCycleLinker.setErrorView(this);
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

  @Override public void setContentView(int layoutResID) {
    layoutSet = true;
    super.setContentView(layoutResID);
  }

  @Override public void setContentView(View view) {
    layoutSet = true;
    super.setContentView(view);
  }

  @Override public void setContentView(View view, ViewGroup.LayoutParams params) {
    layoutSet = true;
    super.setContentView(view, params);
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
    RosieApplication rosieApplication = (RosieApplication) getApplication();
    List<Object> activityScopeModules = getActivityScopeModules();
    if (activityScopeModules == null && shouldInjectActivity()) {
      activityScopeModules = Collections.EMPTY_LIST;
    }

    activityScopeGraph = rosieApplication.plusGraph(activityScopeModules);
    inject(this);
  }
}
