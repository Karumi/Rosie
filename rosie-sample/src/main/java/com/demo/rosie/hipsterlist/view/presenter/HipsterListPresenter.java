package com.demo.rosie.hipsterlist.view.presenter;

import com.demo.rosie.hipsterlist.view.model.HipsterListViewModel;
import com.demo.rosie.hipsterlist.view.model.HipsterViewModel;
import com.rosie.view.presenter.Presenter;

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
