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

import android.content.Context;
import com.karumi.rosie.time.TimeProvider;
import io.realm.Realm;
import io.realm.RealmObject;
import io.realm.RealmResults;
import java.util.Collection;
import java.util.LinkedList;

public class RealmCacheDataSource<V extends RealmIdentifiable,
    VR extends RealmObject>
    implements CacheDataSource<String, V> {

  private final Mapper<V, VR> mapperToDb;
  private final TimeProvider timeProvider;
  private final Class<VR> type;
  private final String primaryKeyName;
  private final RosieRealm rosieRealm;
  private final TtlStorage ttlStorage;
  protected final long ttlInMillis;

  public RealmCacheDataSource(Mapper<V, VR> mapperToDb, Class<VR> type, String primaryKeyName,
      TimeProvider timeProvider, long ttlInMillis, Context context) {
    this.mapperToDb = mapperToDb;
    this.timeProvider = timeProvider;
    this.ttlInMillis = ttlInMillis;
    this.type = type;
    this.primaryKeyName = primaryKeyName;
    rosieRealm = new RosieRealm(context);
    ttlStorage = new TtlStorage(rosieRealm, timeProvider);
  }

  @Override public V getByKey(String key) {
    V value = null;
    try {
      validatePrimaryKeyFieldName();
      VR valueRealm = rosieRealm.getRealm().where(type).equalTo(primaryKeyName, key).findFirst();
      if (valueRealm != null) {
        value = mapperToDb.reverseMap(valueRealm);
      }
    } finally {
      rosieRealm.closeRealm();
    }
    return value;
  }

  @Override public Collection<V> getAll() {
    Collection<V> values = new LinkedList<>();
    try {
      RealmResults<VR> result = rosieRealm.getRealm().allObjects(type);
      for (VR valueRealm : result) {
        V value = mapperToDb.reverseMap(valueRealm);
        values.add(value);
      }
    } finally {
      rosieRealm.closeRealm();
    }
    return values;
  }

  @Override public V addOrUpdate(V value) {
    try {
      Realm realm = rosieRealm.getRealm();
      rosieRealm.beginTransaction();
      VR valueRealm = mapperToDb.map(value);
      realm.copyToRealmOrUpdate(valueRealm);
      rosieRealm.commitTransaction();
    } catch (Exception e) {
      rosieRealm.cancelTransaction();
    } finally {
      rosieRealm.closeRealm();
    }
    ttlStorage.update(type.getName());
    return value;
  }

  @Override public Collection<V> addOrUpdateAll(Collection<V> values) {
    try {

      Realm realm = rosieRealm.getRealm();
      rosieRealm.beginTransaction();
      Collection<VR> valuesRealm = mapperToDb.map(values);
      realm.copyToRealmOrUpdate(valuesRealm);
      rosieRealm.commitTransaction();
    } catch (Exception e) {
      rosieRealm.cancelTransaction();
    } finally {
      rosieRealm.closeRealm();
    }
    ttlStorage.update(type.getName());
    return values;
  }

  @Override public void deleteByKey(String key) {
    validatePrimaryKeyFieldName();
    try {
      rosieRealm.beginTransaction();
      VR first = rosieRealm.getRealm().where(type).equalTo(primaryKeyName, key).findFirst();
      if (first != null) {
        first.removeFromRealm();
      }
      rosieRealm.commitTransaction();
    } catch (Exception e) {
      rosieRealm.cancelTransaction();
    } finally {
      rosieRealm.closeRealm();
    }
    ttlStorage.delete(type.getName());
  }

  @Override public void deleteAll() {
    try {
      Realm realm = rosieRealm.getRealm();
      rosieRealm.beginTransaction();
      realm.allObjects(type).clear();
      rosieRealm.commitTransaction();
    } catch (Exception e) {
      rosieRealm.cancelTransaction();
    } finally {
      rosieRealm.closeRealm();
    }
    ttlStorage.delete(type.getName());
  }

  @Override public boolean isValid(V value) {
    long lastItemsUpdate = ttlStorage.getLastUpdate(type.getName());
    return timeProvider.currentTimeMillis() - lastItemsUpdate < ttlInMillis;
  }

  private void validatePrimaryKeyFieldName() {
    if (primaryKeyName == null || primaryKeyName.isEmpty()) {
      throw new IllegalStateException(
          "You are trying to do an operation by Key, but you Realm model don't have an PrimaryKey ");
    }
  }
}
