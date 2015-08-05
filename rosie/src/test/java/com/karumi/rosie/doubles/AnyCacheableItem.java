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

  @Override public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    AnyCacheableItem that = (AnyCacheableItem) o;

    return !(id != null ? !id.equals(that.id) : that.id != null);
  }

  @Override public int hashCode() {
    return id != null ? id.hashCode() : 0;
  }
}
