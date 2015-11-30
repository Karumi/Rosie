/*
 * Copyright (C) 2015 Karumi.
 */

package com.karumi.rosie.sample.comics.domain.model;

import com.karumi.rosie.repository.datasource.Identifiable;

public class Comic implements Identifiable<Integer> {

  private int key;
  private String name;
  private String description;
  private String coverUrl;
  private int releaseYear;

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
}
