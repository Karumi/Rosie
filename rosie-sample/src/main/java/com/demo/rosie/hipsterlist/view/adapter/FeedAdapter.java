package com.demo.rosie.hipsterlist.view.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.demo.rosie.R;
import com.demo.rosie.hipsterlist.view.model.HipsterViewModel;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;
import java.util.List;

/**
 *
 */
public class FeedAdapter extends BaseAdapter {
  private final Context context;
  private final List<HipsterViewModel> hipsters;
  private final Picasso picasso;
  private final Transformation transformation;

  public FeedAdapter(Context context, List<HipsterViewModel> hipsters, Picasso picasso,
      Transformation transformation) {
    this.context = context;
    this.hipsters = hipsters;
    this.picasso = picasso;
    this.transformation = transformation;
  }

  @Override public int getCount() {
    return hipsters.size();
  }

  @Override public Object getItem(int position) {
    HipsterViewModel hipsterViewModel = hipsters.get(position);
    return hipsterViewModel;
  }

  @Override public long getItemId(int position) {
    return position;
  }

  @Override public View getView(int position, View convertView, ViewGroup parent) {
    if (convertView == null) {
      convertView = LayoutInflater.from(context).inflate(R.layout.row_hipster, parent, false);
    }

    ImageView avatarView = (ImageView) convertView.findViewById(R.id.iv_avatar);
    TextView nameTitleView = (TextView) convertView.findViewById(R.id.tv_hipster_name);

    HipsterViewModel hipsterViewModel = hipsters.get(position);

    nameTitleView.setText(hipsterViewModel.getName());

    if(hipsterViewModel.getAvatarUrl() != null) {
      picasso.load(hipsterViewModel.getAvatarUrl())
          .fit()
          .centerCrop()
          .placeholder(R.drawable.placeholder)
          .transform(transformation)
          .into(avatarView);
    }else{
      avatarView.setImageResource(R.drawable.placeholder);
    }

    return convertView;
  }
}
