/*
 *  Copyright (C) 2015 Karumi.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package com.karumi.rosie.repository.datasource;

import com.karumi.rosie.time.TimeProvider;
import io.realm.RealmQuery;

class TtlStorage {

  private final RosieRealm realm;
  private final TimeProvider timeProvider;

  TtlStorage(RosieRealm realm, TimeProvider timeProvider) {
    this.realm = realm;
    this.timeProvider = timeProvider;
  }

  /**
   * Returns the last update for the specified table
   */
  long getLastUpdate(String tableName) {
    long lastUpdate = 0;
    try {
      RealmQuery<Ttl> query = realm.getRealm().where(Ttl.class);
      query.equalTo(Ttl.CLASS_NAME, tableName);
      Ttl getTtl = query.findFirst();
      if (getTtl != null) {
        lastUpdate = getTtl.getLastUpdate();
      }
    } finally {
      realm.closeRealm();
    }
    return lastUpdate;
  }

  /**
   * Updates the stored TTL for the provided table This method must not be called from an open
   * transaction
   */
  void update(String tableName) {
    try {
      realm.beginTransaction();
      long now = timeProvider.currentTimeMillis();
      Ttl ttl = new Ttl();
      ttl.setClassName(tableName);
      ttl.setLastUpdate(now);
      realm.getRealm().copyToRealmOrUpdate(ttl);
      realm.commitTransaction();
    } catch (Exception e) {
      realm.cancelTransaction();
    } finally {
      realm.closeRealm();
    }
  }

  /**
   * Deletes an already stored TTL This method must not be called from an open transaction
   */
  void delete(String tableName) {
    try {
      realm.beginTransaction();
      realm.getRealm().where(Ttl.class).equalTo(Ttl.CLASS_NAME, tableName).findAll().clear();
      realm.commitTransaction();
    } catch (Exception e) {
      realm.cancelTransaction();
    } finally {
      realm.closeRealm();
    }
  }
}
