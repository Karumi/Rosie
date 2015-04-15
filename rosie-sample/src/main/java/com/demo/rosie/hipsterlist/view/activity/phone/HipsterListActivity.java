package com.demo.rosie.hipsterlist.view.activity.phone;

import android.os.Bundle;
import android.widget.ListView;
import com.demo.rosie.R;
import com.demo.rosie.hipsterlist.domain.HipsterListDomainModule;
import com.demo.rosie.hipsterlist.view.HipsterListViewModule;
import com.demo.rosie.hipsterlist.view.adapter.FeedAdapter;
import com.demo.rosie.hipsterlist.view.model.HipsterViewModel;
import com.demo.rosie.hipsterlist.view.presenter.HipsterListPresenter;
import com.rosie.view.activity.BaseActivity;
import com.rosie.view.presenter.annotation.Presenter;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;
import java.util.Arrays;
import java.util.List;
import javax.inject.Inject;

/**
 * A placeholder fragment containing a simple view.
 */
public class HipsterListActivity extends BaseActivity implements HipsterListPresenter.View {

  @Inject @Presenter public HipsterListPresenter presenter;
  @Inject Picasso picasso;
  @Inject Transformation transformationAvatar;
  ListView listviewFeed;
  private FeedAdapter feedAdapter;


  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_hipster_list);

    listviewFeed = ((ListView) findViewById(R.id.lv_feed));
  }

  @Override
  public void onResume() {
    super.onResume();
    presenter.setView(this);
    presenter.obtainHipsters();
    refreshData();
  }

  private void refreshData() {
    List<HipsterViewModel> hipsters = presenter.getHipsters();

    if (feedAdapter == null) {
      feedAdapter = new FeedAdapter(getBaseContext(), hipsters, picasso, transformationAvatar);
      listviewFeed.setAdapter(feedAdapter);
    } else {
      feedAdapter.notifyDataSetChanged();
    }
  }

  @Override public void updateList() {
    refreshData();
  }

  @Override protected List<Object> provideActivityScopeModules() {
    return Arrays.asList(new HipsterListViewModule(), new HipsterListDomainModule());
  }
}
