package com.karumi.rosie.view;

import com.karumi.rosie.domain.usecase.RosieUseCase;
import com.karumi.rosie.domain.usecase.UseCaseCall;
import com.karumi.rosie.domain.usecase.UseCaseHandler;
import com.karumi.rosie.domain.usecase.error.OnErrorCallback;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.LinkedList;
import java.util.List;

/**
 * Implements all the presentation logic. All Presenters must extends from this class and indicate
 * which view extending from RosiePresenter.View interface are going to use. Override lifecycle
 * methods to be able to react to the view lifecycle.
 */
public class RosiePresenter<T extends RosiePresenter.View> {

  private final UseCaseHandler useCaseHandler;
  private final List<UseCaseCall> useCaseCalls = new LinkedList<>();
  private final OnErrorCallback globalErrorCallback = new OnErrorCallback() {
    @Override
    public void onError(Error error) {
      RosiePresenter.this.onError(error);
    }
  };
  private T view;
  private boolean shouldRegisterGlobalErrorCallback = true;

  public RosiePresenter(UseCaseHandler useCaseHandler) {
    this.useCaseHandler = useCaseHandler;
  }

  /**
   * Method called in the presenter lifecycle. Invoked when the component containing the presenter
   * is initialized.
   */
  protected void initialize() {
    registerGlobalErrorCallback();
  }

  /**
   * Method called in the presenter lifecycle. Invoked when the component containing the presenter
   * is resumed.
   */
  protected void update() {
    registerGlobalErrorCallback();
  }

  /**
   * Method called in the presenter lifecycle. Invoked when the component containing the presenter
   * is paused.
   */
  protected void pause() {
    unregisterGlobalErrorCallback();
  }

  /**
   * Method called in the presenter lifecycle. Invoked when the component containing the presenter
   * is destroyed.
   */
  protected void destroy() {

  }

  /**
   * Create a new call to can execute an use case. <p/>
   *
   * @param useCase use case will be execute.
   * @return Call object for invoke the use case.
   */
  protected final UseCaseCall createUseCaseCall(RosieUseCase useCase) {
    UseCaseCall useCaseCall = new UseCaseCall(useCase, useCaseHandler);
    retainUseCaseCall(useCaseCall);
    return useCaseCall;
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
  protected final UseCaseHandler getUseCaseHandler() {
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
    final Class<?> viewClass = getViewInterfaceClass();
    InvocationHandler emptyHandler = new InvocationHandler() {
      @Override
      public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        return null;
      }
    };
    ClassLoader classLoader = viewClass.getClassLoader();
    Class[] interfaces = new Class[1];
    interfaces[0] = viewClass;
    this.view = (T) Proxy.newProxyInstance(classLoader, interfaces, emptyHandler);
  }

  private Class<?> getViewInterfaceClass() {
    Class<?> interfaceClass = null;
    Class<?>[] interfaces = this.view.getClass().getInterfaces();
    for (int i = 0; i < interfaces.length; i++) {
      Class<?> interfaceCandidate = interfaces[i];
      if (RosiePresenter.View.class.isAssignableFrom(interfaceCandidate)) {
        interfaceClass = interfaceCandidate;
      }
    }
    return interfaceClass;
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

  private void retainUseCaseCall(UseCaseCall useCaseCall) {
    useCaseCalls.add(useCaseCall);
  }

  /**
   * Represents the View component inside the Model View Presenter pattern. This interface must be
   * used as base interface for every View interface declared.
   */
  public interface View {

  }
}