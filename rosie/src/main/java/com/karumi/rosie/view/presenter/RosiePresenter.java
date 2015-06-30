package com.karumi.rosie.view.presenter;

import com.karumi.rosie.domain.usecase.UseCaseHandler;
import com.karumi.rosie.domain.usecase.error.OnErrorCallback;
import com.karumi.rosie.view.presenter.view.ErrorView;

/**
 * Implements all the presentation logic. All Presenters must extends from this class and indicate
 * which view extending from RosiePresenter.View interface are going to use. Override lifecycle
 * methods to be able to react to the view lifecycle.
 */
public class RosiePresenter<T extends RosiePresenter.View> {

  private final UseCaseHandler useCaseHandler;

  private ErrorView errorView;
  private T view;

  public RosiePresenter(UseCaseHandler useCaseHandler) {
    this.useCaseHandler = useCaseHandler;
  }

  /**
   * Method called in the presenter lifecycle. Invoked when the component containing the
   * presenter is initialized.
   */
  public void initialize() {

  }

  /**
   * Method called in the presenter lifecycle. Invoked when the component containing the
   * presenter is resumed.
   */
  public void update() {
    if (globalError != null) {
      useCaseHandler.registerGlobalErrorCallback(globalError);
    }
  }

  /**
   * Method called in the presenter lifecycle. Invoked when the component containing the
   * presenter is paused.
   */
  public void pause() {
    if (globalError != null) {
      useCaseHandler.unregisterGlobalErrorCallback(globalError);
    }
  }

  /**
   * Method called in the presenter lifecycle. Invoked when the component containing the
   * presenter is destroyed.
   */
  public void destroy() {

  }

  /**
   * Returns the view configured in the presenter which real implementation is an Activity or
   * Fragment using this presenter.
   */
  protected final T getView() {
    return view;
  }

  /**
   * Notifies that an unexpected error has happened.
   *
   * @param error the error.
   * @return true if the error must be consume.
   */
  protected boolean onError(Error error) {
    return false;
  }

  /**
   * Returns the UseCaseHandler instance used to create this presenter class.
   */
  protected UseCaseHandler getUseCaseHandler() {
    return useCaseHandler;
  }

  /**
   * Configures the View instance used in this presenter as view.
   */
  void setView(T view) {
    this.view = view;
  }

  /**
   * Configures the ErrorView used in this presenter
   */
  void setErrorView(ErrorView errorView) {
    this.errorView = errorView;
  }

  private OnErrorCallback globalError = new OnErrorCallback() {
    @Override public void onError(Error error) {
      if (!RosiePresenter.this.onError(error)) {
        if (errorView != null) {
          errorView.showError(error);
        }
      }
    }
  };

  /**
   * Represents the View component inside the Model View Presenter pattern. This interface must be
   * used as base interface for every View interface declared.
   */
  public interface View {

  }
}
