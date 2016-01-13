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

package com.karumi.rosie.sample.characters.repository.datasource.mapper;

import com.karumi.marvelapiclient.model.MarvelImage;
import com.karumi.marvelapiclient.model.SeriesDto;
import com.karumi.rosie.mapper.Mapper;
import com.karumi.rosie.sample.comics.domain.model.Comic;
import com.karumi.rosie.sample.comics.domain.model.ComicSeries;
import java.util.ArrayList;
import java.util.List;

public class ComicSeriesToSeriesDtoMapper extends Mapper<ComicSeries, SeriesDto> {

  @Override public SeriesDto map(ComicSeries value) {
    throw new UnsupportedOperationException();
  }

  @Override public ComicSeries reverseMap(SeriesDto value) {
    ComicSeries comicSeries = new ComicSeries();
    comicSeries.setDescription(value.getDescription());
    comicSeries.setName(value.getTitle());
    comicSeries.setKey(Integer.valueOf(value.getId()));
    comicSeries.setReleaseYear(value.getStartYear());
    comicSeries.setCoverUrl(value.getThumbnail().getImageUrl(MarvelImage.Size.PORTRAIT_UNCANNY));
    comicSeries.setRating(value.getRating());
    List<Comic> comics = new ArrayList<>();
    comicSeries.setComics(comics);

    return comicSeries;
  }
}
