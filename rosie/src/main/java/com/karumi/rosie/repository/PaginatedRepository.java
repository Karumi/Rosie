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

import com.karumi.rosie.repository.datasource.PaginatedDataSource;
import java.util.Collection;

/**
 * Paginated version of RosieRepository.
 */
public class PaginatedRepository<T extends Cacheable> extends RosieRepository<T> {

  public PaginatedRepository(PaginatedDataSource<T>... dataSources) {
    super(dataSources);
  }

  public PaginatedCollection<T> get(int offset, int limit) throws Exception {
    return get(offset, limit, false);
  }

  public PaginatedCollection<T> get(int offset, int limit, boolean forceLoad) throws Exception {
    validatePageParams(offset, limit);

    PaginatedCollection<T> page = null;
    int numberOfDataSources = getNumberOfDataSources();
    int firstDataSource = forceLoad ? numberOfDataSources - 1 : 0;
    for (int i = firstDataSource; i < numberOfDataSources; i++) {
      PaginatedDataSource<T> dataSource = getPaginatedDataSource(i);
      page = dataSource.get(offset, limit);
      if (areValidItems(dataSource, page.getItems())) {
        populateDataSources(page, i);
        onItemsLoadedFromTheLastDataSource(page);
        break;
      } else {
        dataSource.deleteAll();
      }
    }
    return page;
  }

  protected void onItemsLoadedFromTheLastDataSource(PaginatedCollection<T> page) {

  }

  private void populateDataSources(PaginatedCollection page, int dataSourceIndex) {
    Collection items = page.getItems();
    if (items == null) {
      return;
    }

    for (int i = 0; i < dataSourceIndex; i++) {
      PaginatedDataSource dataSource = getPaginatedDataSource(i);
      int offset = page.getOffset();
      int limit = page.getLimit();
      boolean hasMore = page.hasMore();
      dataSource.addOrUpdate(offset, limit, items, hasMore);
    }
  }

  private PaginatedDataSource<T> getPaginatedDataSource(int index) {
    return (PaginatedDataSource<T>) getDataSource(index);
  }

  private void validatePageParams(int offset, int limit) {
    if (offset < 0) {
      throw new IllegalArgumentException("The offset used can't be negative.");
    }
    if (limit < 0) {
      throw new IllegalArgumentException("The limit used can't be negative.");
    }
  }
}
