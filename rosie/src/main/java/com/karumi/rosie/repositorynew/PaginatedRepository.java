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
import java.util.Arrays;
import java.util.Collection;
import java.util.EnumSet;
import java.util.LinkedList;

public class PaginatedRepository<K, V extends Keyable<K>> extends Repository<K, V> {

  private Collection<PaginatedReadable<V>> paginatedReadables = new LinkedList<>();
  private Collection<PaginatedCache<K, V>> paginatedCaches = new LinkedList<>();

  public PaginatedCollection<V> get(int offset, int limit) throws Exception {
    return get(offset, limit, ReadPolicy.READ_ALL_AND_POPULATE);
  }

  public PaginatedCollection<V> get(int offset, int limit, EnumSet<ReadPolicy> policies)
      throws Exception {
    PaginatedCollection<V> values = new PaginatedCollection<>();

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
  protected final <R extends PaginatedReadable<V>> void addPaginatedReadables(R... readables) {
    this.paginatedReadables.addAll(Arrays.asList(readables));
  }

  @SafeVarargs
  protected final <R extends PaginatedCache<K, V>> void addPaginatedCaches(R... caches) {
    this.paginatedCaches.addAll(Arrays.asList(caches));
  }

  protected PaginatedCollection<V> getPaginatedValuesFromCaches(int offset, int limit) {
    PaginatedCollection<V> values = null;

    for (PaginatedCache<K, V> cache : paginatedCaches) {
      values = cache.get(offset, limit);
      if (!values.getItems().isEmpty()) {
        break;
      }
    }

    return values;
  }

  protected PaginatedCollection<V> getPaginatedValuesFromReadables(int offset, int limit) {
    PaginatedCollection<V> values = null;

    for (PaginatedReadable<V> readable : paginatedReadables) {
      values = readable.get(offset, limit);
      if (!values.getItems().isEmpty()) {
        break;
      }
    }

    return values;
  }

  protected void populatePaginatedCaches(int offset, int limit, PaginatedCollection<V> values) {
    for (PaginatedCache<K, V> cache : paginatedCaches) {
      cache.addOrUpdate(offset, limit, values.getItems(), values.hasMore());
    }
  }
}
