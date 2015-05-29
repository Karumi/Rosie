/*
 * The MIT License (MIT) Copyright (c) 2014 karumi Permission is hereby granted, free of charge,
 * to any person obtaining a copy of this software and associated documentation files (the
 * "Software"), to deal in the Software without restriction, including without limitation the
 * rights to use, copy, modify, merge, publish, distribute, sublicense,
 * and/or sell copies of the Software, and to permit persons to whom the Software is furnished to
 * do so, subject to the following conditions: The above copyright notice and this permission
 * notice shall be included in all copies or substantial portions of the Software. THE SOFTWARE
 * IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED,
 * INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR
 * COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY,
 * WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package com.karumi.rosie.demo.hipsterdetail.view.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import butterknife.InjectView;
import com.karumi.rosie.demo.R;
import com.karumi.rosie.demo.hipsterlist.view.model.Hipster;
import com.karumi.rosie.view.fragment.RosieFragment;
import com.squareup.picasso.Picasso;

/**
 * RosieFragment extension created to show detailed information related to a Hipster instance
 * passed as argument.
 */
public class HipsterDetailFragment extends RosieFragment {

  public static final String HIPSTER_EXTRA_KEY = "HipsterDetailFragment.hipsterKey";

  @InjectView(R.id.iv_avatar) ImageView hipsterAvatarView;
  @InjectView(R.id.tv_hipster_name) TextView hipsterNameView;

  @Override public View onCreateView(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    super.onCreateView(inflater, container, savedInstanceState);
    return inflater.inflate(R.layout.fragment_hipster_detail, container, false);
  }

  @Override public void onViewCreated(View view, Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
    Hipster hipster = getHipster();
    showHipsterPhoto(hipster.getAvatarUrl());
    showHipsterName(hipster.getName());
  }

  private void showHipsterPhoto(String avatarUrl) {
    Picasso.with(getActivity()).load(avatarUrl).into(hipsterAvatarView);
  }

  private void showHipsterName(String name) {
    hipsterNameView.setText(name);
  }

  @Override protected boolean shouldInjectFragment() {
    return false;
  }

  private Hipster getHipster() {
    return (Hipster) getActivity().getIntent().getExtras().get(HIPSTER_EXTRA_KEY);
  }
}
