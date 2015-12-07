package com.karumi.rosie.domain.usecase;

import com.karumi.rosie.domain.usecase.callback.OnSuccessCallback;
import com.karumi.rosie.domain.usecase.error.OnErrorCallback;
import java.lang.ref.WeakReference;

public final class UseCaseCall {
  private final RosieUseCase useCase;
  private final UseCaseHandler useCaseHandler;

  private String useCaseName;
  private Object[] args;
  private WeakReference<OnSuccessCallback> onSuccessCallback;
  private WeakReference<OnErrorCallback> onErrorCallback;

  public UseCaseCall(RosieUseCase useCase, UseCaseHandler useCaseHandler) {
    this.useCase = useCase;
    this.useCaseHandler = useCaseHandler;
  }

  public UseCaseCall useCaseName(String name) {
    useCaseName = name;
    return this;
  }

  public UseCaseCall args(Object... args) {
    this.args = args;
    return this;
  }

  /**
   * The OnSuccessCallback passed as argument in this method will be referenced as a WeakReference
   * inside RosieUseCase and UseCaseParams to avoid memory leaks during the Activity lifecycle
   * pause-destroy stage. Remember to keep a strong reference of your OnSuccessCallback instance if
   * needed.
   */
  public UseCaseCall onSuccess(OnSuccessCallback onSuccessCallback) {
    if (onSuccessCallback == null) {
      throw new IllegalArgumentException(
          "OnSuccessCallback is null. You can not invoke it with" + " null callback.");
    }

    this.onSuccessCallback = new WeakReference<>(onSuccessCallback);
    return this;
  }

  /**
   * The OnErrorCallback passed as argument in this method will be referenced as a WeakReference
   * inside RosieUseCase and UseCaseParams to avoid memory leaks during the Activity lifecycle
   * pause-destroy stage. Remember to keep a strong reference of your OnErrorCallback instance if
   * needed.
   */
  public UseCaseCall onError(OnErrorCallback errorCallback) {
    if (errorCallback == null) {
      throw new IllegalArgumentException(
          "The onErrorCallback used is null, you can't use a null instance as onError callback.");
    }
    this.onErrorCallback = new WeakReference<>(errorCallback);
    return this;
  }

  public void execute() {
    if (args == null) {
      args = new Object[0];
    }
    UseCaseParams useCaseParams =
        new UseCaseParams(useCaseName, args, onSuccessCallback, onErrorCallback);
    useCaseHandler.execute(useCase, useCaseParams);
  }
}
