/*
 * The MIT License (MIT) Copyright (c) 2014 karumi Permission is hereby granted, free of charge,
 * to any person obtaining a copy of this software and associated documentation files (the
 * "Software"), to deal in the Software without restriction, including without limitation the
 * rights to use, copy, modify, merge, publish, distribute, sublicense,
 * and/or sell copies of the Software, and to permit persons to whom the Software is furnished to
  * do so, subject to the following conditions: The above copyright notice and this permission
  * notice shall be included in all copies or substantial portions of the Software. THE SOFTWARE
  * IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED,
  * INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
  * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR
  * COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY,
  * WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
  * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package com.karumi.rosie.repository;

import com.karumi.rosie.repository.datasource.CacheDataSource;
import com.karumi.rosie.repository.datasource.Identifiable;
import com.karumi.rosie.repository.datasource.ReadableDataSource;
import com.karumi.rosie.repository.datasource.WriteableDataSource;
import com.karumi.rosie.repository.policy.ReadPolicy;
import com.karumi.rosie.repository.policy.WritePolicy;
import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedList;

/**
 * Repository pattern implementation. This class implements all the data handling logic based on
 * different data sources. Abstracts the data origin and works as a processor cache system where
 * different data sources are going to work as different cache levels. It coordinates three
 * different types of data sources, {@link ReadableDataSource}, {@link WriteableDataSource} and
 * {@link CacheDataSource}
 *
 * @param <K> Class representing the key used to identify items in this repository
 * @param <V> Class representing the contents of the items held by this repository
 */
public class RosieRepository<K, V extends Identifiable<K>>
    implements ReadableDataSource<K, V>, WriteableDataSource<K, V> {

  private final Collection<ReadableDataSource<K, V>> readableDataSources = new LinkedList<>();
  private final Collection<WriteableDataSource<K, V>> writeableDataSources = new LinkedList<>();
  private final Collection<CacheDataSource<K, V>> cacheDataSources = new LinkedList<>();

  @SafeVarargs protected final <R extends ReadableDataSource<K, V>> void addReadableDataSources(
      R... readableDataSources) {
    this.readableDataSources.addAll(Arrays.asList(readableDataSources));
  }

  @SafeVarargs protected final <R extends WriteableDataSource<K, V>> void addWriteableDataSources(
      R... writeableDataSources) {
    this.writeableDataSources.addAll(Arrays.asList(writeableDataSources));
  }

  @SafeVarargs protected final <R extends CacheDataSource<K, V>> void addCacheDataSources(
      R... cacheDataSources) {
    this.cacheDataSources.addAll(Arrays.asList(cacheDataSources));
  }

  @Override public V getByKey(K key) {
    return getByKey(key, ReadPolicy.READ_ALL);
  }

  public V getByKey(K key, ReadPolicy policy) {
    validateKey(key);

    V value = null;

    if (policy.useCache()) {
      value = getValueFromCaches(key);
    }

    if (value == null && policy.useReadable()) {
      value = getValueFromReadables(key);
    }

    if (value != null) {
      populateCaches(value);
    }

    return value;
  }

  @Override public Collection<V> getAll() {
    return getAll(ReadPolicy.READ_ALL);
  }

  public Collection<V> getAll(ReadPolicy policy) {
    Collection<V> values = null;

    if (policy.useCache()) {
      values = getValuesFromCaches();
    }

    if (values == null && policy.useReadable()) {
      values = getValuesFromReadables();
    }

    if (values != null) {
      populateCaches(values);
    }

    return values;
  }

  @Override public V addOrUpdate(V value) {
    return addOrUpdate(value, WritePolicy.WRITE_ALL);
  }

  public V addOrUpdate(V value, WritePolicy policy) {
    validateValue(value);

    V updatedValue = null;

    for (WriteableDataSource<K, V> writeableDataSource : writeableDataSources) {
      updatedValue = writeableDataSource.addOrUpdate(value);

      if (updatedValue != null && policy == WritePolicy.WRITE_ONCE) {
        break;
      }
    }

    if (updatedValue != null) {
      populateCaches(updatedValue);
    }

    return updatedValue;
  }

  @Override public Collection<V> addOrUpdateAll(Collection<V> values) {
    return addOrUpdateAll(values, WritePolicy.WRITE_ALL);
  }

  public Collection<V> addOrUpdateAll(Collection<V> values, WritePolicy policy) {
    validateValues(values);

    Collection<V> updatedValues = null;

    for (WriteableDataSource<K, V> writeableDataSource : writeableDataSources) {
      updatedValues = writeableDataSource.addOrUpdateAll(values);

      if (updatedValues != null && policy == WritePolicy.WRITE_ONCE) {
        break;
      }
    }

    if (updatedValues != null) {
      populateCaches(values);
    }

    return updatedValues;
  }

  @Override public void deleteByKey(K key) {
    for (WriteableDataSource<K, V> writeableDataSource : writeableDataSources) {
      writeableDataSource.deleteByKey(key);
    }

    for (CacheDataSource<K, V> cacheDataSource : cacheDataSources) {
      cacheDataSource.deleteByKey(key);
    }
  }

  public void deleteAll() {
    for (WriteableDataSource<K, V> writeableDataSource : writeableDataSources) {
      writeableDataSource.deleteAll();
    }

    for (CacheDataSource<K, V> cacheDataSource : cacheDataSources) {
      cacheDataSource.deleteAll();
    }
  }

  private V getValueFromCaches(K id) {
    V value = null;

    for (CacheDataSource<K, V> cacheDataSource : cacheDataSources) {
      value = cacheDataSource.getByKey(id);

      if (value != null) {
        if (cacheDataSource.isValid(value)) {
          break;
        } else {
          cacheDataSource.deleteByKey(id);
          value = null;
        }
      }
    }

    return value;
  }

  private Collection<V> getValuesFromCaches() {
    Collection<V> values = null;

    for (CacheDataSource<K, V> cacheDataSource : cacheDataSources) {
      values = cacheDataSource.getAll();

      if (values != null) {
        if (areValidValues(values, cacheDataSource)) {
          break;
        } else {
          cacheDataSource.deleteAll();
          values = null;
        }
      }
    }

    return values;
  }

  private V getValueFromReadables(K key) {
    V value = null;

    for (ReadableDataSource<K, V> readableDataSource : readableDataSources) {
      value = readableDataSource.getByKey(key);

      if (value != null) {
        break;
      }
    }

    return value;
  }

  protected Collection<V> getValuesFromReadables() {
    Collection<V> values = null;

    for (ReadableDataSource<K, V> readableDataSource : readableDataSources) {
      values = readableDataSource.getAll();

      if (values != null) {
        break;
      }
    }

    return values;
  }

  private void populateCaches(V value) {
    for (CacheDataSource<K, V> cacheDataSource : cacheDataSources) {
      cacheDataSource.addOrUpdate(value);
    }
  }

  protected void populateCaches(Collection<V> values) {
    for (CacheDataSource<K, V> cacheDataSource : cacheDataSources) {
      cacheDataSource.addOrUpdateAll(values);
    }
  }

  private boolean areValidValues(Collection<V> values, CacheDataSource<K, V> cacheDataSource) {
    boolean areValidValues = false;
    for (V value : values) {
      areValidValues |= cacheDataSource.isValid(value);
    }
    return areValidValues;
  }

  private void validateKey(K key) {
    if (key == null) {
      throw new IllegalArgumentException("The key used can't be null.");
    }
  }

  private void validateValue(V value) {
    if (value == null) {
      throw new IllegalArgumentException("The value used can't be null.");
    }
  }

  private void validateValues(Collection<V> values) {
    if (values == null) {
      throw new IllegalArgumentException("The values used can't be null.");
    }
  }
}
