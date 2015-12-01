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

package com.karumi.rosie.view;

import com.karumi.rosie.RobolectricTest;
import com.karumi.rosie.doubles.FakeActivity;
import org.junit.Test;
import org.robolectric.Robolectric;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

public class RosieActivityTest extends RobolectricTest {

  @Test public void shouldCallInitializePresenterWhenActivityCreate() {
    FakeActivity fakeActivity = Robolectric.buildActivity(FakeActivity.class).create().get();
    verify(fakeActivity.getPresenter(), times(1)).initialize();
  }

  @Test public void shouldCallUpdatePresenterWhenActivityResume() {
    FakeActivity fakeActivity =
        Robolectric.buildActivity(FakeActivity.class).create().resume().get();

    verify(fakeActivity.getPresenter()).initialize();
    verify(fakeActivity.getPresenter()).update();
  }

  @Test public void shouldCallPausePresenterWhenActivityPause() {
    FakeActivity fakeActivity =
        Robolectric.buildActivity(FakeActivity.class).create().resume().pause().get();
    verify(fakeActivity.getPresenter()).initialize();
    verify(fakeActivity.getPresenter()).update();
    verify(fakeActivity.getPresenter()).pause();
  }

  @Test public void shouldCallPausePresenterAndRestartPresenterWhenActivityPauseAndRestart() {
    FakeActivity fakeActivity =
        Robolectric.buildActivity(FakeActivity.class).create().resume().pause().resume().get();
    verify(fakeActivity.getPresenter()).initialize();
    verify(fakeActivity.getPresenter(), times(2)).update();
    verify(fakeActivity.getPresenter()).pause();
  }

  @Test public void shouldCallDestroyPresenterWhenActivityDestroy() {
    FakeActivity fakeActivity =
        Robolectric.buildActivity(FakeActivity.class).create().destroy().get();
    verify(fakeActivity.getPresenter()).initialize();
    verify(fakeActivity.getPresenter()).destroy();
  }

  @Test
  public void shouldNotCrashIfSomeoneTriesToInjectAnObjectBeforeTheOnCreateMethodHasNotBeenCalled() {
    FakeActivity fakeActivity = Robolectric.buildActivity(FakeActivity.class).get();

    fakeActivity.inject(new Object());
  }
}