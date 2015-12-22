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
import com.karumi.rosie.repository.datasource.ReadableDataSource;

/**
 * Data source interface meant to be used only to retrieve pages of data.
 *
 * @param <V> The class of the values retrieved from this data source.
 */
public interface PaginatedReadableDataSource<K, V> extends ReadableDataSource<K, V> {
  /**
   * Returns a page of values bounded by the provided page.
   *
   * @param page page to be retrieved
   */
  PaginatedCollection<V> getPage(Page page) throws Exception;
}
