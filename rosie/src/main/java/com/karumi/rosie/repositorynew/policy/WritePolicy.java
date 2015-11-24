/*
 * Copyright (C) 2015 Karumi.
 */

package com.karumi.rosie.repositorynew.policy;

import java.util.EnumSet;

public enum WritePolicy {
  WRITE_ONCE,
  POPULATE_CACHE;

  public static final EnumSet<WritePolicy> WRITE_ONCE_AND_POPULATE =
      EnumSet.of(WRITE_ONCE, POPULATE_CACHE);
  public static final EnumSet<WritePolicy> WRITE_IN_ALL_AND_POPULATE = EnumSet.of(POPULATE_CACHE);
}
