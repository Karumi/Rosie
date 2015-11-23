/*
 * Copyright (C) 2015 Karumi.
 */

package com.karumi.rosie.repositorynew.sample;

import com.karumi.rosie.repositorynew.Writeable;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class SampleWriteable implements Writeable<SampleKey, SampleValue> {

  private final Map<SampleKey, SampleValue> writes = new HashMap<>();

  @Override public SampleValue addOrUpdate(SampleValue value) {
    return writes.put(value.getKey(), value);
  }

  @Override public Collection<SampleValue> addOrUpdateAll(Collection<SampleValue> values) {
    for (SampleValue value : values) {
      addOrUpdate(value);
    }
    return values;
  }

  @Override public void delete(SampleKey key) {
    writes.remove(key);
  }

  @Override public void deleteAll() {
    writes.clear();
  }
}
