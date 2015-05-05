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
import com.karumi.rosie.domain.usercase.error.DomainError;
import com.karumi.rosie.view.presenter.PresenterLifeCycleHooker;
import com.karumi.rosie.view.presenter.RosiePresenter;
import com.karumi.rosie.view.presenter.view.ErrorUi;
import dagger.ObjectGraph;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 *
 */
public class RosieActivity extends FragmentActivity implements ErrorUi {

  private ObjectGraph activityScopeGraph;
  private PresenterLifeCycleHooker presenterLifeCycleHooker = new PresenterLifeCycleHooker();
  private boolean layoutSet = false;

  /**
   * Oncreate method is mandatory called for init rosie. Classes extending from RosieActivity
   * should call {@link #setContentView(int)} or {@link #setContentView(View)} or {@link
   * #setContentView(View, ViewGroup.LayoutParams)} before this method.
   * call
   * super.
   *
   * @throws IllegalStateException if setContentView is not called before this method.
   */
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    if (!layoutSet) {
      throw new IllegalStateException("You need call setContentView(...) before call onCreate.");
    }

    super.onCreate(savedInstanceState);
    injectActivityModules();
    presenterLifeCycleHooker.addAnnotatedPresenter(getClass().getDeclaredFields(), this);
    ButterKnife.inject(this);
    presenterLifeCycleHooker.initializePresenters();
    presenterLifeCycleHooker.setGlobalError(this);
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

  @Override protected void onResume() {
    super.onResume();
    presenterLifeCycleHooker.updatePresenters();
  }

  @Override protected void onPause() {
    super.onPause();
    presenterLifeCycleHooker.pausePresenters();
  }

  @Override protected void onDestroy() {
    super.onDestroy();
    presenterLifeCycleHooker.destroyPresenters();
  }

  private void injectActivityModules() {
    RosieApplication rosieApplication = (RosieApplication) getApplication();

    List<Object> activityScopeModules = provideActivityScopeModules();
    if (activityScopeModules == null) {
      activityScopeModules = Collections.EMPTY_LIST;
    }

    activityScopeGraph = rosieApplication.plusModules(activityScopeModules);
    inject(this);
  }

  protected List<Object> provideActivityScopeModules() {
    return new ArrayList<Object>();
  }

  public void inject(Object object) {
    activityScopeGraph.inject(object);
  }

  protected void registerPresenter(RosiePresenter presenter) {
    presenterLifeCycleHooker.registerPresenter(presenter);
  }

  @Override public void showGlobalError(DomainError domainError) {
  }
}
