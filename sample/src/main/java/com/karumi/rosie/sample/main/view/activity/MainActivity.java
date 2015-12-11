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
import butterknife.Bind;
import com.karumi.rosie.sample.R;
import com.karumi.rosie.sample.characters.view.fragment.CharactersFragment;
import com.karumi.rosie.sample.comics.view.fragment.ComicSeriesFragment;
import com.karumi.rosie.sample.main.MainModule;
import com.karumi.rosie.sample.main.view.adapter.FragmentAdapter;
import com.karumi.rosie.view.RosieActivity;
import java.util.Arrays;
import java.util.List;

public class MainActivity extends RosieActivity {

  @Bind(R.id.vp_main) ViewPager viewPager;
  @Bind(R.id.tab_page_indicator) TabLayout pagerTabView;
  private FragmentAdapter adapter;

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
}
