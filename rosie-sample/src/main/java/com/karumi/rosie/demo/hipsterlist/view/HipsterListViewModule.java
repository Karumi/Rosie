package com.karumi.rosie.demo.hipsterlist.view;

import com.karumi.rosie.demo.hipsterlist.view.activity.phone.HipsterListActivity;
import com.karumi.rosie.demo.hipsterlist.view.presenter.HipsterListPresenter;

import dagger.Module;
import dagger.Provides;

/**
 *
 */
@Module(
    library = true,
    complete = false,
    injects = { HipsterListActivity.class }
)
public class HipsterListViewModule {

  @Provides
  public HipsterListPresenter provideHipsterListPresenter() {
    return new HipsterListPresenter();
  }

}
