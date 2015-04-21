package com.karumi.rosie.view.activity;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import butterknife.ButterKnife;
import com.karumi.rosie.application.RosieApplication;
import dagger.ObjectGraph;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 *
 */
public class RosieActivity extends FragmentActivity {

  Set<com.karumi.rosie.view.presenter.Presenter> presenters = new HashSet<>();
  private ObjectGraph activityScopeGraph;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    injectActivityModules();
    addPresenterAnnotated();
    ButterKnife.inject(this);
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

  private void addPresenterAnnotated() {
    for (Field field : getClass().getDeclaredFields()) {
      if (field.isAnnotationPresent(com.karumi.rosie.view.presenter.annotation.Presenter.class)) {
        if (!Modifier.isPublic(field.getModifiers())) {
          throw new RuntimeException(
              "Presenter must be accessible for this class. Change visibility to public");
        } else {
          try {
            //TODO ADD CHECK TYPE
            com.karumi.rosie.view.presenter.Presenter
                presenter = (com.karumi.rosie.view.presenter.Presenter) field.get(this);
            presenters.add(presenter);
          } catch (IllegalAccessException e) {
            IllegalStateException runtimeException =
                new IllegalStateException("the presenter " + field.getName() + " can not be access");
            runtimeException.initCause(e);
            throw runtimeException;
          }
        }
      }
    }
  }

  public void registerPresenter(com.karumi.rosie.view.presenter.Presenter presenter) {
    //this method doesn't override the presenter
    presenters.add(presenter);
  }
}
