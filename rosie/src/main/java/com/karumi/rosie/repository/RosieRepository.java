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
    return new RosieRepository<R>(dataSources);
  }

  public RosieRepository(DataSource<T>... dataSources) {
    this.dataSources = dataSources;
  }

  public Collection<T> getAll() throws Exception {
    return getAll(false);
  }

  public Collection<T> getAll(boolean forceLoad) throws Exception {
    Collection<T> allData = null;
    int firstDataSource = forceLoad ? dataSources.length - 1 : 0;
    for (int i = firstDataSource; i < dataSources.length; i++) {
      DataSource<T> dataSource = dataSources[i];
      boolean isTheLastDataSource = i == dataSources.length - 1;
      allData = dataSource.getAll();
      if (isTheLastDataSource) {
        populateDataSources(allData, i);
        break;
      } else if (isValidData(dataSource, allData)) {
        break;
      } else {
        dataSource.deleteAll();
      }
    }
    return allData;
  }

  private void populateDataSources(Collection<T> data, int dataSourceIndex) {
    if (data == null) {
      return;
    }
    for (int i = 0; i < dataSourceIndex; i++) {
      DataSource dataSource = dataSources[i];
      dataSource.addOrUpdate(data);
    }
  }

  private boolean isValidData(DataSource<T> dataSource, Collection<T> data) {
    boolean isValidData = false;
    if (data != null) {
      for (T item : data) {
        isValidData |= dataSource.isValidItem(item);
      }
    }
    return isValidData;
  }

  public Collection<T> get(Predicate<T> predicate) throws Exception {
    validatePredicate(predicate);
    Collection<T> filteredData = new LinkedList<>();
    for (T item : getAll()) {
      if (predicate.isValid(item)) {
        filteredData.add(item);
      }
    }
    return filteredData;
  }

  private void validatePredicate(Predicate<T> predicate) {
    if (predicate == null) {
      throw new IllegalArgumentException("The predicate used can't be null.");
    }
  }
}
