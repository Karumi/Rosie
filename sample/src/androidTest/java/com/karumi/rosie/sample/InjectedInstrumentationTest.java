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

import android.app.Instrumentation;
import android.content.Context;
import android.support.test.InstrumentationRegistry;
import com.karumi.rosie.sample.main.MainApplication;
import dagger.ObjectGraph;
import java.util.LinkedList;
import java.util.List;
import org.junit.After;
import org.junit.Before;

/**
 * Base test class created to be extended by every instrumentation test injecting custom
 * dependencies in this project.
 */

public abstract class InjectedInstrumentationTest {

  @Before public void setUp() {
    MainApplication application = getApplication();
    List<Object> childTestModules = getTestModules();
    Context context = InstrumentationRegistry.getInstrumentation().getTargetContext();
    List<Object> testModules = new LinkedList<>(childTestModules);
    testModules.add(new BaseTestModule(context));
    ObjectGraph objectGraph = application.plusGraph(testModules);
    application.replaceGraph(objectGraph);
    objectGraph.inject(this);
  }

  @After public void tearDown() throws Exception {
    MainApplication application = getApplication();
    application.resetFakeGraph();
  }

  private MainApplication getApplication() {
    Instrumentation instrumentation = InstrumentationRegistry.getInstrumentation();
    MainApplication app = (MainApplication) instrumentation.getTargetContext()
        .getApplicationContext();
    return app;
  }

  public abstract List<Object> getTestModules();
}


