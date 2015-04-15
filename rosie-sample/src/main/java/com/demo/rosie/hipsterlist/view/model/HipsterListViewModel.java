package com.demo.rosie.hipsterlist.view.model;

import java.util.ArrayList;
import java.util.List;

/**
 *
 */
public class HipsterListViewModel {

  private List<HipsterViewModel> hipsters = new ArrayList<HipsterViewModel>();

  public List<HipsterViewModel> getHipsters() {
    return hipsters;
  }

  public void setHipsters(List<HipsterViewModel> hipsters) {
    this.hipsters = hipsters;
  }
}
