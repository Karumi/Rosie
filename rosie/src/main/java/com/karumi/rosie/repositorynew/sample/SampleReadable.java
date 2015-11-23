/*
 * Copyright (C) 2015 Karumi.
 */

package com.karumi.rosie.repositorynew.sample;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class SampleReadable
    implements com.karumi.rosie.repositorynew.Readable<SampleKey, SampleValue> {

  private static final Map<Integer, SampleValue> VALUES = new HashMap<>();

  static {
    VALUES.put(0, new SampleValue(new SampleKey(0), "Sergio", "Gutiérrez", 27));
    VALUES.put(1, new SampleValue(new SampleKey(1), "Jorge", "Barroso", 28));
    VALUES.put(2, new SampleValue(new SampleKey(2), "Pedro", "Gómez", 29));
    VALUES.put(3, new SampleValue(new SampleKey(3), "Davide", "Mendolia", 30));
    VALUES.put(4, new SampleValue(new SampleKey(4), "Alberto", "Gragera", 31));
    VALUES.put(5, new SampleValue(new SampleKey(5), "Irene", "Herranz", 32));
  }

  @Override public SampleValue get(SampleKey key) {
    SampleValue sampleValue = VALUES.get(key.getIndex());
    System.out.println("Get from readable [" + key + "] -> " + sampleValue);
    return sampleValue;
  }

  @Override public Collection<SampleValue> getAll() {
    Collection<SampleValue> values = VALUES.values();
    System.out.println("Get all from readable -> " + values);
    return values;
  }
}
