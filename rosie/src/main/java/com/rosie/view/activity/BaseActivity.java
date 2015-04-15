package com.rosie.view.activity;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import butterknife.ButterKnife;
import com.rosie.application.BaseApplication;
import com.rosie.view.presenter.Presenter;
import dagger.ObjectGraph;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;

/**
 *
 */
public class BaseActivity extends FragmentActivity {

  List<Presenter> presenters = new ArrayList<Presenter>();
  private ObjectGraph activityScopeGraph;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    injectActivityModules();
    addPresentedAnnotated();
    ButterKnife.inject(this);
  }

  private void injectActivityModules() {
    BaseApplication baseApplication = (BaseApplication) getApplication();

    List<Object> activityScopeModules = provideActivityScopeModules();
    if (activityScopeModules == null) {
      activityScopeModules = new ArrayList<Object>();
    }

    activityScopeGraph = baseApplication.plusModules(activityScopeModules);
    inject(this);
  }

  protected List<Object> provideActivityScopeModules() {
    return new ArrayList<Object>();
  }

  public void inject(Object object) {
    activityScopeGraph.inject(object);
  }

  @Override protected void onResume() {
    super.onResume();
  }

  private void addPresentedAnnotated() {
    for (Field field : getClass().getDeclaredFields()) {
      if (field.isAnnotationPresent(com.rosie.view.presenter.annotation.Presenter.class)) {
        if (!Modifier.isPublic(field.getModifiers())) {
          throw new RuntimeException(
              "Presenter must be accessible for this class. Change visibility to public");
        } else {
          try {
            //TODO ADD CHECK TYPE
            Presenter presenter = (Presenter) field.get(this);
            presenters.add(presenter);
          } catch (IllegalAccessException e) {
            RuntimeException runtimeException = new RuntimeException("the presenter "
                + field.getName() + " can not be access");
            runtimeException.initCause(e);
            throw runtimeException;
          }
        }
      }
    }
  }

  @Override protected void onPause() {
    super.onPause();
  }

  public void registerPresenter(Presenter presenter) {
    if (!presenters.contains(presenter)) {
      presenters.add(presenter);
    }
  }

  @Override
  protected void onDestroy() {
    super.onDestroy();
  }
}
