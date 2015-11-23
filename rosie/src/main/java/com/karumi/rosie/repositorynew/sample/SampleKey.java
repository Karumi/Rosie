/*
 * Copyright (C) 2015 Karumi.
 */

package com.karumi.rosie.repositorynew.sample;

public class SampleKey {

  private final int index;

  public SampleKey(int index) {
    this.index = index;
  }

  public int getIndex() {
    return index;
  }

  @Override public String toString() {
    return "SampleKey{" +
        "index=" + index +
        '}';
  }

  @Override public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;

    SampleKey sampleKey = (SampleKey) o;

    return index == sampleKey.index;
  }

  @Override public int hashCode() {
    return index;
  }
}
