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

package com.karumi.rosie.repository.sample;

import com.karumi.rosie.repository.PaginatedCollection;
import com.karumi.rosie.repository.datasource.PaginatedCacheDataSource;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class SamplePaginatedCacheDataSource
    implements PaginatedCacheDataSource<SampleKey, SampleValue> {

  private static final List<SampleValue> VALUES = new ArrayList<>();

  static {
    VALUES.add(new SampleValue(new SampleKey(0), "Nombre0", "Apellido0", 20));
    VALUES.add(new SampleValue(new SampleKey(1), "Nombre1", "Apellido1", 21));
    VALUES.add(new SampleValue(new SampleKey(2), "Nombre2", "Apellido2", 22));
    VALUES.add(new SampleValue(new SampleKey(3), "Nombre3", "Apellido3", 23));
    VALUES.add(new SampleValue(new SampleKey(4), "Nombre4", "Apellido4", 24));
    VALUES.add(new SampleValue(new SampleKey(5), "Nombre5", "Apellido5", 25));
  }

  @Override public PaginatedCollection<SampleValue> getPage(int offset, int limit) {
    Collection<SampleValue> items = new ArrayList<>();

    for (int i = offset; i < offset + limit && i < VALUES.size(); i++) {
      items.add(VALUES.get(i));
    }

    PaginatedCollection<SampleValue> paginatedItems = new PaginatedCollection<>(items);
    paginatedItems.setOffset(offset);
    paginatedItems.setLimit(limit);
    paginatedItems.setHasMore(offset + limit >= VALUES.size());
    return paginatedItems;
  }

  @Override public PaginatedCollection<SampleValue> addOrUpdatePage(int offset, int limit,
      Collection<SampleValue> items, boolean hasMore) {
    int i = offset;
    for (SampleValue value : items) {
      if (i < VALUES.size() && i < offset + limit) {
        VALUES.add(i, value);
      }
      i++;
    }
    return getPage(offset, limit);
  }

  @Override public boolean isValid(SampleValue value) {
    return true;
  }

  @Override public void deleteAll() {
    VALUES.clear();
  }
}
