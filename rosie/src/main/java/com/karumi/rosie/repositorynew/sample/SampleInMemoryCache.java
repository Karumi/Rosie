/*
 * Copyright (C) 2015 Karumi.
 */

package com.karumi.rosie.repositorynew.sample;

import com.karumi.rosie.repositorynew.Cache;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class SampleInMemoryCache implements Cache<SampleKey, SampleValue> {

  private final Map<SampleKey, SampleValue> cache = new HashMap<>();

  @Override public SampleValue get(SampleKey key) {
    SampleValue sampleValue = cache.get(key);
    System.out.println("Get from cache [" + key + "] -> " + sampleValue);
    return sampleValue;
  }

  @Override public Collection<SampleValue> getAll() {
    Collection<SampleValue> values = cache.values();
    System.out.println("Get all from cache -> " + values);
    return values;
  }

  @Override public SampleValue addOrUpdate(SampleValue value) {
    return cache.put(value.getKey(), value);
  }

  @Override public Collection<SampleValue> addOrUpdateAll(Collection<SampleValue> values) {
    for (SampleValue value : values) {
      addOrUpdate(value);
    }
    return values;
  }

  @Override public void delete(SampleKey key) {
    cache.remove(key);
  }

  @Override public void deleteAll() {
    cache.clear();
  }
}
