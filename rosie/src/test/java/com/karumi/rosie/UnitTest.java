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

package com.karumi.rosie;

import org.junit.Before;
import org.mockito.MockitoAnnotations;

/**
 * Base test class created to perform some common operations like initialize the mocking framework
 * before each test. Every unit test in this repository should extend from this class.
 */
public class UnitTest {

  @Before public void setUpMockitoAnnotations() {
    MockitoAnnotations.initMocks(this);
  }
}
