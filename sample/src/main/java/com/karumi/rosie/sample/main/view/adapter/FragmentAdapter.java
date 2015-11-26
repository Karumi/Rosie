/*
 * Copyright (C) 2015 Karumi.
 */

package com.karumi.rosie.sample.main.view.adapter;

import android.app.Fragment;
import android.app.FragmentManager;
import android.support.v13.app.FragmentStatePagerAdapter;
import java.util.ArrayList;
import java.util.List;

public class FragmentAdapter extends FragmentStatePagerAdapter {

  private final List<Fragment> fragments = new ArrayList<>();

  public FragmentAdapter(FragmentManager fm) {
    super(fm);
  }

  @Override public Fragment getItem(int position) {
    return fragments.get(position);
  }

  @Override public int getCount() {
    return fragments.size();
  }

  @Override public CharSequence getPageTitle(int position) {
    return "Page " + position;
  }

  public void addFragment(Fragment fragment) {
    fragments.add(fragment);
  }
}
