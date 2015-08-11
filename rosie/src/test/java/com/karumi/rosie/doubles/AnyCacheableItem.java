package com.karumi.rosie.doubles;

import com.karumi.rosie.repository.Cacheable;

public class AnyCacheableItem implements Cacheable {

  private String id;

  public AnyCacheableItem() {
    this(null);
  }

  public AnyCacheableItem(String id) {
    this.id = id;
  }

  @Override public String getId() {
    return id;
  }
}
