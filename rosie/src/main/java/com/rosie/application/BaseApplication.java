package com.rosie.application;

import android.app.Application;

import com.rosie.module.RosieAndroidModule;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import dagger.ObjectGraph;

/**
 *
 */
public class BaseApplication extends Application {
  private ObjectGraph graph;

  @Override
  public void onCreate() {
    super.onCreate();

    initGraph();
  }

  private void initGraph() {
    List<Object> modules = new ArrayList<Object>();
    List<Object> rosieModules = getRosieModules();
    List<Object> applicationModules = getApplicationModules();

    modules.addAll(rosieModules);
    if (applicationModules != null) {
      modules.addAll(applicationModules);
    }

    graph = ObjectGraph.create(modules.toArray());
    graph.injectStatics();
  }

  private List<Object> getRosieModules() {
    return Arrays.asList((Object) new RosieAndroidModule(this));
  }

  public List<Object> getApplicationModules() {
    return null;
  }

  public void inject(Object object) {
    graph.inject(this);
  }

  public ObjectGraph plusModules(List<Object> activityScopeModules) {
    return graph.plus(activityScopeModules.toArray());
  }
}
