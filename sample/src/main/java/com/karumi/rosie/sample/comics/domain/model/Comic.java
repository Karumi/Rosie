/*
 * Copyright (C) 2015 Karumi.
 */

package com.karumi.rosie.sample.comics.domain.model;

import com.karumi.rosie.repository.datasource.Identifiable;

public class Comic implements Identifiable<Integer> {

  private int key;
  private String name;
  private int number;
  private String description;
  private String coverUrl;
  private int releaseYear;
  private Rating rating;

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

  public int getNumber() {
    return number;
  }

  public void setNumber(int number) {
    this.number = number;
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

  public Rating getRating() {
    return rating;
  }

  public void setRating(Rating rating) {
    this.rating = rating;
  }

  public enum Rating {
    ALL_AGES,
    T,
    TEENS_AND_UP,
    PARENTAL_ADVISORY,
    EXPLICIT_CONTENT
  }
}
