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

package com.karumi.rosie.view.fragment;

import com.karumi.rosie.RobolectricTest;
import com.karumi.rosie.doubles.FakeActivity;
import com.karumi.rosie.view.presenter.RosiePresenter;
import com.karumi.rosie.view.presenter.annotation.Presenter;
import org.junit.Test;
import org.robolectric.util.FragmentTestUtil;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class RosieFragmentTest extends RobolectricTest {

  @Test
  public void shouldCallInitializePresenterWhenFragmentCreate() {

    TestFragment testFragment = new TestFragment();
    FragmentTestUtil.startFragment(testFragment, FakeActivity.class);

    verify(testFragment.presenter).initialize();
  }

  @Test
  public void shouldCallUpdatePresenterWhenFragmentResume() {

    TestFragment testFragment = new TestFragment();
    FragmentTestUtil.startFragment(testFragment, FakeActivity.class);

    verify(testFragment.presenter).initialize();
    verify(testFragment.presenter).update();
  }

  @Test
  public void shouldCallPausePresenterWhenFragmentPause() {

    TestFragment testFragment = new TestFragment();
    FragmentTestUtil.startFragment(testFragment, FakeActivity.class);
    testFragment.onPause();

    verify(testFragment.presenter).initialize();
    verify(testFragment.presenter).update();
    verify(testFragment.presenter).pause();
  }

  @Test
  public void shouldCallDestroyPresenterWhenFragmentDestroy() {

    TestFragment testFragment = new TestFragment();
    FragmentTestUtil.startFragment(testFragment, FakeActivity.class);
    testFragment.onPause();
    testFragment.onDestroy();

    verify(testFragment.presenter).initialize();
    verify(testFragment.presenter).update();
    verify(testFragment.presenter).pause();
    verify(testFragment.presenter).destroy();
  }


  public static class TestFragment extends RosieFragment {
    @Presenter
    public RosiePresenter presenter =
        mock(RosiePresenter.class);

    public TestFragment() {
    }

  }
}