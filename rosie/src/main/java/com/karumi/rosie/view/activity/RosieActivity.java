package com.karumi.rosie.view.activity;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.ViewGroup;
import butterknife.ButterKnife;
import com.karumi.rosie.application.RosieApplication;
import com.karumi.rosie.view.presenter.RosiePresenter;
import com.karumi.rosie.view.presenter.PresenterLifeCycleHooker;
import dagger.ObjectGraph;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 *
 */
public class RosieActivity extends FragmentActivity {

  private ObjectGraph activityScopeGraph;
  private PresenterLifeCycleHooker presenterLifeCycleHooker = new PresenterLifeCycleHooker();
  private boolean layoutSetted = false;


  /**
   * Oncreate method is mandatory called for init rosie. Classes extending from RosieActivity
   * should call {@link #setContentView(int)} or {@link #setContentView(View)} or {@link
   * #setContentView(View, ViewGroup.LayoutParams)} before this method.
   * call
   * super.
   * @param savedInstanceState
   * @throws IllegalStateException if setContentView is not called before this method.
   */
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    if (!layoutSetted) {
      throw new IllegalStateException("You need call setContentView(...) before call onCreate.");
    }

    super.onCreate(savedInstanceState);
    injectActivityModules();
    presenterLifeCycleHooker.addAnnotatedPresenter(getClass().getDeclaredFields(), this);
    ButterKnife.inject(this);
    presenterLifeCycleHooker.initializePresenters();
  }

  @Override public void setContentView(int layoutResID) {
    layoutSetted = true;
    super.setContentView(layoutResID);
  }

  @Override public void setContentView(View view) {
    layoutSetted = true;
    super.setContentView(view);
  }

  @Override public void setContentView(View view, ViewGroup.LayoutParams params) {
    layoutSetted = true;
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
}
