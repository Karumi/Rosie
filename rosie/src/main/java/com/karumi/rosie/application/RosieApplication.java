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

package com.karumi.rosie.application;

import android.app.Application;
import com.karumi.rosie.module.RosieAndroidModule;
import dagger.ObjectGraph;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 *
 */
public class RosieApplication extends Application {
  private ObjectGraph graph;

  @Override
  public void onCreate() {
    super.onCreate();

    initGraph();
  }

  private void initGraph() {
    List<Object> modules = new ArrayList<Object>();
    List<Object> rosieModules = provideRosieModules();
    List<Object> applicationModules = provideApplicationModules();

    modules.addAll(rosieModules);
    if (applicationModules != null) {
      modules.addAll(applicationModules);
    }

    graph = ObjectGraph.create(modules.toArray());
    graph.injectStatics();
  }

  private List<Object> provideRosieModules() {
    return Arrays.asList((Object) new RosieAndroidModule(this));
  }

  public List<Object> provideApplicationModules() {
    return null;
  }

  public void inject(Object object) {
    graph.inject(object);
  }

  public ObjectGraph plusModules(List<Object> activityScopeModules) {
    if (activityScopeModules == null) {
      throw new IllegalArgumentException(
          "You can't extend the application graph with a null list of modules");
    }
    return graph.plus(activityScopeModules.toArray());
  }
}
