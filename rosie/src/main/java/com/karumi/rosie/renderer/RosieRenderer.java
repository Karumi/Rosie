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

package com.karumi.rosie.renderer;

import android.view.View;
import butterknife.ButterKnife;
import com.pedrogomez.renderers.Renderer;

/**
 * Renderer extension create to provide Butter Knife view injection in a transparent way. Your
 * Renderer classes should extend from this one to be able tu use Butter Knife annotations.
 * Remember to call super in you overridden render method.
 */
public abstract class RosieRenderer<T> extends Renderer<T> {

  @Override public void render() {
    ButterKnife.bind(this, getRootView());
  }

  @Override protected void setUpView(View view) {

  }

  @Override protected void hookListeners(View view) {

  }
}
