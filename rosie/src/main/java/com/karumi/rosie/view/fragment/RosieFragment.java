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

package com.karumi.rosie.view.fragment;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import butterknife.ButterKnife;
import com.karumi.rosie.view.activity.RosieActivity;
import com.karumi.rosie.view.presenter.PresenterLifeCycleLinker;
import com.karumi.rosie.view.presenter.RosiePresenter;

/**
 * Base Fragment created to implement some common functionality to every Fragment using this
 * library. All Fragments in this project should extend from this one to be able to use core
 * features like view injection, dependency injection or Rosie presenters.
 */
public abstract class RosieFragment extends Fragment implements RosiePresenter.View {

  private PresenterLifeCycleLinker presenterLifeCycleLinker;

  private boolean injected;

  /**
   * Injects the Fragment dependencies if this injection wasn't performed previously in other
   * Fragment life cycle event.
   */
  @Override public void onActivityCreated(Bundle savedInstanceState) {
    super.onActivityCreated(savedInstanceState);
    injectDependencies();
  }

  /**
   * Injects the Fragment dependencies if this injection wasn't performed previously in other
   * Fragment life cycle event.
   */
  @Override public void onAttach(Activity activity) {
    super.onAttach(activity);
    injectDependencies();
  }

  @Nullable @Override public View onCreateView(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    int layoutId = getLayoutId();
    View view = inflater.inflate(layoutId, container, false);
    ButterKnife.inject(this, view);
    return view;
  }

  /**
   * Injects the Fragment views using Butter Knife library.
   */
  @Override public void onViewCreated(View view, Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
    presenterLifeCycleLinker = new PresenterLifeCycleLinker();
    presenterLifeCycleLinker.setView(this);
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
   * Connects the Fragment onResume method with the presenter used in this Activity.
   */
  @Override public void onResume() {
    super.onResume();
    presenterLifeCycleLinker.setView(this);
    presenterLifeCycleLinker.updatePresenters();
  }

  /**
   * Connects the Fragment onPause method with the presenter used in this Activity.
   */
  @Override public void onPause() {
    super.onPause();
    presenterLifeCycleLinker.pausePresenters();
  }

  /**
   * Connects the Fragment onDestroy method with the presenter used in this Activity.
   */
  @Override public void onDestroy() {
    super.onDestroy();
    presenterLifeCycleLinker.destroyPresenters();
  }

  /**
   * Indicates if the class has to be injected or not. Override this method and return false to use
   * RosieActivity without inject any dependency.
   */
  protected boolean shouldInjectFragment() {
    return true;
  }

  /**
   * Returns the layout id associated to the layout used in the activity.
   */
  protected abstract int getLayoutId();

  protected void registerPresenter(RosiePresenter presenter) {
    presenterLifeCycleLinker.registerPresenter(presenter);
  }

  private void injectDependencies() {
    if (!injected && shouldInjectFragment()) {
      ((RosieActivity) getActivity()).inject(this);
      presenterLifeCycleLinker.addAnnotatedPresenter(getClass().getDeclaredFields(), this);
      injected = true;
    }
  }
}
