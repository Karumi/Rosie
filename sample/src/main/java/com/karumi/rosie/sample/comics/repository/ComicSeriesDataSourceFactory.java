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

package com.karumi.rosie.sample.comics.repository;

import com.karumi.marvelapiclient.MarvelApiConfig;
import com.karumi.marvelapiclient.SeriesApiClient;
import com.karumi.rosie.sample.BuildConfig;
import com.karumi.rosie.sample.comics.repository.datasource.ComicSeriesApiDataSource;
import com.karumi.rosie.sample.comics.repository.datasource.ComicSeriesDataSource;
import com.karumi.rosie.sample.comics.repository.datasource.ComicSeriesFakeDataSource;
import javax.inject.Inject;

class ComicSeriesDataSourceFactory {

  @Inject ComicSeriesDataSourceFactory() {
  }

  ComicSeriesDataSource createDataSource() {
    if (hasKeys()) {
      MarvelApiConfig marvelApiConfig =
          MarvelApiConfig.with(BuildConfig.MARVEL_PUBLIC_KEY, BuildConfig.MARVEL_PRIVATE_KEY);
      SeriesApiClient seriesApiClient = new SeriesApiClient(marvelApiConfig);
      return new ComicSeriesApiDataSource(seriesApiClient);
    } else {
      return new ComicSeriesFakeDataSource();
    }
  }

  private boolean hasKeys() {
    return BuildConfig.MARVEL_PUBLIC_KEY != null && BuildConfig.MARVEL_PRIVATE_KEY != null;
  }
}
