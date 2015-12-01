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

package com.karumi.rosie.module;

import android.app.Activity;
import android.content.Context;
import com.karumi.rosie.daggerutils.ForActivity;
import dagger.Module;
import dagger.Provides;

/**
 * Dagger module created to provide activity scope dependencies like the Context associated to the
 * current Activity. Requires an instance of the current Activity to be able to provide it.
 */
@Module(library = true) public class RosieActivityModule {

  private final Context context;

  public RosieActivityModule(Activity activity) {
    validateActivity(activity);
    this.context = activity;
  }

  @ForActivity @Provides Context provideCurrentActivityContext() {
    return context;
  }

  private void validateActivity(Activity activity) {
    if (activity == null) {
      throw new IllegalArgumentException("The Activity passed in construction can't be null.");
    }
  }
}
