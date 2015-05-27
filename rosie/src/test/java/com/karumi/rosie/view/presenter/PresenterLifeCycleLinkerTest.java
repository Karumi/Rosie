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

import com.karumi.rosie.view.presenter.view.ErrorView;
import org.junit.Test;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class PresenterLifeCycleLinkerTest {

  @Test public void shouldCallAllRegisteredPresentersAreCalledWhenInitializeIsCalled() {
    RosiePresenter anyPresenter1 = mock(RosiePresenter.class);
    RosiePresenter anyPresenter2 = mock(RosiePresenter.class);
    PresenterLifeCycleLinker presenterLifeCycleLinker =
        givenAPresenterLifecycleLinker(anyPresenter1, anyPresenter2);

    presenterLifeCycleLinker.initializePresenters();

    verify(anyPresenter1).initialize();
    verify(anyPresenter2).initialize();
  }

  @Test public void shouldCallAllRegisteredPresentersAreCalledWhenUpdateIsCalled() {
    RosiePresenter anyPresenter1 = mock(RosiePresenter.class);
    RosiePresenter anyPresenter2 = mock(RosiePresenter.class);
    PresenterLifeCycleLinker presenterLifeCycleLinker =
        givenAPresenterLifecycleLinker(anyPresenter1, anyPresenter2);

    presenterLifeCycleLinker.updatePresenters();

    verify(anyPresenter1).update();
    verify(anyPresenter2).update();
  }

  @Test public void shouldCallAllRegisteredPresentersAreCalledWhenDestroyIsCalled() {
    RosiePresenter anyPresenter1 = mock(RosiePresenter.class);
    RosiePresenter anyPresenter2 = mock(RosiePresenter.class);
    PresenterLifeCycleLinker presenterLifeCycleLinker =
        givenAPresenterLifecycleLinker(anyPresenter1, anyPresenter2);

    presenterLifeCycleLinker.destroyPresenters();

    verify(anyPresenter1).destroy();
    verify(anyPresenter2).destroy();
  }

  @Test public void shouldCallAllRegisteredPresentersAreCalledWhenPauseIsCalled() {
    RosiePresenter anyPresenter1 = mock(RosiePresenter.class);
    RosiePresenter anyPresenter2 = mock(RosiePresenter.class);
    PresenterLifeCycleLinker presenterLifeCycleLinker =
        givenAPresenterLifecycleLinker(anyPresenter1, anyPresenter2);

    presenterLifeCycleLinker.pausePresenters();

    verify(anyPresenter1).pause();
    verify(anyPresenter2).pause();
  }

  @Test public void shouldConfigureViewToEveryPresenterRegistered() {
    RosiePresenter anyPresenter1 = mock(RosiePresenter.class);
    RosiePresenter anyPresenter2 = mock(RosiePresenter.class);
    PresenterLifeCycleLinker presenterLifeCycleLinker =
        givenAPresenterLifecycleLinker(anyPresenter1, anyPresenter2);

    RosiePresenter.View view = mock(RosiePresenter.View.class);
    presenterLifeCycleLinker.setView(view);

    verify(anyPresenter1).setView(view);
    verify(anyPresenter2).setView(view);
  }

  @Test public void shouldConfigureErrorViewToEveryPresenterRegistered() {
    RosiePresenter anyPresenter1 = mock(RosiePresenter.class);
    RosiePresenter anyPresenter2 = mock(RosiePresenter.class);
    PresenterLifeCycleLinker presenterLifeCycleLinker =
        givenAPresenterLifecycleLinker(anyPresenter1, anyPresenter2);

    ErrorView errorView = mock(ErrorView.class);
    presenterLifeCycleLinker.setErrorView(errorView);

    verify(anyPresenter1).setErrorView(errorView);
    verify(anyPresenter2).setErrorView(errorView);
  }

  private PresenterLifeCycleLinker givenAPresenterLifecycleLinker(RosiePresenter... presenters) {
    PresenterLifeCycleLinker presenterLifeCycleLinker = new PresenterLifeCycleLinker();
    for (RosiePresenter presenter : presenters) {
      presenterLifeCycleLinker.registerPresenter(presenter);
    }
    return presenterLifeCycleLinker;
  }
}