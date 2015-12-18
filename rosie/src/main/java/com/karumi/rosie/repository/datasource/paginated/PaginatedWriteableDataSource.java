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

/**
 * Data source interface meant to be used only to persist page of data.
 *
 * @param <K> The class of the key used by this data source.
 * @param <V> The class of the values stored into this data source.
 */
public interface PaginatedWriteableDataSource<K, V extends Identifiable<K>> {
  /**
   * Adds or update a page of values into this data source.
   *
   * @param page Page to be stored
   * @param values Collection of values to be stored
   * @param hasMore True whether the persisted page has more elements
   */
  PaginatedCollection<V> addOrUpdatePage(Page page, Collection<V> values, boolean hasMore)
      throws Exception;

  /**
   * Deletes all the pages stored in this data source.
   */
  void deleteAll() throws Exception;
}
