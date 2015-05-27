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

package com.karumi.rosie.demo.hipsterlist.view.presenter;

import com.karumi.rosie.demo.hipsterlist.domain.usecase.ObtainHipsters;
import com.karumi.rosie.demo.hipsterlist.view.model.Hipster;
import com.karumi.rosie.domain.usecase.UseCaseHandler;
import com.karumi.rosie.domain.usecase.UseCaseParams;
import com.karumi.rosie.domain.usecase.annotation.Success;
import com.karumi.rosie.domain.usecase.callback.OnSuccessCallback;
import com.karumi.rosie.view.presenter.RosiePresenter;
import java.util.ArrayList;
import java.util.List;
import javax.inject.Inject;

public class HipsterListPresenter extends RosiePresenter<HipsterListPresenter.View> {

  private final ObtainHipsters obtainHipsters;
  private ArrayList<Hipster> hipsters = new ArrayList<Hipster>();

  @Inject
  public HipsterListPresenter(UseCaseHandler useCaseHandler, ObtainHipsters obtainHipsters) {
    super(useCaseHandler);
    this.obtainHipsters = obtainHipsters;
  }

  @Override public void update() {
    super.update();
    obtainHipsters();
  }

  private void obtainHipsters() {
    UseCaseParams params = new UseCaseParams.Builder().onSuccess(successCallback).build();

    UseCaseHandler useCaseHandler = getUseCaseHandler();
    useCaseHandler.execute(obtainHipsters, params);
  }

  private OnSuccessCallback successCallback = new OnSuccessCallback() {
    @Success public void success(List<Hipster> hipsters) {
      HipsterListPresenter.this.hipsters.clear();
      HipsterListPresenter.this.hipsters.addAll(hipsters);

      getView().updateList(HipsterListPresenter.this.hipsters);
    }
  };

  public interface View extends RosiePresenter.View {
    void updateList(List<Hipster> hipsters);
  }
}
