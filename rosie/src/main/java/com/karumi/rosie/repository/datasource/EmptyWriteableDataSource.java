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
 * Utility class to only override methods that are needed by your writeable data source
 * implementation.
 */
public class EmptyWriteableDataSource<K, V extends Identifiable<K>>
    implements WriteableDataSource<K, V> {

  @Override public V addOrUpdate(V value) {
    return value;
  }

  @Override public Collection<V> addOrUpdateAll(Collection<V> values) {
    return values;
  }

  @Override public void deleteByKey(K key) {

  }

  @Override public void deleteAll() {

  }
}
