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
