package com.karumi.rosie.view.activity;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import butterknife.ButterKnife;
import com.karumi.rosie.application.RosieApplication;
import com.karumi.rosie.view.presenter.Presenter;
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

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    injectActivityModules();
    presenterLifeCycleHooker.addPresentedAnnotated(getClass().getDeclaredFields(), this);
    ButterKnife.inject(this);
    presenterLifeCycleHooker.presentersInitialize();
  }

  @Override protected void onResume() {
    super.onResume();
    presenterLifeCycleHooker.presentersUpdate();
  }

  @Override protected void onPause() {
    super.onPause();
    presenterLifeCycleHooker.presentersPause();
  }

  @Override protected void onDestroy() {
    super.onDestroy();
    presenterLifeCycleHooker.presentersDestroy();
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

  protected void registerPresenter(Presenter presenter) {
    presenterLifeCycleHooker.registerPresenter(presenter);
  }
}
