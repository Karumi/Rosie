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
import com.karumi.rosie.sample.BuildConfig;
import com.karumi.rosie.view.RosiePresenter;
import com.karumi.rosie.view.loading.RosiePresenterWithLoading;
import javax.inject.Inject;

public class FakeDataPresenter extends RosiePresenter<FakeDataPresenter.View> {
  @Inject public FakeDataPresenter(UseCaseHandler useCaseHandler) {
    super(useCaseHandler);
  }

  @Override protected void initialize() {
    super.initialize();
    if (BuildConfig.MARVEL_PUBLIC_KEY == null || BuildConfig.MARVEL_PRIVATE_KEY == null) {
      getView().showFakeDisclaimer();
    } else {
      getView().hideFakeDisclaymer();
    }
  }

  public interface View extends RosiePresenterWithLoading.View {
    void showFakeDisclaimer();

    void hideFakeDisclaymer();
  }
}
