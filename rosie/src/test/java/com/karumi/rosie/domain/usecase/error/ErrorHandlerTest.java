/*
 * The MIT License (MIT) Copyright (c) 2014 karumi Permission is hereby granted, free of charge,
 * to any person obtaining a copy of this software and associated documentation files (the
 * "Software"), to deal in the Software without restriction, including without limitation the
 * rights to use, copy, modify, merge, publish, distribute, sublicense,
 * and/or sell copies of the Software, and to permit persons to whom the Software is furnished to
 * do so, subject to the following conditions: The above copyright notice and this permission
 * notice shall be included in all copies or substantial portions of the Software. THE SOFTWARE
 * IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED,
 * INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR
 * COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY,
 * WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
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
