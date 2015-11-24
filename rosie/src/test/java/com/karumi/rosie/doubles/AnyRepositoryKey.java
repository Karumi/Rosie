/*
 * Copyright (C) 2015 Karumi.
 */

package com.karumi.rosie.doubles;

public class AnyRepositoryKey {

  private final int key;

  public AnyRepositoryKey(int key) {
    this.key = key;
  }

  public int getKey() {
    return key;
  }

  @Override public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;

    AnyRepositoryKey that = (AnyRepositoryKey) o;

    return key == that.key;
  }

  @Override public int hashCode() {
    return key;
  }
}
