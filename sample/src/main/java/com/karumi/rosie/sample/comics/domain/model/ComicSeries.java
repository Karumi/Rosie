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

package com.karumi.rosie.sample.comics.domain.model;

import com.karumi.rosie.repository.datasource.Identifiable;
import java.util.ArrayList;
import java.util.List;

public class ComicSeries implements Identifiable<Integer> {

  private int key;
  private String name;
  private String description;
  private String coverUrl;
  private int releaseYear;
  private String rating;
  private List<Comic> comics = new ArrayList<>();

  @Override public Integer getKey() {
    return key;
  }

  public void setKey(int key) {
    this.key = key;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public String getCoverUrl() {
    return coverUrl;
  }

  public void setCoverUrl(String coverUrl) {
    this.coverUrl = coverUrl;
  }

  public int getReleaseYear() {
    return releaseYear;
  }

  public void setReleaseYear(int releaseYear) {
    this.releaseYear = releaseYear;
  }

  public String getRating() {
    return rating;
  }

  public void setRating(String rating) {
    this.rating = rating;
  }

  public List<Comic> getComics() {
    return comics;
  }

  public void setComics(List<Comic> comics) {
    this.comics = comics;
  }

}
