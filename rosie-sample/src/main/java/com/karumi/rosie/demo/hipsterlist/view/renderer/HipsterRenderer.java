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

package com.karumi.rosie.demo.hipsterlist.view.renderer;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import butterknife.InjectView;
import butterknife.OnClick;
import com.karumi.rosie.demo.R;
import com.karumi.rosie.demo.base.view.transformation.RoundAvatarTransformation;
import com.karumi.rosie.demo.hipsterdetail.view.activity.HipsterDetailActivity;
import com.karumi.rosie.demo.hipsterlist.view.model.Hipster;
import com.karumi.rosie.renderer.RosieRenderer;
import com.squareup.picasso.Picasso;

/**
 * Renderer extension created to render Hipster instances into a ListView or RecyclerView
 */
public class HipsterRenderer extends RosieRenderer<Hipster> {

  private final Context context;

  @InjectView(R.id.iv_avatar) ImageView hipsterAvatarView;
  @InjectView(R.id.tv_hipster_name) TextView hipsterNameView;

  public HipsterRenderer(Context context) {
    this.context = context;
  }

  @Override protected View inflate(LayoutInflater layoutInflater, ViewGroup viewGroup) {
    return layoutInflater.inflate(R.layout.row_hipster, viewGroup, false);
  }

  @Override public void render() {
    super.render();
    Hipster hipster = getContent();
    renderHipsterAvatar(hipster.getAvatarUrl());
    renderHipsterName(hipster.getName());
  }

  @OnClick(R.id.rl_container) public void onHipsterClicked() {
    Hipster hipster = getContent();
    HipsterDetailActivity.open(context, hipster);
  }

  private void renderHipsterAvatar(String avatarUrl) {
    Picasso.with(context)
        .load(avatarUrl)
        .fit()
        .centerCrop()
        .placeholder(R.drawable.placeholder)
        .transform(new RoundAvatarTransformation())
        .into(hipsterAvatarView);
  }

  private void renderHipsterName(String name) {
    hipsterNameView.setText(name);
  }
}
