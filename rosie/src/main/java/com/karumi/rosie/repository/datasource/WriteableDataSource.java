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
 * Data source interface meant to be used only to persist data.
 *
 * @param <K> The class of the key used by this data source.
 * @param <V> The class of the values stored into this data source.
 */
public interface WriteableDataSource<K, V extends Identifiable<K>> {
  /**
   * Adds or update the provided value into this data source.
   *
   * @param value The value to be persisted.
   * @return The value after its addition or update.
   * @throws Exception any exception on the data source.
   */
  V addOrUpdate(V value) throws Exception;

  /**
   * Add or updates all the provided values into this data source.
   *
   * @param values A collection of values to be added or persisted.
   * @return The values that has been persisted.
   * @throws Exception any exception on the data source.
   */
  Collection<V> addOrUpdateAll(Collection<V> values) throws Exception;

  /**
   * Deletes a value given its associated key.
   *
   * @param key The key that uniquely identifies the value to be deleted.
   * @throws Exception any exception on the data source.
   */
  void deleteByKey(K key) throws Exception;

  /**
   * Delete all the values stored in this data source.
   * @throws Exception any exception on the data source.
   */
  void deleteAll() throws Exception;
}
