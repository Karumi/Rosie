/*
 * Copyright (C) 2015 Karumi.
 */

package com.karumi.rosie.sample.comics.view.presenter;

import com.karumi.rosie.domain.usecase.UseCaseHandler;
import com.karumi.rosie.view.RosiePresenter;

public class ComicsPresenter extends RosiePresenter<RosiePresenter.View> {

  public ComicsPresenter(UseCaseHandler useCaseHandler) {
    super(useCaseHandler);
  }

  public interface View extends RosiePresenter.View {

  }
}
