package com.karumi.rosie.view.presenter;

import com.karumi.rosie.domain.usecase.UseCaseHandler;
import com.karumi.rosie.domain.usecase.error.Error;
import com.karumi.rosie.domain.usecase.error.UseCaseErrorCallback;
import com.karumi.rosie.view.presenter.view.ErrorView;

/**
 * All Presenters must extends from this one. Add accessor methods to manage the presenter life
 * cycle.
 */
public class RosiePresenter<T extends RosiePresenter.View> {

  private final UseCaseHandler useCaseHandler;
  private ErrorView errorView;
  private T view;

  public RosiePresenter(UseCaseHandler useCaseHandler) {
    this.useCaseHandler = useCaseHandler;
  }

  /**
   * This method has been called when the presenter must be initialize.
   */
  public void initialize() {

  }

  /**
   * This method has been called when the presenter must be update the info.
   */
  public void update() {
    if (globalError != null) {
      useCaseHandler.registerGlobalErrorCallback(globalError);
    }
  }

  /**
   * This method has been called when the presenter has been paused.
   */
  public void pause() {
    if (globalError != null) {
      useCaseHandler.unregisterGlobalErrorCallback(globalError);
    }
  }

  /**
   * This method has been called when the presenter must be destroyed.
   */
  public void destroy() {

  }

  protected T getView(){
    return view;
  }

  void setView(T view) {
    this.view = view;
  }

  void setErrorView(ErrorView errorView) {
    this.errorView = errorView;
  }

  /**
   * notifies that an unexpected error has happened.
   *
   * @param error the error.
   * @return true if the error must be consume.
   */
  protected boolean onGlobalError(Error error) {
    return false;
  }

  protected UseCaseHandler getUseCaseHandler() {
    return useCaseHandler;
  }

  private UseCaseErrorCallback globalError = new UseCaseErrorCallback() {
    @Override public void onError(com.karumi.rosie.domain.usecase.error.Error error) {
      if (!onGlobalError(error)) {
        if (errorView != null) {
          errorView.showError(error);
        }
      }
    }
  };

  public interface View {

  }
}
