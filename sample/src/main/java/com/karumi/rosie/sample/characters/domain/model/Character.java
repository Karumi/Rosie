/*
 * Copyright (C) 2015 Karumi.
 */

package com.karumi.rosie.sample.characters.domain.model;

import com.karumi.rosie.repository.datasource.Identifiable;

public class Character implements Identifiable<String> {

  public String id;
  public String name;
  public String description;
  public String thumbnailUrl;

  @Override public String getKey() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
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

  public String getThumbnailUrl() {
    return thumbnailUrl;
  }

  public void setThumbnailUrl(String thumbnailUrl) {
    this.thumbnailUrl = thumbnailUrl;
  }
}
