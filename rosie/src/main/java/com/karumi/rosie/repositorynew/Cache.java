/*
 * Copyright (C) 2015 Karumi.
 */

package com.karumi.rosie.repositorynew;

public interface Cache<K, V extends Keyable<K>> extends Readable<K, V>, Writeable<K, V> {
}
