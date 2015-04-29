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

import com.karumi.rosie.demo.hipsterlist.view.model.HipsterListViewModel;
import com.karumi.rosie.demo.hipsterlist.view.model.HipsterViewModel;
import com.karumi.rosie.view.presenter.Presenter;
import java.util.ArrayList;
import java.util.List;

/**
 *
 */
public class HipsterListPresenter extends Presenter {

  private View view;
  ArrayList<HipsterViewModel> hipsters = new ArrayList<HipsterViewModel>();

  public HipsterListPresenter() {
    super();
  }

  public void setView(View view) {
    this.view = view;
  }

  public void obtainHipsters() {
    HipsterListViewModel hipsterListViewModel = new HipsterListViewModel();
    HipsterViewModel hipsterTest = new HipsterViewModel();
    hipsterTest.setName("Hipstotito Fernandez");
    hipsterTest.setAvatarUrl(
        "https://cdn0.iconfinder.com/data/icons/avatars-3/512/avatar_hipster_guy-512.png");
    hipsterTest.setId("1");

    ArrayList<HipsterViewModel> hipsterViewModels = new ArrayList<HipsterViewModel>();
    hipsterViewModels.add(hipsterTest);
    hipsterListViewModel.setHipsters(hipsterViewModels);

    hipsters.clear();
    hipsters.addAll(hipsterListViewModel.getHipsters());
    view.updateList();
  }

  public List<HipsterViewModel> getHipsters() {
    return hipsters;
  }

  public interface View {
    public void updateList();
  }
}
