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
 * Application extension created to perform some of the most important tasks related with
 * dependency injection. This class creates an ObjectGraph, initializes all the modules associated
 * to the application scope graph and injects dependencies over objects passed as arguments or
 * extends the graph using the application graph created during the application onCreate method.
 *
 * Extend this class in your project to be able to use Rosie Framework.
 */
public class RosieApplication extends Application {
  private ObjectGraph graph;

  /**
   * Initializes the object graph associated to the application scope and injects the application
   * instance.
   */
  @Override public void onCreate() {
    super.onCreate();
    initGraph();
    injectApplication();
  }

  /**
   * Given an object passed as argument uses the object graph associated to the application scope
   * to
   * resolve all the dependencies needed by the object and inject them.
   */
  public void inject(Object object) {
    graph.inject(object);
  }

  /**
   * Given a List<Object> with Dagger modules inside performs a plus over the application graph and
   * returns an new one with all the dependencies already created plus the one resolved with the
   * list of modules passed as argument.
   */
  public ObjectGraph plusGraph(List<Object> activityScopeModules) {
    if (activityScopeModules == null) {
      throw new IllegalArgumentException(
          "You can't extend the application graph with a null list of modules");
    }
    return graph.plus(activityScopeModules.toArray());
  }

  /**
   * Returns a List<Object> with the additional modules needed to create the application scope
   * graph. Override this method to return the list of modules associated to your application
   * graph.
   */
  protected List<Object> getApplicationModules() {
    return new ArrayList<Object>();
  }

  /**
   * Indicates if the class has to be injected or not. Override this method and return true to use
   * RosieApplication without inject any dependency.
   */
  protected boolean shouldInjectApplication() {
    return true;
  }

  private void initGraph() {
    List<Object> rosieModules = getRosieModules();
    List<Object> modules = new ArrayList<Object>(rosieModules);
    List<Object> applicationModules = getApplicationModules();

    if (applicationModules != null) {
      modules.addAll(applicationModules);
    }
    graph = ObjectGraph.create(modules.toArray());
  }

  private void injectApplication() {
    if (shouldInjectApplication()) {
      graph.inject(this);
      graph.injectStatics();
    }
  }

  private List<Object> getRosieModules() {
    return Arrays.asList((Object) new RosieAndroidModule(this));
  }
}
