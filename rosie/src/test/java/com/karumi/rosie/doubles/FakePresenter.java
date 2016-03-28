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
    createUseCaseCall(errorUseCase).execute();
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
