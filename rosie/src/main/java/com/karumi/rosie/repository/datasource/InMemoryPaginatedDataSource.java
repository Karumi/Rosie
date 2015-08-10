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

package com.karumi.rosie.repository.datasource;

import com.karumi.rosie.repository.Cacheable;
import com.karumi.rosie.repository.PaginatedCollection;
import com.karumi.rosie.time.TimeProvider;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

public class InMemoryPaginatedDataSource<T extends Cacheable> extends InMemoryDataSource
    implements PaginatedDataSource {

  private boolean hasMore;

  public InMemoryPaginatedDataSource(TimeProvider timeProvider, long ttlInMillis) {
    super(timeProvider, ttlInMillis);
  }

  @Override public PaginatedCollection get(int offset, int limit) throws Exception {
    validateOffsetAndLimit(offset, limit);

    List<T> result = new LinkedList<>();
    for (int i = offset; i < limit && offset < items.size(); i++) {
      T item = (T) items.get(i);
      result.add(item);
    }
    PaginatedCollection<T> paginatedCollection = new PaginatedCollection<>(result);
    paginatedCollection.setOffset(offset);
    paginatedCollection.setLimit(limit);
    boolean hasMore = offset + limit < items.size() || this.hasMore;
    paginatedCollection.setHasMore(hasMore);
    return paginatedCollection;
  }

  @Override
  public PaginatedCollection addOrUpdate(int offset, int limit, Collection items, boolean hasMore) {
    validateOffsetAndLimit(offset, limit);
    this.items.addAll(items);
    this.hasMore = hasMore;
    PaginatedCollection<T> paginatedCollection = new PaginatedCollection<>(items);
    paginatedCollection.setOffset(offset);
    paginatedCollection.setLimit(limit);
    paginatedCollection.setHasMore(hasMore);
    updateLastItemsUpdateTime();
    return paginatedCollection;
  }

  @Override public void deleteAll(int offset, int limit) {
    validateOffsetAndLimit(offset, limit);
    List<T> validItems = new LinkedList<>();
    for (int i = 0; i < offset; i++) {
      T item = (T) items.get(i);
      validItems.add(item);
    }
  }

  private void validateOffsetAndLimit(int offset, int limit) {
    if (offset < 0) {
      throw new IllegalArgumentException("The offset can't be negative.");
    }
    if (limit < 0) {
      throw new IllegalArgumentException("The limit can't be negative.");
    }
    if (offset < limit) {
      throw new IllegalArgumentException("The limit can't lower than the offset.");
    }
  }
}
