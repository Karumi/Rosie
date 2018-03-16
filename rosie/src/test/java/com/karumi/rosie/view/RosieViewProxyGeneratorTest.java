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

package com.karumi.rosie.view;

import org.junit.Test;

import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertTrue;

public class RosieViewProxyGeneratorTest {

  private final RosieViewProxyGenerator<C> generator = new RosieViewProxyGenerator<>();
  private final C anyClassExtendingInterfaces = new C();

  @Test
  public void shouldGenerateAClassImplementingBothInterfacesExtendingFromRosieView() {
    A a = generator.generate(anyClassExtendingInterfaces);
    B b = generator.generate(anyClassExtendingInterfaces);

    assertTrue(a instanceof A);
    assertTrue(a instanceof B);
    assertTrue(b instanceof A);
    assertTrue(b instanceof B);
  }

  @Test public void shouldNotGenerateTheClassUsingTheInstancesNotExtendingFromRosieView() {
    A a = generator.generate(anyClassExtendingInterfaces);

    assertFalse(a instanceof NotARosieView);
  }

  interface A extends RosiePresenter.View {

  }

  interface B extends RosiePresenter.View {

  }

  interface NotARosieView {

  }

  class C implements A, B, NotARosieView {

  }
}