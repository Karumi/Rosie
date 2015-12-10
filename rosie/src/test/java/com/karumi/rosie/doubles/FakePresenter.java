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

package com.karumi.rosie.doubles;

import com.karumi.rosie.domain.usecase.RosieUseCase;
import com.karumi.rosie.domain.usecase.UseCaseHandler;
import com.karumi.rosie.domain.usecase.annotation.UseCase;
import com.karumi.rosie.domain.usecase.error.ErrorHandler;
import com.karumi.rosie.domain.usecase.error.OnErrorCallback;
import com.karumi.rosie.testutils.FakeTaskScheduler;
import com.karumi.rosie.view.RosiePresenter;

/**
 * Double presenter to use on some tests.
 */
public class FakePresenter extends RosiePresenter implements OnErrorCallback {
  private UseCaseHandler useCaseHandler;
  private FakeUi fakeUi;

  public FakePresenter() {
    this(
        new UseCaseHandler(new FakeTaskScheduler(), new ErrorHandler(new FakeCallbackScheduler())));
    registerOnErrorCallback(this);
  }

  public FakePresenter(UseCaseHandler useCaseHandler) {
    super(useCaseHandler);
    this.useCaseHandler = useCaseHandler;
  }

  public void callErrorUseCase() {
    ErrorUseCase errorUseCase = new ErrorUseCase();
    useCaseHandler.execute(errorUseCase);
  }

  public void setUi(FakeUi fakeUi) {
    this.fakeUi = fakeUi;
  }

  @Override public boolean onError(Error error) {
    if (fakeUi != null) {
      fakeUi.showFakeError();
      return true;
    } else {
      return false;
    }
  }

  public interface FakeUi {
    void showFakeError();
  }

  public class ErrorUseCase extends RosieUseCase {

    @UseCase public void executeError() throws Exception {
      notifyError(new Error("error"));
    }
  }
}
