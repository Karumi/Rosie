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
import java.util.Collection;

public class EmptyPaginatedCacheDataSource<K, V extends Identifiable<K>>
    implements PaginatedCacheDataSource<K, V> {

  @Override public boolean isValid(V value) {
    return false;
  }

  @Override public PaginatedCollection<V> getPage(int offset, int limit) {
    return null;
  }

  @Override
  public PaginatedCollection<V> addOrUpdatePage(int offset, int limit, Collection<V> values,
      boolean hasMore) {
    return null;
  }

  @Override public void deleteAll() {

  }
}
