/*
 * Copyright (C) 2015 Karumi.
 */

package com.karumi.rosie.repository.policy;

import java.util.EnumSet;

public enum ReadPolicy {
  USE_CACHE,
  USE_READABLE,
  POPULATE_CACHE;

  public static final EnumSet<ReadPolicy> CACHE_ONLY = EnumSet.of(USE_CACHE);
  public static final EnumSet<ReadPolicy> READABLE_ONLY_AND_POPULATE =
      EnumSet.of(USE_READABLE, POPULATE_CACHE);
  public static final EnumSet<ReadPolicy> READ_ALL_AND_POPULATE = EnumSet.allOf(ReadPolicy.class);
}
