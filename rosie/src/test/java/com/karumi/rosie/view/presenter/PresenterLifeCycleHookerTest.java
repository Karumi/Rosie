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

package com.karumi.rosie.view.presenter;

import org.junit.Test;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class PresenterLifeCycleHookerTest {

  @Test
  public void shouldCallAllRegisteredPresenersAreCalledWhenInitializateIsCalled() {
    PresenterLifeCycleHooker presenterLifeCycleHooker = new PresenterLifeCycleHooker();
    Presenter anyPresenter1 = mock(Presenter.class);
    Presenter anyPresenter2 = mock(Presenter.class);
    presenterLifeCycleHooker.registerPresenter(anyPresenter1);
    presenterLifeCycleHooker.registerPresenter(anyPresenter2);

    presenterLifeCycleHooker.presentersInitialize();

    verify(anyPresenter1).initialize();
    verify(anyPresenter2).initialize();
  }

  @Test
  public void shouldCallAllRegisteredPresenersAreCalledWhenUpdateIsCalled() {
    PresenterLifeCycleHooker presenterLifeCycleHooker = new PresenterLifeCycleHooker();
    Presenter anyPresenter1 = mock(Presenter.class);
    Presenter anyPresenter2 = mock(Presenter.class);
    presenterLifeCycleHooker.registerPresenter(anyPresenter1);
    presenterLifeCycleHooker.registerPresenter(anyPresenter2);

    presenterLifeCycleHooker.presentersUpdate();

    verify(anyPresenter1).update();
    verify(anyPresenter2).update();
  }

  @Test
  public void shouldCallAllRegisteredPresenersAreCalledWhenDestroyIsCalled() {
    PresenterLifeCycleHooker presenterLifeCycleHooker = new PresenterLifeCycleHooker();
    Presenter anyPresenter1 = mock(Presenter.class);
    Presenter anyPresenter2 = mock(Presenter.class);
    presenterLifeCycleHooker.registerPresenter(anyPresenter1);
    presenterLifeCycleHooker.registerPresenter(anyPresenter2);

    presenterLifeCycleHooker.presentersDestroy();

    verify(anyPresenter1).destroy();
    verify(anyPresenter2).destroy();
  }

  @Test
  public void shouldCallAllRegisteredPresenersAreCalledWhenPauseIsCalled() {
    PresenterLifeCycleHooker presenterLifeCycleHooker = new PresenterLifeCycleHooker();
    Presenter anyPresenter1 = mock(Presenter.class);
    Presenter anyPresenter2 = mock(Presenter.class);
    presenterLifeCycleHooker.registerPresenter(anyPresenter1);
    presenterLifeCycleHooker.registerPresenter(anyPresenter2);

    presenterLifeCycleHooker.presentersPause();

    verify(anyPresenter1).pause();
    verify(anyPresenter2).pause();
  }
}