/*
 * Copyright (C) 2015 Karumi.
 */

package com.karumi.rosie.repositorynew.sample;

import com.karumi.rosie.repositorynew.Repository;

public class SampleRepository extends Repository<SampleKey, SampleValue> {

  public SampleRepository(SampleReadable sampleReadable, SampleWriteable sampleWriteable,
      SampleInMemoryCache sampleInMemoryCache) {
    addReadables(sampleReadable);
    addWriteables(sampleWriteable);
    addCaches(sampleInMemoryCache);
  }
}
