package com.rosie.view.fragment;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.view.View;
import butterknife.ButterKnife;
import com.rosie.view.activity.BaseActivity;
import com.rosie.view.presenter.Presenter;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;

/**
 * Base fragment which performs injection using the activity object graph of its parent.
 */
public abstract class BaseFragment extends Fragment {

  List<Presenter> presenters = new ArrayList<Presenter>();
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
      if (field.isAnnotationPresent(com.rosie.view.presenter.annotation.Presenter.class)) {
        if (!Modifier.isPublic(field.getModifiers())) {
          throw new RuntimeException(
              "Presenter must be accessible for this class. Change visibility to public");
        } else {
          try {
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

  protected void injectDependencies() {
    if (!injected) {
      ((BaseActivity) getActivity()).inject(this);
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

  public void registerPresenter(Presenter presenter) {
    if (!presenters.contains(presenter)) {
      presenters.add(presenter);
    }
  }
}
