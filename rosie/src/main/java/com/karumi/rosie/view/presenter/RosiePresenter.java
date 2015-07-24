package com.karumi.rosie.view.presenter;

import com.karumi.rosie.domain.usecase.RosieUseCase;
import com.karumi.rosie.domain.usecase.UseCaseHandler;
import com.karumi.rosie.domain.usecase.UseCaseParams;
import com.karumi.rosie.domain.usecase.error.OnErrorCallback;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * Implements all the presentation logic. All Presenters must extends from this class and indicate
 * which view extending from RosiePresenter.View interface are going to use. Override lifecycle
 * methods to be able to react to the view lifecycle.
 */
public class RosiePresenter<T extends RosiePresenter.View> {

  private final UseCaseHandler useCaseHandler;

  private T view;
  private boolean shouldRegisterGlobalErrorCallback = true;
  private final OnErrorCallback globalErrorCallback = new OnErrorCallback() {
    @Override public void onError(Error error) {
      RosiePresenter.this.onError(error);
    }
  };

  public RosiePresenter(UseCaseHandler useCaseHandler) {
    this.useCaseHandler = useCaseHandler;
  }

  /**
   * Method called in the presenter lifecycle. Invoked when the component containing the
   * presenter is initialized.
   */
  public void initialize() {
    registerGlobalErrorCallback();
  }

  /**
   * Method called in the presenter lifecycle. Invoked when the component containing the
   * presenter is resumed.
   */
  public void update() {
    registerGlobalErrorCallback();
  }

  /**
   * Method called in the presenter lifecycle. Invoked when the component containing the
   * presenter is paused.
   */
  public void pause() {
    unregisterGlobalErrorCallback();
  }

  /**
   * Method called in the presenter lifecycle. Invoked when the component containing the
   * presenter is destroyed.
   */
  public void destroy() {
    this.view = null;
  }

  /**
   * Executes a RosieUseCase passed as parameter using the UseCaseHandler instance obtained during
   * the RosiePresenter construction.
   */
  protected void execute(RosieUseCase useCase) {
    useCaseHandler.execute(useCase);
  }

  /**
   * Executes a RosieUseCase passed as parameter using the UseCaseHandler instance obtained during
   * the RosiePresenter construction and the UseCaseParams object passed as second parameter.
   */
  protected void execute(RosieUseCase useCase, UseCaseParams useCaseParams) {
    useCaseHandler.execute(useCase, useCaseParams);
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
   * Changes the current view instance with a dynamic proxy to avoid real UI updates.
   */
  void resetView() {
    Class<?> viewClass = view.getClass().getInterfaces()[0];
    InvocationHandler emptyHandler = new InvocationHandler() {
      @Override public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        return null;
      }
    };
    ClassLoader classLoader = viewClass.getClassLoader();
    Class[] interfaces = new Class[1];
    interfaces[0] = viewClass;
    view = (T) Proxy.newProxyInstance(classLoader, interfaces, emptyHandler);
  }

  private void registerGlobalErrorCallback() {
    if (globalErrorCallback != null && shouldRegisterGlobalErrorCallback) {
      shouldRegisterGlobalErrorCallback = false;
      useCaseHandler.registerGlobalErrorCallback(globalErrorCallback);
    }
  }

  private void unregisterGlobalErrorCallback() {
    if (globalErrorCallback != null) {
      shouldRegisterGlobalErrorCallback = true;
      useCaseHandler.unregisterGlobalErrorCallback(globalErrorCallback);
    }
  }

  /**
   * Represents the View component inside the Model View Presenter pattern. This interface must be
   * used as base interface for every View interface declared.
   */
  public interface View {

  }
}
