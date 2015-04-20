package com.karumi.rosie.demo.hipsterlist.view.activity.phone;

import android.os.Bundle;
import android.widget.ListView;
import butterknife.InjectView;
import com.demo.rosie.R;
import com.karumi.rosie.demo.base.view.transformation.RoundAvatarTransformation;
import com.karumi.rosie.demo.hipsterlist.view.adapter.FeedAdapter;
import com.karumi.rosie.demo.hipsterlist.view.presenter.HipsterListPresenter;
import com.karumi.rosie.demo.hipsterlist.view.HipsterListViewModule;
import com.karumi.rosie.demo.hipsterlist.view.model.HipsterViewModel;
import com.karumi.rosie.view.activity.RosieActivity;
import com.karumi.rosie.view.presenter.annotation.Presenter;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;
import java.util.Arrays;
import java.util.List;
import javax.inject.Inject;

/**
 * A placeholder fragment containing a simple view.
 */
public class HipsterListActivity extends RosieActivity implements HipsterListPresenter.View {

  @Inject @Presenter public HipsterListPresenter presenter;
  @Inject Picasso picasso;
  private Transformation transformationAvatar;

  @InjectView(R.id.lv_feed)
  ListView listviewFeed;

  private FeedAdapter feedAdapter;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    setContentView(R.layout.activity_hipster_list);
    super.onCreate(savedInstanceState);
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
      transformationAvatar = new RoundAvatarTransformation();
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
    return Arrays.asList((Object) new HipsterListViewModule());
  }
}
