package com.karumi.rosie.view.presenter;

import com.karumi.rosie.domain.usercase.UserCaseHandler;
import com.karumi.rosie.domain.usercase.error.DomainError;
import com.karumi.rosie.domain.usercase.error.UseCaseErrorCallback;
import com.karumi.rosie.view.presenter.view.ErrorUi;

/**
 * All Presenters must extends from this one. Add accessor methods to manage the presenter life
 * cycle.
 */
public class RosiePresenter {

  protected final UserCaseHandler userCaseHandler;
  protected ErrorUi errorUi;

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
      userCaseHandler.registerErrorGlobalCallback(globalError);
    }
  }

  /**
   * This method has been called when the presenter has been paused.
   */
  public void pause() {
    if (globalError != null) {
      userCaseHandler.unregisterErrorGlobalCallback(globalError);
    }
  }

  /**
   * This method has been called when the presenter must be destroyed.
   */
  public void destroy() {

  }

  public void setErrorUi(ErrorUi errorUi) {
    this.errorUi = errorUi;
  }

  /**
   * notifies that an unexpected error has happened.
   *
   * @param domainError the error.
   * @return true if the error must be consume.
   */
  protected boolean onGlobalError(DomainError domainError) {
    return false;
  }

  private UseCaseErrorCallback globalError = new UseCaseErrorCallback() {
    @Override public void onError(DomainError error) {
      if (!onGlobalError(error)) {
        if (errorUi != null) {
          errorUi.showGlobalError(error);
        }
      }
    }
  };
}
