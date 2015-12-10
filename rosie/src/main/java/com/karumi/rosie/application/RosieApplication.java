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
   * to resolve all the dependencies needed by the object and inject them.
   */
  public final void inject(Object object) {
    graph.inject(object);
  }

  /**
   * Given a List<Object> with Dagger modules inside performs a plus over the application graph and
   * returns a new one with all the dependencies already created plus the one resolved with the
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
    return new ArrayList<>();
  }

  /**
   * Indicates if the class has to be injected or not. Override this method and return false to use
   * RosieApplication without injecting any dependency.
   */
  protected boolean shouldInjectApplication() {
    return true;
  }

  private void initGraph() {
    List<Object> rosieModules = getRosieModules();
    List<Object> modules = new ArrayList<>(rosieModules);
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
