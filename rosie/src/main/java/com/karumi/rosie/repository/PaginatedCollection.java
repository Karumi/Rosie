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

package com.karumi.rosie.repository;

import com.karumi.rosie.repository.datasource.paginated.Page;
import java.util.Collection;
import java.util.Collections;

/**
 * Collection of data with additional information related to the page, offset, limit and the next
 * items in the collection.
 */
public class PaginatedCollection<T> {

  private Collection<T> items;
  private boolean hasMore;
  private Page page;

  public PaginatedCollection() {
    this(Collections.<T>emptyList());
  }

  public PaginatedCollection(Collection<T> items) {
    this.items = items;
  }

  public PaginatedCollection setHasMore(boolean hasMore) {
    this.hasMore = hasMore;
    return this;
  }

  public PaginatedCollection setPage(Page page) {
    this.page = page;
    return this;
  }

  public Collection<T> getItems() {
    return items;
  }

  public boolean hasMore() {
    return hasMore;
  }

  public Page getPage() {
    return page;
  }
}
