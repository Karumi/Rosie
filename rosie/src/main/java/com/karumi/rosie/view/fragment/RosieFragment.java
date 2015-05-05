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
import android.view.View;
import butterknife.ButterKnife;
import com.karumi.rosie.domain.usercase.error.DomainError;
import com.karumi.rosie.view.activity.RosieActivity;
import com.karumi.rosie.view.presenter.PresenterLifeCycleHooker;
import com.karumi.rosie.view.presenter.RosiePresenter;
import com.karumi.rosie.view.presenter.view.ErrorUi;

/**
 * Base fragment which performs injection using the activity object graph of its parent.
 */
public abstract class RosieFragment extends Fragment implements ErrorUi {

  private PresenterLifeCycleHooker presenterLifeCycleHooker = new PresenterLifeCycleHooker();

  private boolean injected;

  @Override
  public void onActivityCreated(Bundle savedInstanceState) {
    super.onActivityCreated(savedInstanceState);
    injectDependencies();
  }

  @Override public void onAttach(Activity activity) {
    super.onAttach(activity);
    injectDependencies();
  }

  private void injectDependencies() {
    if (!injected) {
      ((RosieActivity) getActivity()).inject(this);
      presenterLifeCycleHooker.addAnnotatedPresenter(getClass().getDeclaredFields(), this);
      injected = true;
    }
  }

  @Override public void onViewCreated(View view, Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
    ButterKnife.inject(this, view);
  }

  @Override public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    presenterLifeCycleHooker.initializePresenters();
    presenterLifeCycleHooker.setGlobalError(this);
  }

  @Override public void onResume() {
    super.onResume();
    presenterLifeCycleHooker.updatePresenters();
  }

  @Override public void onPause() {
    super.onPause();
    presenterLifeCycleHooker.pausePresenters();
  }

  @Override public void onDestroy() {
    super.onDestroy();
    presenterLifeCycleHooker.destroyPresenters();
  }

  protected void registerPresenter(RosiePresenter presenter) {
    presenterLifeCycleHooker.registerPresenter(presenter);
  }

  @Override public void showGlobalError(DomainError domainError) {

  }
}
