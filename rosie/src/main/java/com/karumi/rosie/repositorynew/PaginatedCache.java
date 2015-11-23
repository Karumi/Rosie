/*
 * Copyright (C) 2015 Karumi.
 */

package com.karumi.rosie.repositorynew;

public interface PaginatedCache<K, V extends Keyable<K>>
    extends PaginatedReadable<V>, PaginatedWriteable<K, V> {
}
