/*
 * Copyright (C) 2015 Karumi.
 */

package com.karumi.rosie.repositorynew;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.EnumSet;
import java.util.LinkedList;

public class Repository<K, V extends Keyable<K>> implements Readable<K, V>, Writeable<K, V> {

  private Collection<Readable<K, V>> readables = new LinkedList<>();
  private Collection<Writeable<K, V>> writeables = new LinkedList<>();
  private Collection<Cache<K, V>> caches = new LinkedList<>();

  @Override public V get(K key) {
    return get(key, ReadPolicy.READ_ALL_AND_POPULATE);
  }

  public V get(K key, EnumSet<ReadPolicy> policies) {
    V value = null;

    if (policies.contains(ReadPolicy.USE_CACHE)) {
      value = getValueFromCaches(key);
    }

    if (value == null && policies.contains(ReadPolicy.USE_READABLE)) {
      value = getValueFromReadables(key);
    }

    if (value != null && policies.contains(ReadPolicy.POPULATE_CACHE)) {
      populateCaches(value);
    }

    return value;
  }

  @Override public Collection<V> getAll() {
    return getAll(ReadPolicy.READ_ALL_AND_POPULATE);
  }

  public Collection<V> getAll(EnumSet<ReadPolicy> policies) {
    Collection<V> values = Collections.emptyList();

    if (policies.contains(ReadPolicy.USE_CACHE)) {
      values = getValuesFromCaches();
    }

    if (values == null && policies.contains(ReadPolicy.USE_READABLE)) {
      values = getValuesFromReadables();
    }

    if (values != null && policies.contains(ReadPolicy.POPULATE_CACHE)) {
      populateCaches(values);
    }

    return values;
  }

  @Override public V addOrUpdate(V value) {
    return add(value, WritePolicy.WRITE_ONCE_AND_POPULATE);
  }

  public V add(V value, EnumSet<WritePolicy> policies) {
    for (Writeable<K, V> writeable : writeables) {
      writeable.addOrUpdate(value);

      if (policies.contains(WritePolicy.WRITE_ONCE)) {
        break;
      }
    }

    if (policies.contains(WritePolicy.POPULATE_CACHE)) {
      populateCaches(value);
    }

    return value;
  }

  @Override public Collection<V> addOrUpdateAll(Collection<V> values) {
    return addOrUpdateAll(values, WritePolicy.WRITE_IN_ALL_AND_POPULATE);
  }

  public Collection<V> addOrUpdateAll(Collection<V> values, EnumSet<WritePolicy> policies) {
    for (Writeable<K, V> writeable : writeables) {
      writeable.addOrUpdateAll(values);

      if (policies.contains(WritePolicy.WRITE_ONCE)) {
        break;
      }
    }

    if (policies.contains(WritePolicy.POPULATE_CACHE)) {
      populateCaches(values);
    }

    return values;
  }

  @Override public void delete(K key) {
    for (Writeable<K, V> writeable : writeables) {
      writeable.delete(key);
    }

    for (Cache<K, V> cache : caches) {
      cache.delete(key);
    }
  }

  public void deleteAll() {
    for (Writeable<K, V> writeable : writeables) {
      writeable.deleteAll();
    }

    for (Cache<K, V> cache : caches) {
      cache.deleteAll();
    }
  }

  @SafeVarargs protected final <R extends Readable<K, V>> void addReadables(R... readables) {
    this.readables.addAll(Arrays.asList(readables));
  }

  @SafeVarargs protected final <R extends Writeable<K, V>> void addWriteables(R... writeables) {
    this.writeables.addAll(Arrays.asList(writeables));
  }

  @SafeVarargs protected final <R extends Cache<K, V>> void addCaches(R... caches) {
    this.caches.addAll(Arrays.asList(caches));
  }

  private V getValueFromCaches(K key) {
    V value = null;

    for (Cache<K, V> cache : caches) {
      value = cache.get(key);
      if (value != null) {
        break;
      }
    }

    return value;
  }

  private Collection<V> getValuesFromCaches() {
    Collection<V> values = null;

    for (Cache<K, V> cache : caches) {
      values = cache.getAll();
      if (!values.isEmpty()) {
        break;
      }
    }

    return values;
  }

  private V getValueFromReadables(K key) {
    V value = null;

    for (Readable<K, V> readable : readables) {
      value = readable.get(key);
      if (value != null) {
        break;
      }
    }

    return value;
  }

  protected Collection<V> getValuesFromReadables() {
    Collection<V> values = null;

    for (Readable<K, V> readable : readables) {
      values = readable.getAll();
      if (values != null) {
        break;
      }
    }

    return values;
  }

  private void populateCaches(V value) {
    for (Cache<K, V> cache : caches) {
      cache.addOrUpdate(value);
    }
  }

  protected void populateCaches(Collection<V> values) {
    for (Cache<K, V> cache : caches) {
      cache.addOrUpdateAll(values);
    }
  }

  public enum ReadPolicy {
    USE_CACHE,
    USE_READABLE,
    POPULATE_CACHE;

    public static final EnumSet<ReadPolicy> CACHE_ONLY = EnumSet.of(USE_CACHE);
    public static final EnumSet<ReadPolicy> READABLE_AND_POPULATE =
        EnumSet.of(USE_READABLE, POPULATE_CACHE);
    public static final EnumSet<ReadPolicy> READ_ALL_AND_POPULATE = EnumSet.allOf(ReadPolicy.class);
  }

  public enum WritePolicy {
    WRITE_ONCE,
    POPULATE_CACHE;

    public static final EnumSet<WritePolicy> WRITE_ONCE_AND_POPULATE =
        EnumSet.of(WRITE_ONCE, POPULATE_CACHE);
    public static final EnumSet<WritePolicy> WRITE_IN_ALL_AND_POPULATE = EnumSet.of(POPULATE_CACHE);
  }
}
