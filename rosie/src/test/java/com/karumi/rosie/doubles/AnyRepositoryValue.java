/*
 * Copyright (C) 2015 Karumi.
 */

package com.karumi.rosie.doubles;

import com.karumi.rosie.repository.datasource.Identifiable;

public class AnyRepositoryValue implements Identifiable<AnyRepositoryKey> {

  private final AnyRepositoryKey key;

  public AnyRepositoryValue(AnyRepositoryKey key) {
    this.key = key;
  }

  @Override public AnyRepositoryKey getKey() {
    return key;
  }

  @Override public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;

    AnyRepositoryValue that = (AnyRepositoryValue) o;

    return !(key != null ? !key.equals(that.key) : that.key != null);
  }

  @Override public int hashCode() {
    return key != null ? key.hashCode() : 0;
  }
}
