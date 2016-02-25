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

package com.karumi.rosie.repository.datasource;

import java.util.Collection;

/**
 * Data source interface meant to be used only to retrieve data.
 *
 * @param <K> The class of the key used by this data source.
 * @param <V> The class of the values retrieved from this data source.
 */
public interface ReadableDataSource<K, V> {
  /**
   * Returns the only value that is uniquely identified by the provided key or null if there is
   * no value associated to it.
   *
   * @param key The key that uniquely identifies the requested value.
   * @return The value associated to the provided key or null if there is not any.
   * @throws Exception any exception on the data source.
   */
  V getByKey(K key) throws Exception;

  /**
   * Returns all the values available in the data source or null if the operation does not make
   * sense in the context of the data source.
   *
   * @return A collection of values or null if the operation is not implemented by this data source.
   * @throws Exception any exception on the data source.
   */
  Collection<V> getAll() throws Exception;
}
