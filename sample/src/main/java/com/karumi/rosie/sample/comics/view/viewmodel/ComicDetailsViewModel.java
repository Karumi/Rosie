/*
 * Copyright (C) 2015 Karumi.
 */

package com.karumi.rosie.sample.comics.view.viewmodel;

public class ComicDetailsViewModel {

  private String title;
  private String coverUrl;
  private String description;
  private int ratingNameResourceId;

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public String getCoverUrl() {
    return coverUrl;
  }

  public void setCoverUrl(String coverUrl) {
    this.coverUrl = coverUrl;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public int getRatingNameResourceId() {
    return ratingNameResourceId;
  }

  public void setRatingNameResourceId(int ratingNameResourceId) {
    this.ratingNameResourceId = ratingNameResourceId;
  }
}
