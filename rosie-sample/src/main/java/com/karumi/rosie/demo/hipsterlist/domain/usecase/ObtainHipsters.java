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

package com.karumi.rosie.demo.hipsterlist.domain.usecase;

import com.karumi.rosie.demo.hipsterlist.view.model.Hipster;
import com.karumi.rosie.domain.usecase.RosieUseCase;
import com.karumi.rosie.domain.usecase.annotation.UseCase;
import java.util.ArrayList;
import java.util.List;

/**
 * This UseCase provide hipsters from dummy data.
 */
public class ObtainHipsters extends RosieUseCase {

  @UseCase
  public void obtain() {
    Hipster hipster = new Hipster();
    hipster.setName("Hipstotito Fernandez");
    hipster.setAvatarUrl(
        "https://cdn0.iconfinder.com/data/icons/avatars-3/512/avatar_hipster_guy-512.png");
    hipster.setId("1");

    List<Hipster> hipsters = new ArrayList<Hipster>();
    hipsters.add(hipster);

    try {
      Thread.sleep(5000);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }

    notifySuccess(hipsters);
  }
}
