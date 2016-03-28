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

package com.karumi.rosie.view;

import com.karumi.rosie.domain.usecase.RosieUseCase;
import com.karumi.rosie.domain.usecase.UseCaseHandler;
import com.karumi.rosie.domain.usecase.error.OnErrorCallback;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class) public class RosiePresenterTest {

  @Mock private UseCaseHandler useCaseHandler;
  @Mock private RosieUseCase anyUseCase;

  @Test public void shouldRegisterGlobalErrorCallbackDuringTheInitializeLifecycleStage() {
    RosiePresenter rosiePresenter = givenARosiePresenterWithRegisteredCallback();

    rosiePresenter.initialize();
    rosiePresenter.update();

    verify(useCaseHandler).registerGlobalErrorCallback(any(OnErrorCallback.class));
  }

  @Test public void shouldUnregisterGlobalErrorCallbackDuringThePauseLifecycleStage() {
    RosiePresenter rosiePresenter = givenARosiePresenterWithRegisteredCallback();

    rosiePresenter.pause();

    verify(useCaseHandler).unregisterGlobalErrorCallback(any(OnErrorCallback.class));
  }

  private RosiePresenter givenARosiePresenterWithRegisteredCallback() {
    return new RosiePresenterWithRegisteredCallback(useCaseHandler);
  }

  private static class RosiePresenterWithRegisteredCallback extends RosiePresenter
      implements OnErrorCallback {
    public RosiePresenterWithRegisteredCallback(UseCaseHandler useCaseHandler) {
      super(useCaseHandler);
      registerOnErrorCallback(this);
    }

    @Override public boolean onError(Error error) {
      return true;
    }
  }
}
