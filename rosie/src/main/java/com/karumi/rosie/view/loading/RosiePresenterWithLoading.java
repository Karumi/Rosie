/*
 * Copyright (C) 2015 Karumi.
 */

package com.karumi.rosie.view.loading;

import com.karumi.rosie.domain.usecase.UseCaseHandler;
import com.karumi.rosie.view.RosiePresenter;

public class RosiePresenterWithLoading<T extends RosiePresenterWithLoading.View>
    extends RosiePresenter<T> {

  public RosiePresenterWithLoading(UseCaseHandler useCaseHandler) {
    super(useCaseHandler);
  }

  protected void hideLoading() {
    getView().hideLoading();
  }

  protected void showLoading() {
    getView().showLoading();
  }

  public interface View extends RosiePresenter.View {
    void hideLoading();

    void showLoading();
  }
}
