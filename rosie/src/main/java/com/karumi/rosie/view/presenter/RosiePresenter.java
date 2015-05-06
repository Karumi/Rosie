package com.karumi.rosie.view.presenter;

import com.karumi.rosie.domain.usercase.UserCaseHandler;
import com.karumi.rosie.domain.usercase.error.*;
import com.karumi.rosie.domain.usercase.error.Error;
import com.karumi.rosie.view.presenter.view.ErrorView;

/**
 * All Presenters must extends from this one. Add accessor methods to manage the presenter life
 * cycle.
 */
public class RosiePresenter {

  private final UserCaseHandler userCaseHandler;
  private ErrorView errorView;

  public RosiePresenter(UserCaseHandler userCaseHandler) {
    this.userCaseHandler = userCaseHandler;
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
      userCaseHandler.registerGlobalErrorCallback(globalError);
    }
  }

  /**
   * This method has been called when the presenter has been paused.
   */
  public void pause() {
    if (globalError != null) {
      userCaseHandler.unregisterGlobalErrorCallback(globalError);
    }
  }

  /**
   * This method has been called when the presenter must be destroyed.
   */
  public void destroy() {

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

  private UseCaseErrorCallback globalError = new UseCaseErrorCallback() {
    @Override public void onError(com.karumi.rosie.domain.usercase.error.Error error) {
      if (!onGlobalError(error)) {
        if (errorView != null) {
          errorView.showGlobalError(error);
        }
      }
    }
  };

  protected UserCaseHandler getUserCaseHandler() {
    return userCaseHandler;
  }
}
