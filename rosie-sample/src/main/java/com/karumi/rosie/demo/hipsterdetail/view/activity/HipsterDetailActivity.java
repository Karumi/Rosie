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

package com.karumi.rosie.demo.hipsterdetail.view.activity;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import com.karumi.rosie.demo.R;
import com.karumi.rosie.demo.hipsterdetail.view.fragment.HipsterDetailFragment;
import com.karumi.rosie.demo.hipsterlist.view.model.Hipster;
import com.karumi.rosie.view.activity.RosieActivity;

/**
 * RosieActivity extension created to show a Hipster detailed information.
 */
public class HipsterDetailActivity extends RosieActivity {

  @Override protected void onCreate(Bundle savedInstanceState) {
    setContentView(R.layout.activity_hipster_detail);
    super.onCreate(savedInstanceState);
    if (savedInstanceState == null) {
      Fragment fragment = new HipsterDetailFragment();
      fragment.setArguments(getIntent().getExtras());

      FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
      fragmentTransaction.add(R.id.fragment_container, fragment).commit();
    }
  }

  public static void open(Context context, Hipster hipster) {
    Intent intent = new Intent(context, HipsterDetailActivity.class);
    intent.putExtra(HipsterDetailFragment.HIPSTER_EXTRA_KEY, hipster);
    context.startActivity(intent);
  }
}
