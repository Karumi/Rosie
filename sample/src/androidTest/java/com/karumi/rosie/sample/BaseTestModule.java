/*
 *  Copyright (C) 2015 Karumi.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package com.karumi.rosie.sample;

import android.content.Context;
import com.karumi.rosie.daggerutils.ForActivity;
import com.karumi.rosie.sample.main.MainModule;
import dagger.Module;
import dagger.Provides;

@Module(overrides = true, complete = false, library = true, includes = {
    MainModule.class
}) public class BaseTestModule {

  private final Context context;

  public BaseTestModule(Context context) {
    this.context = context;
  }

  @Provides @ForActivity Context provideActivityContext() {
    return context;
  }

}
