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

import com.karumi.rosie.domain.usercase.RosieUseCase;
import com.karumi.rosie.domain.usercase.UserCaseHandler;
import com.karumi.rosie.domain.usercase.annotation.UserCase;
import com.karumi.rosie.domain.usercase.error.DomainError;
import com.karumi.rosie.domain.usercase.error.GenericError;
import com.karumi.rosie.testutils.FakeScheduler;
import com.karumi.rosie.view.presenter.RosiePresenter;

/**
 * Double presenter to use on some tests.
 */
public class FakePresenter extends RosiePresenter {
  private UserCaseHandler userCaseHandler;
  private FakeUi fakeUi;

  public FakePresenter() {
    this(new UserCaseHandler(new FakeScheduler()));
  }

  public FakePresenter(UserCaseHandler userCaseHandler) {
    super(userCaseHandler);
    this.userCaseHandler = userCaseHandler;
  }

  public void callErrorUseCase() {
    ErrorUserCase errorUserCase = new ErrorUserCase();
    userCaseHandler.execute(errorUserCase);
  }

  public void setUi(FakeUi fakeUi) {
    this.fakeUi = fakeUi;
  }

  @Override protected boolean onGlobalError(DomainError domainError) {
    if(fakeUi != null) {
      fakeUi.showFakeError();
      return true;
    } else {
      return false;
    }
  }

  public interface FakeUi {
    void showFakeError();
  }

  public class ErrorUserCase extends RosieUseCase {

    @UserCase
    public void excuteError() throws Exception {
      notifyError(new GenericError("error"));
    }
  }
}
