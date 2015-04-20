package com.karumi.rosie.view.fragment;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.view.View;
import butterknife.ButterKnife;
import com.karumi.rosie.view.activity.RosieActivity;
import com.karumi.rosie.view.presenter.annotation.Presenter;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;

/**
 * Base fragment which performs injection using the activity object graph of its parent.
 */
public abstract class RosieFragment extends Fragment {

  List<com.karumi.rosie.view.presenter.Presenter> presenters = new ArrayList<>();
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

  private void addPresentedAnnotated() {
    for (Field field : getClass().getDeclaredFields()) {
      if (field.isAnnotationPresent(Presenter.class)) {
        if (!Modifier.isPublic(field.getModifiers())) {
          throw new RuntimeException(
              "Presenter must be accessible for this class. Change visibility to public");
        } else {
          try {
            com.karumi.rosie.view.presenter.Presenter presenter =
                (com.karumi.rosie.view.presenter.Presenter) field.get(this);
            presenters.add(presenter);
          } catch (IllegalAccessException e) {
            IllegalStateException runtimeException = new IllegalStateException(
                "the presenter " + field.getName() + " can not be access");
            runtimeException.initCause(e);
            throw runtimeException;
          }
        }
      }
    }
  }

  protected void injectDependencies() {
    if (!injected) {
      ((RosieActivity) getActivity()).inject(this);
      addPresentedAnnotated();
      injected = true;
    }
  }

  @Override public void onViewCreated(View view, Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
    ButterKnife.inject(this, view);
  }

  @Override public void onResume() {
    super.onResume();
  }

  @Override public void onPause() {
    super.onPause();
  }

  public void registerPresenter(com.karumi.rosie.view.presenter.Presenter presenter) {
    if (!presenters.contains(presenter)) {
      presenters.add(presenter);
    }
  }
}
