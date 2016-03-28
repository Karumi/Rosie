package com.karumi.rosie.doubles;

import com.karumi.rosie.view.Presenter;
import com.karumi.rosie.view.RosiePresenter;

public class AnyClassWithAnAnnotatedPresenter {

  @Presenter public RosiePresenter<RosiePresenter.View> presenter;

}
