package com.demo.rosie.hipsterlist.view;

import com.demo.rosie.hipsterlist.view.activity.phone.HipsterListActivity;
import com.demo.rosie.hipsterlist.view.presenter.HipsterListPresenter;

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
