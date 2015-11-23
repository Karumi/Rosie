/*
 * Copyright (C) 2015 Karumi.
 */

package com.karumi.rosie.repositorynew.sample;

import com.karumi.rosie.repositorynew.Keyable;

public class SampleValue implements Keyable<SampleKey> {

  private final SampleKey key;
  private final String name;
  private final String surname;
  private final int age;

  public SampleValue(SampleKey key, String name, String surname, int age) {
    this.key = key;
    this.name = name;
    this.surname = surname;
    this.age = age;
  }

  @Override public SampleKey getKey() {
    return key;
  }

  public String getName() {
    return name;
  }

  public String getSurname() {
    return surname;
  }

  public int getAge() {
    return age;
  }

  @Override public String toString() {
    return "SampleValue{"
        + "key=" + key
        + ", name='" + name + '\''
        + ", surname='" + surname + '\''
        + ", age=" + age
        + '}';
  }
}
