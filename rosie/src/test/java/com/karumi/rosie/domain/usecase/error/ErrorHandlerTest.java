/*
 * Copyright (C) 2015 Karumi.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.karumi.rosie.domain.usecase.error;

import com.karumi.rosie.doubles.FakeCallbackScheduler;
import java.lang.reflect.InvocationTargetException;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class ErrorHandlerTest {

  @Mock private Error error;

  @Before public void setUp() {
    MockitoAnnotations.initMocks(this);
  }

  @Test public void shouldNotifyInvocationTargetExceptionWrappingAnErrorNotHandledExceptionError() {
    OnErrorCallback onErrorCallback = givenAnErrorCallbackThatCapturesError();
    ErrorHandler errorHandler = givenAnErrorHandlerWithOnErrorCallbacks(onErrorCallback);
    InvocationTargetException invocationTargetException =
        givenAnInvocationExceptionWrappingAnErrorNotHandledException();

    errorHandler.notifyException(invocationTargetException, null);

    verify(onErrorCallback).onError(error);
  }

  @Test public void shouldNotifyBothRegisteredOnErrorCallbacksIfNoneCapturesError() {
    OnErrorCallback firstOnErrorCallback = givenAnErrorCallbackThatDoesNotCaptureError();
    OnErrorCallback secondOnErrorCallback = givenAnErrorCallbackThatDoesNotCaptureError();
    ErrorHandler errorHandler =
        givenAnErrorHandlerWithOnErrorCallbacks(firstOnErrorCallback, secondOnErrorCallback);
    InvocationTargetException invocationTargetException =
        givenAnInvocationExceptionWrappingAnErrorNotHandledException();

    errorHandler.notifyException(invocationTargetException, null);

    verify(secondOnErrorCallback).onError(error);
  }

  @Test public void shouldNotNotifySecondRegisteredOnErrorCallbackIfFirstOneCapturesError() {
    OnErrorCallback firstOnErrorCallback = givenAnErrorCallbackThatCapturesError();
    OnErrorCallback secondOnErrorCallback = givenAnErrorCallbackThatDoesNotCaptureError();
    ErrorHandler errorHandler =
        givenAnErrorHandlerWithOnErrorCallbacks(firstOnErrorCallback, secondOnErrorCallback);
    InvocationTargetException invocationTargetException =
        givenAnInvocationExceptionWrappingAnErrorNotHandledException();

    errorHandler.notifyException(invocationTargetException, null);

    verify(secondOnErrorCallback, never()).onError(error);
  }

  private InvocationTargetException givenAnInvocationExceptionWrappingAnErrorNotHandledException() {
    ErrorNotHandledException errorNotHandledException = new ErrorNotHandledException(error);
    return new InvocationTargetException(errorNotHandledException);
  }

  private OnErrorCallback givenAnErrorCallbackThatDoesNotCaptureError() {
    return givenAnErrorCallback(false);
  }

  private OnErrorCallback givenAnErrorCallbackThatCapturesError() {
    return givenAnErrorCallback(true);
  }

  private OnErrorCallback givenAnErrorCallback(boolean capturesError) {
    OnErrorCallback onErrorCallback = mock(OnErrorCallback.class);
    when(onErrorCallback.onError(any(Error.class))).thenReturn(capturesError);
    return onErrorCallback;
  }

  private ErrorHandler givenAnErrorHandlerWithOnErrorCallbacks(
      OnErrorCallback... onErrorCallbacks) {
    ErrorHandler errorHandler = new ErrorHandler(new ErrorFactory(), new FakeCallbackScheduler());
    for (OnErrorCallback onErrorCallback : onErrorCallbacks) {
      errorHandler.registerCallback(onErrorCallback);
    }
    return errorHandler;
  }
}
