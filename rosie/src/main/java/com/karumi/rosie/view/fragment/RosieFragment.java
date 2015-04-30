package com.karumi.rosie.view.fragment;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.view.View;
import butterknife.ButterKnife;
import com.karumi.rosie.view.activity.RosieActivity;
import com.karumi.rosie.view.presenter.RosiePresenter;
import com.karumi.rosie.view.presenter.PresenterLifeCycleHooker;

/**
 * Base fragment which performs injection using the activity object graph of its parent.
 */
public abstract class RosieFragment extends Fragment {

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
}
