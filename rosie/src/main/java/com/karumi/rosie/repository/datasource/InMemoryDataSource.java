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

package com.karumi.rosie.repository.datasource;

import com.karumi.rosie.repository.Cacheable;
import com.karumi.rosie.time.TimeProvider;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

/**
 * In memory implementation of a generic DataSource implementing a basic TTL policy.
 */
public class InMemoryDataSource<T extends Cacheable> implements DataSource<T> {

  private final TimeProvider timeProvider;
  private final long ttlInMillis;

  protected final List<T> items;
  private long lastItemsUpdate;

  public InMemoryDataSource(TimeProvider timeProvider, long ttlInMillis) {
    this.items = new ArrayList<>();
    this.timeProvider = timeProvider;
    this.ttlInMillis = ttlInMillis;
  }

  @Override public synchronized T getById(String id) throws Exception {
    T result = null;
    for (T item : items) {
      if (item.getId().equals(id)) {
        result = item;
        break;
      }
    }
    return result;
  }

  @Override public synchronized Collection<T> getAll() throws Exception {
    return new ArrayList<>(items);
  }

  @Override public synchronized T addOrUpdate(T item) throws Exception {
    if (contains(item)) {
      updateItem(item);
    } else {
      items.add(item);
    }
    updateLastItemsUpdateTime();
    return item;
  }

  private boolean contains(T item) {
    boolean isItemInCache = false;
    for (T i : items) {
      if (i.getId().equals(item.getId())) {
        isItemInCache = true;
        break;
      }
    }
    return isItemInCache;
  }

  @Override public synchronized Collection<T> addOrUpdate(Collection<T> items) throws Exception {
    for (T item : items) {
      addOrUpdate(item);
    }
    updateLastItemsUpdateTime();
    return items;
  }

  @Override public synchronized void deleteById(String id) throws Exception {
    Iterator<T> iterator = items.iterator();
    while (iterator.hasNext()) {
      T next = iterator.next();
      if (next.getId().equals(id)) {
        iterator.remove();
      }
    }
  }

  @Override public synchronized void deleteAll() throws Exception {
    items.clear();
    lastItemsUpdate = 0;
  }

  @Override public boolean isValid(T item) throws Exception {
    if (item == null) {
      return false;
    }
    long now = timeProvider.currentTimeMillis();
    return now - lastItemsUpdate < ttlInMillis;
  }

  protected void updateLastItemsUpdateTime() {
    lastItemsUpdate = timeProvider.currentTimeMillis();
  }

  private void updateItem(T item) {
    String itemId = item.getId();
    for (int i = 0; i < items.size(); i++) {
      if (items.get(i).getId().equals(itemId)) {
        items.set(i, item);
      }
    }
  }
}
