/*
 *  Copyright (C) 2015 Karumi.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package com.karumi.rosie.sample.main.view.presenter;

import com.karumi.rosie.domain.usecase.UseCaseHandler;
import com.karumi.rosie.sample.main.domain.usecase.GetMarvelSettings;
import com.karumi.rosie.view.RosiePresenter;
import com.karumi.rosie.view.loading.RosiePresenterWithLoading;
import javax.inject.Inject;

public class FakeDataPresenter extends RosiePresenter<FakeDataPresenter.View> {

  private final GetMarvelSettings getMarvelSettings;

  @Inject
  public FakeDataPresenter(GetMarvelSettings getMarvelSettings, UseCaseHandler useCaseHandler) {
    super(useCaseHandler);
    this.getMarvelSettings = getMarvelSettings;
  }

  @Override public void initialize() {
    super.initialize();
    if (!getMarvelSettings.haveKeys()) {
      getView().showFakeDisclaimer();
    } else {
      getView().hideFakeDisclaimer();
    }
  }

  public interface View extends RosiePresenterWithLoading.View {
    void showFakeDisclaimer();

    void hideFakeDisclaimer();
  }
}
