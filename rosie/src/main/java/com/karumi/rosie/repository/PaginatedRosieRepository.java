/*
 * Copyright (C) 2015 Karumi.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.karumi.rosie.repository;

import com.karumi.rosie.repository.datasource.Identifiable;
import com.karumi.rosie.repository.datasource.paginated.PaginatedCacheDataSource;
import com.karumi.rosie.repository.datasource.paginated.PaginatedReadableDataSource;
import com.karumi.rosie.repository.policy.ReadPolicy;
import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedList;

/**
 * Paginated version of {@link RosieRepository}. It adds methods for retrieving paginated data
 */
public class PaginatedRosieRepository<K, V extends Identifiable<K>> extends RosieRepository<K, V> {

  private final Collection<PaginatedReadableDataSource<V>> paginatedReadableDataSources =
      new LinkedList<>();
  private final Collection<PaginatedCacheDataSource<K, V>> paginatedCacheDataSources =
      new LinkedList<>();

  @SafeVarargs
  protected final <R extends PaginatedReadableDataSource<V>> void addPaginatedReadableDataSources(
      R... readables) {
    this.paginatedReadableDataSources.addAll(Arrays.asList(readables));
  }

  @SafeVarargs
  protected final <R extends PaginatedCacheDataSource<K, V>> void addPaginatedCacheDataSources(
      R... caches) {
    this.paginatedCacheDataSources.addAll(Arrays.asList(caches));
  }

  /**
   * Returns a page of values bounded by the offset and limit values.
   * @param offset Index of the first item to be retrieved
   * @param limit Number of elements that will be retrieved
   * @return a PaginatedCollection instance with the retrieved content
   */
  public PaginatedCollection<V> getPage(int offset, int limit) {
    return getPage(offset, limit, ReadPolicy.READ_ALL);
  }

  /**
   * Returns a page of values bounded by the offset and limit values.
   * @param offset Index of the first item to be retrieved
   * @param limit Number of elements that will be retrieved
   * @param policy Specifies how the value is going to be retrieved.
   * @return a PaginatedCollection instance with the retrieved content
   */
  public PaginatedCollection<V> getPage(int offset, int limit, ReadPolicy policy) {
    PaginatedCollection<V> values = null;

    if (policy.useCache()) {
      values = getPaginatedValuesFromCaches(offset, limit);
    }

    if (values == null && policy.useReadable()) {
      values = getPaginatedValuesFromReadables(offset, limit);
    }

    if (values != null) {
      populatePaginatedCaches(offset, limit, values);
    }

    return values;
  }

  protected PaginatedCollection<V> getPaginatedValuesFromCaches(int offset, int limit) {
    PaginatedCollection<V> values = null;

    for (PaginatedCacheDataSource<K, V> cacheDataSource : paginatedCacheDataSources) {
      values = cacheDataSource.getPage(offset, limit);

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

  protected PaginatedCollection<V> getPaginatedValuesFromReadables(int offset, int limit) {
    PaginatedCollection<V> values = null;

    for (PaginatedReadableDataSource<V> readable : paginatedReadableDataSources) {
      values = readable.getPage(offset, limit);

      if (values != null) {
        break;
      }
    }

    return values;
  }

  protected void populatePaginatedCaches(int offset, int limit, PaginatedCollection<V> values) {
    for (PaginatedCacheDataSource<K, V> cacheDataSource : paginatedCacheDataSources) {
      cacheDataSource.addOrUpdatePage(offset, limit, values.getItems(), values.hasMore());
    }
  }

  private boolean areValidValues(PaginatedCollection<V> values,
      PaginatedCacheDataSource<K, V> cacheDataSource) {
    boolean areValidValues = false;
    for (V value : values.getItems()) {
      areValidValues |= cacheDataSource.isValid(value);
    }
    return areValidValues;
  }
}
