package com.karumi.rosie.demo.hipsterlist.view.renderer;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import butterknife.InjectView;
import com.demo.rosie.R;
import com.karumi.rosie.demo.base.view.transformation.RoundAvatarTransformation;
import com.karumi.rosie.demo.hipsterlist.view.model.Hipster;
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

  private void renderHipsterAvatar(String avatarUrl) {
    Picasso.with(context).load(avatarUrl)
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
