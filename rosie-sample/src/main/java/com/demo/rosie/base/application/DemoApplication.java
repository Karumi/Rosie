package com.demo.rosie.base.application;

import com.rosie.application.BaseApplication;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 *
 */
public class DemoApplication extends BaseApplication {

  @Override public List<Object> getApplicationModules() {
    return Arrays.asList((Object) new GlobalDemoAndroidModule());
  }
}
