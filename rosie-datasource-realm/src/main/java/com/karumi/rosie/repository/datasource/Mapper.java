package com.karumi.rosie.repository.datasource;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public abstract class Mapper<V1, V2> {
  public abstract V2 map(V1 value);

  public abstract V1 reverseMap(V2 value2);

  public Collection<V2> map(Collection<V1> values) {
    List<V2> valuesV2 = new ArrayList<>();
    for (V1 value : values) {
      V2 valueV2 = map(value);
      valuesV2.add(valueV2);
    }
    return valuesV2;
  }

  public Collection<V1> reverseMap(Collection<V2> valuesV2) {
    List<V1> values = new ArrayList<>();
    for (V2 valueV2 : valuesV2) {
      V1 value = reverseMap(valueV2);
      values.add(value);
    }
    return values;
  }
}
