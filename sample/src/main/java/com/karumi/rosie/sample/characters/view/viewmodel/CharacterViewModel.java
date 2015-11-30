/*
 * Copyright (C) 2015 Karumi.
 */

package com.karumi.rosie.sample.characters.view.viewmodel;

public class CharacterViewModel {

  private String key;
  private String name;
  private String thumbnailUrl;

  public String getKey() {
    return key;
  }

  public void setKey(String key) {
    this.key = key;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getThumbnailUrl() {
    return thumbnailUrl;
  }

  public void setThumbnailUrl(String thumbnailUrl) {
    this.thumbnailUrl = thumbnailUrl;
  }
}
