/*
 * Copyright (C) 2015 Karumi.
 */

package com.karumi.rosie.repository.datasource.paginated;

public final class Page {

  private final int offset;
  private final int limit;

  private Page(int offset, int limit) {
    this.offset = offset;
    this.limit = limit;
  }

  public static Page withOffsetAndLimit(int offset, int limit) {
    return new Page(offset, limit);
  }

  public int getOffset() {
    return offset;
  }

  public int getLimit() {
    return limit;
  }
}
