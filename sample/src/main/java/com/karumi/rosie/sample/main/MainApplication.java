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

package com.karumi.rosie.sample.main;

import com.karumi.rosie.application.RosieApplication;
import dagger.ObjectGraph;
import java.util.Arrays;
import java.util.List;

/**
 * Sample application that registers the global application dependencies.
 */
public class MainApplication extends RosieApplication {
  private ObjectGraph fakeObjectGraph;

  @Override protected List<Object> getApplicationModules() {
    return Arrays.asList((Object) new ApplicationModule());
  }

  public void replaceGraph(ObjectGraph objectGraph) {
    this.fakeObjectGraph = objectGraph;
  }

  @Override public ObjectGraph plusGraph(List<Object> activityScopeModules) {
    ObjectGraph newObjectGraph = null;
    if (fakeObjectGraph == null) {
      newObjectGraph = super.plusGraph(activityScopeModules);
    } else {
      newObjectGraph = fakeObjectGraph.plus(activityScopeModules.toArray());
    }
    return newObjectGraph;
  }

  public void resetFakeGraph() {
    fakeObjectGraph = null;
  }
}
