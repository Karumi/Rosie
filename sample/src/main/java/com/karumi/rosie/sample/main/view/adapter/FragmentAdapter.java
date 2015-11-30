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

  private final List<ViewPagerFragmentHolder> fragments = new ArrayList<>();

  public FragmentAdapter(FragmentManager fm) {
    super(fm);
  }

  @Override public Fragment getItem(int position) {
    ViewPagerFragmentHolder fragmentHolder = fragments.get(position);
    return fragmentHolder.fragment;
  }

  @Override public int getCount() {
    return fragments.size();
  }

  @Override public CharSequence getPageTitle(int position) {
    ViewPagerFragmentHolder fragmentHolder = fragments.get(position);
    return fragmentHolder.pageTitle;
  }

  public void addFragment(Fragment fragment, String pageTitle) {
    fragments.add(new ViewPagerFragmentHolder(fragment, pageTitle));
  }

  private static class ViewPagerFragmentHolder {
    public final Fragment fragment;
    public final String pageTitle;

    public ViewPagerFragmentHolder(Fragment fragment, String pageTitle) {
      this.fragment = fragment;
      this.pageTitle = pageTitle;
    }
  }
}
