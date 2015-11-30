/*
 * Copyright (C) 2015 Karumi.
 */

package com.karumi.rosie.sample.characters.view.viewmodel;

public class CharacterDetailViewModel {

  private String key;
  private String name;
  private String headerImage;
  private String description;

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

  public String getHeaderImage() {
    return headerImage;
  }

  public void setHeaderImage(String headerImage) {
    this.headerImage = headerImage;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }
}
