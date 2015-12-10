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

/**
 * Mixin data source used to act as a cache. It contains methods to both read and add/update/delete
 * values.
 *
 * @param <K> The class of the key used by this data source.
 * @param <V> The class of the values retrieved from this data source.
 */
public interface CacheDataSource<K, V extends Identifiable<K>>
    extends ReadableDataSource<K, V>, WriteableDataSource<K, V> {
  /**
   * Returns true whether the value stored in this cache is still valid.
   *
   * @param value The value to be checked.
   */
  boolean isValid(V value);
}
