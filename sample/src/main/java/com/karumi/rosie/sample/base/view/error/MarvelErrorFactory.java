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

package com.karumi.rosie.sample.base.view.error;

import com.karumi.marvelapiclient.MarvelApiException;
import com.karumi.rosie.domain.usecase.error.ErrorFactory;
import java.lang.reflect.InvocationTargetException;
import java.net.UnknownHostException;
import javax.inject.Inject;

public class MarvelErrorFactory extends ErrorFactory {

  @Inject public MarvelErrorFactory() {
  }

  @Override public Error create(Exception exception) {
    Throwable targetException = exception;
    if (exception instanceof InvocationTargetException) {
      targetException = ((InvocationTargetException) exception).getTargetException();
    }

    if (targetException instanceof MarvelApiException) {
      MarvelApiException marvelApiException = (MarvelApiException) targetException;
      if (marvelApiException.getCause() instanceof UnknownHostException) {
        return new ConnectionError();
      }
    }
    return new DefaultError();
  }
}
