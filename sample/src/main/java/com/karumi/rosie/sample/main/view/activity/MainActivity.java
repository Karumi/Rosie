/*
 * Copyright (C) 2015 Karumi.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.karumi.rosie.sample.main.view.activity;

import android.app.Fragment;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.TextView;
import butterknife.BindView;
import com.karumi.rosie.sample.R;
import com.karumi.rosie.sample.base.view.activity.MarvelActivity;
import com.karumi.rosie.sample.characters.view.fragment.CharactersFragment;
import com.karumi.rosie.sample.comics.view.fragment.ComicSeriesFragment;
import com.karumi.rosie.sample.main.MainModule;
import com.karumi.rosie.sample.main.view.adapter.FragmentAdapter;
import com.karumi.rosie.sample.main.view.presenter.FakeDataPresenter;
import com.karumi.rosie.view.Presenter;
import java.util.Arrays;
import java.util.List;
import javax.inject.Inject;

public class MainActivity extends MarvelActivity implements FakeDataPresenter.View {

  @BindView(R.id.vp_main) ViewPager viewPager;
  @BindView(R.id.tab_page_indicator) TabLayout pagerTabView;
  @BindView(R.id.tv_disclaimer) TextView disclaimerView;

  private FragmentAdapter adapter;
  @Inject @Presenter FakeDataPresenter fakeDataPresenter;

  @Override protected int getLayoutId() {
    return R.layout.activity_main;
  }

  @Override protected List<Object> getActivityScopeModules() {
    return Arrays.asList((Object) new MainModule());
  }

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    initializeViewPager();
  }

  private void initializeViewPager() {
    adapter = new FragmentAdapter(getFragmentManager());
    viewPager.setAdapter(adapter);

    Fragment charactersFragment = new CharactersFragment();
    Fragment comicsFragment = new ComicSeriesFragment();
    adapter.addFragment(charactersFragment, getString(R.string.characters_page_title));
    adapter.addFragment(comicsFragment, getString(R.string.comic_series_page_title));
    adapter.notifyDataSetChanged();

    pagerTabView.setupWithViewPager(viewPager);
  }

  @Override public void showFakeDisclaimer() {
    disclaimerView.setVisibility(View.VISIBLE);
  }

  @Override public void hideFakeDisclaimer() {
    disclaimerView.setVisibility(View.GONE);
  }

  @Override public void hideLoading() {
  }

  @Override public void showLoading() {
  }
}
