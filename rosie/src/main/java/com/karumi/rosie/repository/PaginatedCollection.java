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

/**
 * Collection of data with additional information related to the page, offset, limit and the next
 * items in the collection.
 */
public class PaginatedCollection<T> {

  private Collection<T> items;
  private boolean hasMore;
  private int offset;
  private int limit;

  public PaginatedCollection() {
    this(null);
  }

  public PaginatedCollection(Collection<T> items) {
    this.items = items;
  }

  public PaginatedCollection setHasMore(boolean hasMore) {
    this.hasMore = hasMore;
    return this;
  }

  public PaginatedCollection setOffset(int offset) {
    this.offset = offset;
    return this;
  }

  public PaginatedCollection setLimit(int limit) {
    this.limit = limit;
    return this;
  }

  public Collection<T> getItems() {
    return items;
  }

  public boolean hasMore() {
    return hasMore;
  }

  public int getOffset() {
    return offset;
  }

  public int getLimit() {
    return limit;
  }
}
