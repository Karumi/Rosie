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

package com.karumi.rosie.doubles;

import com.karumi.rosie.TestModule;
import com.karumi.rosie.view.RosieActivity;
import com.karumi.rosie.view.RosiePresenter;
import com.karumi.rosie.view.Presenter;
import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.mock;

/**
 * Activity for run the tests
 */
public class FakeActivity extends RosieActivity {

  private static final int ANY_LAYOUT = android.R.layout.list_content;

  public FakeActivity() {
  }

  @Presenter RosiePresenter presenter = mock(RosiePresenter.class);

  @Override protected int getLayoutId() {
    return ANY_LAYOUT;
  }

  @Override protected List<Object> getActivityScopeModules() {
    return Arrays.asList((Object) new TestModule());
  }

  public RosiePresenter getPresenter() {
    return presenter;
  }
}
