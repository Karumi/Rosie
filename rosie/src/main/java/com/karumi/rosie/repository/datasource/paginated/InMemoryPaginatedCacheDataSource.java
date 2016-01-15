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

package com.karumi.rosie.repository.datasource.paginated;

import com.karumi.rosie.repository.PaginatedCollection;
import com.karumi.rosie.repository.datasource.Identifiable;
import com.karumi.rosie.repository.datasource.InMemoryCacheDataSource;
import com.karumi.rosie.time.TimeProvider;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

public class InMemoryPaginatedCacheDataSource<K, V extends Identifiable<K>>
    extends InMemoryCacheDataSource<K, V> implements PaginatedCacheDataSource<K, V> {

  private boolean hasMore;

  public InMemoryPaginatedCacheDataSource(TimeProvider timeProvider, long ttlInMillis) {
    super(timeProvider, ttlInMillis);
  }

  @Override public synchronized PaginatedCollection<V> getPage(Page page) {
    List<V> result = new LinkedList<>();

    int offset = page.getOffset();
    int limit = page.getLimit();

    List<V> values = new ArrayList<>(items.values());
    for (int i = offset; i < values.size() && i < offset + limit; i++) {
      V value = values.get(i);
      result.add(value);
    }
    PaginatedCollection<V> paginatedCollection = new PaginatedCollection<>(result);
    paginatedCollection.setPage(page);
    paginatedCollection.setHasMore(offset + limit < items.size() || this.hasMore);
    return paginatedCollection;
  }

  @Override
  public synchronized PaginatedCollection<V> addOrUpdatePage(Page page, Collection<V> values, boolean hasMore) {
    for (V value : values) {
      addOrUpdate(value);
    }
    this.hasMore = hasMore;
    PaginatedCollection<V> paginatedCollection = new PaginatedCollection<>(values);
    paginatedCollection.setPage(page);
    paginatedCollection.setHasMore(hasMore);
    lastItemsUpdate = timeProvider.currentTimeMillis();
    return paginatedCollection;
  }

  @Override public synchronized void deleteAll() {
    items.clear();
    hasMore = false;
    lastItemsUpdate = 0;
  }

  @Override public boolean isValid(V value) {
    return timeProvider.currentTimeMillis() - lastItemsUpdate < ttlInMillis;
  }
}
