/*
 * Copyright (C) 2015 Karumi.
 */

package com.karumi.rosie.sample.main;

import com.karumi.rosie.application.RosieApplication;
import java.util.Arrays;
import java.util.List;

public class MainApplication extends RosieApplication {

  @Override protected List<Object> getApplicationModules() {
    return Arrays.asList((Object) new MainModule());
  }
}
