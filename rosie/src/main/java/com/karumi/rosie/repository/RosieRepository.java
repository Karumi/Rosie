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

package com.karumi.rosie.repository;

import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedList;

/**
 * Repository pattern implementation. This class implements all the data handling logic based on
 * different data sources. Abstracts the data origin and works as a processor cache system where
 * different data sources are going to work as different cache levels.
 */
public class RosieRepository<T extends Cacheable> {

  private final DataSource<T>[] dataSources;

  public static <R extends Cacheable> RosieRepository<R> with(DataSource<R>... dataSources) {
    if (dataSources == null || dataSources.length == 0) {
      throw new IllegalArgumentException("The Repository can't be created without data sources.");
    }

    return new RosieRepository<R>(dataSources);
  }

  private RosieRepository(DataSource<T>... dataSources) {
    this.dataSources = dataSources;
  }

  public T get(String id) throws Exception {
    return get(id, false);
  }

  public T get(String id, boolean forceLoad) throws Exception {
    validateId(id);
    T item = null;
    int firstDataSource = forceLoad ? dataSources.length - 1 : 0;
    for (int i = firstDataSource; i < dataSources.length; i++) {
      DataSource<T> dataSource = dataSources[i];
      boolean isTheLastDataSource = i == dataSources.length - 1;
      item = dataSource.getById(id);
      if (isTheLastDataSource) {
        populateDataSources(item, i);
        break;
      } else if (areValidItems(dataSource, Arrays.asList(item))) {
        break;
      } else {
        dataSource.deleteById(id);
      }
    }
    return item;
  }

  public Collection<T> getAll() throws Exception {
    return getAll(false);
  }

  public Collection<T> getAll(boolean forceLoad) throws Exception {
    Collection<T> items = null;
    int firstDataSource = forceLoad ? dataSources.length - 1 : 0;
    for (int i = firstDataSource; i < dataSources.length; i++) {
      DataSource<T> dataSource = dataSources[i];
      boolean isTheLastDataSource = i == dataSources.length - 1;
      items = dataSource.getAll();
      if (isTheLastDataSource) {
        populateDataSources(items, i);
        break;
      } else if (areValidItems(dataSource, items)) {
        break;
      } else {
        dataSource.deleteAll();
      }
    }
    return items;
  }

  public Collection<T> get(Predicate<T> predicate) throws Exception {
    return get(predicate, false);
  }

  public Collection<T> get(Predicate<T> predicate, boolean forceLoad) throws Exception {
    validatePredicate(predicate);

    Collection<T> filteredItems = new LinkedList<>();
    for (T item : getAll(forceLoad)) {
      if (predicate.isValid(item)) {
        filteredItems.add(item);
      }
    }
    return filteredItems;
  }

  public T addOrUpdate(T item) throws Exception {
    validateItem(item);

    int lastDataSourceIndex = dataSources.length - 1;
    DataSource<T> lastDataSource = dataSources[(lastDataSourceIndex)];
    item = lastDataSource.addOrUpdate(item);
    boolean itemAddedSuccessfully = item != null;
    if (itemAddedSuccessfully) {
      populateDataSources(item, lastDataSourceIndex);
    }
    return item;
  }

  public Collection<T> addOrUpdate(Collection<T> items) throws Exception {
    validateItems(items);

    if (items.isEmpty()) {
      return items;
    }

    int lastDataSourceIndex = dataSources.length - 1;
    DataSource<T> lastDataSource = dataSources[(lastDataSourceIndex)];
    items = lastDataSource.addOrUpdate(items);
    boolean itemAddedSuccessfully = items != null && !items.isEmpty();
    if (itemAddedSuccessfully) {
      populateDataSources(items, lastDataSourceIndex);
    }
    return items;
  }

  public void deleteAll() throws Exception {
    for (int i = dataSources.length - 1; i >= 0; i--) {
      DataSource dataSource = dataSources[i];
      dataSource.deleteAll();
    }
  }

  public void deleteById(String id) throws Exception {
    validateId(id);

    for (int i = dataSources.length - 1; i >= 0; i--) {
      DataSource dataSource = dataSources[i];
      dataSource.deleteById(id);
    }
  }

  private void populateDataSources(T item, int dataSourceIndex) throws Exception {
    if (item == null) {
      return;
    }

    for (int i = 0; i < dataSourceIndex; i++) {
      DataSource dataSource = dataSources[i];
      dataSource.addOrUpdate(item);
    }
  }

  private void populateDataSources(Collection<T> items, int dataSourceIndex) throws Exception {
    if (items == null) {
      return;
    }

    for (int i = 0; i < dataSourceIndex; i++) {
      DataSource dataSource = dataSources[i];
      dataSource.addOrUpdate(items);
    }
  }

  private boolean areValidItems(DataSource<T> dataSource, Collection<T> items) throws Exception {
    boolean areValidItems = false;
    if (items != null) {
      for (T item : items) {
        areValidItems |= dataSource.isValid(item);
      }
    }
    return areValidItems;
  }

  private void validatePredicate(Predicate<T> predicate) {
    if (predicate == null) {
      throw new IllegalArgumentException("The predicate used can't be null.");
    }
  }

  private void validateId(String id) {
    if (id == null) {
      throw new IllegalArgumentException("The id used can't be null.");
    }
  }

  private void validateItem(T item) {
    if (item == null) {
      throw new IllegalArgumentException("The item can't be null.");
    }
  }

  private void validateItems(Collection<T> items) {
    if (items == null) {
      throw new IllegalArgumentException("The items can't be null.");
    }
  }
}
