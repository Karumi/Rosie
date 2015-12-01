package com.karumi.rosie.sample.main.view.activity;

import android.app.Fragment;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import butterknife.InjectView;
import com.karumi.rosie.sample.R;
import com.karumi.rosie.sample.characters.view.fragment.CharactersFragment;
import com.karumi.rosie.sample.comics.view.fragment.ComicsFragment;
import com.karumi.rosie.sample.main.view.adapter.FragmentAdapter;
import com.karumi.rosie.view.RosieActivity;
import com.viewpagerindicator.TabPageIndicator;

public class MainActivity extends RosieActivity {

  @InjectView(R.id.vp_main) ViewPager viewPager;
  @InjectView(R.id.tab_page_indicator) TabPageIndicator pagerTabView;
  private FragmentAdapter adapter;

  @Override protected int getLayoutId() {
    return R.layout.activity_main;
  }

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    initializeViewPager();
  }

  private void initializeViewPager() {
    adapter = new FragmentAdapter(getFragmentManager());
    viewPager.setAdapter(adapter);

    Fragment charactersFragment = new CharactersFragment();
    Fragment comicsFragment = new ComicsFragment();
    adapter.addFragment(charactersFragment, getString(R.string.characters_page_title));
    adapter.addFragment(comicsFragment, getString(R.string.comics_page_title));
    adapter.notifyDataSetChanged();

    pagerTabView.setViewPager(viewPager);
  }
}
