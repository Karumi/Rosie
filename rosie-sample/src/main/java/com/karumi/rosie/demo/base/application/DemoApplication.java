package com.karumi.rosie.demo.base.application;

import com.karumi.rosie.application.RosieApplication;
import java.util.Arrays;
import java.util.List;

/**
 *
 */
public class DemoApplication extends RosieApplication {

  @Override public List<Object> provideApplicationModules() {
    return Arrays.asList((Object) new GlobalDemoAndroidModule());
  }
}
