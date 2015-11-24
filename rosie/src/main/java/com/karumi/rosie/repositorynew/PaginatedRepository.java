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

package com.karumi.rosie.repositorynew;

import com.karumi.rosie.repository.PaginatedCollection;
import com.karumi.rosie.repository.RosieRepository;
import com.karumi.rosie.repositorynew.datasource.Identifiable;
import com.karumi.rosie.repositorynew.datasource.PaginatedCacheDataSource;
import com.karumi.rosie.repositorynew.datasource.PaginatedReadableDataSource;
import com.karumi.rosie.repositorynew.policy.ReadPolicy;
import java.util.Arrays;
import java.util.Collection;
import java.util.EnumSet;
import java.util.LinkedList;

/**
 * Paginated version of {@link RosieRepository}. It adds methods for retrieving paginated data
 */
public class PaginatedRepository<K, V extends Identifiable<K>> extends Repository<K, V> {

  private final Collection<PaginatedReadableDataSource<V>> paginatedReadableDataSources =
      new LinkedList<>();
  private final Collection<PaginatedCacheDataSource<K, V>> paginatedCacheDataSources =
      new LinkedList<>();

  public PaginatedCollection<V> getPage(int offset, int limit) throws Exception {
    return getPage(offset, limit, ReadPolicy.READ_ALL_AND_POPULATE);
  }

  public PaginatedCollection<V> getPage(int offset, int limit, EnumSet<ReadPolicy> policies)
      throws Exception {
    PaginatedCollection<V> values = null;

    if (policies.contains(ReadPolicy.USE_CACHE)) {
      values = getPaginatedValuesFromCaches(offset, limit);
    }

    if (values == null && policies.contains(ReadPolicy.USE_READABLE)) {
      values = getPaginatedValuesFromReadables(offset, limit);
    }

    if (values != null && policies.contains(ReadPolicy.POPULATE_CACHE)) {
      populatePaginatedCaches(offset, limit, values);
    }

    return values;
  }

  @SafeVarargs
  protected final <R extends PaginatedReadableDataSource<V>> void addPaginatedReadables(
      R... readables) {
    this.paginatedReadableDataSources.addAll(Arrays.asList(readables));
  }

  @SafeVarargs
  protected final <R extends PaginatedCacheDataSource<K, V>> void addPaginatedCaches(R... caches) {
    this.paginatedCacheDataSources.addAll(Arrays.asList(caches));
  }

  protected PaginatedCollection<V> getPaginatedValuesFromCaches(int offset, int limit) {
    PaginatedCollection<V> values = null;

    for (PaginatedCacheDataSource<K, V> cacheDataSource : paginatedCacheDataSources) {
      values = cacheDataSource.getPage(offset, limit);

      if (values == null) {
        continue;
      }

      if (areValidValues(values, cacheDataSource)) {
        break;
      }

      cacheDataSource.deleteAll();
      values = null;
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
