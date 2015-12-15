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
import io.realm.RealmConfiguration;
import io.realm.RealmObject;
import io.realm.RealmResults;
import java.util.Collection;
import java.util.LinkedList;

public class RealmCacheDataSource<V extends RealmIdentifiable,
    VR extends RealmObject>
    implements CacheDataSource<String, V> {

  private final Mapper<V, VR> mapperToDb;
  private final TimeProvider timeProvider;
  private final Context context;
  private final Class<VR> type;
  private final String primaryKeyName;
  private Realm realm;
  private long lastItemsUpdate = 0;
  protected final long ttlInMillis;

  public RealmCacheDataSource(Mapper<V, VR> mapperToDb, Class<VR> type, String primaryKeyName,
      TimeProvider timeProvider,
      long ttlInMillis, Context context) {
    this.mapperToDb = mapperToDb;
    this.timeProvider = timeProvider;
    this.ttlInMillis = ttlInMillis;
    this.type = type;
    this.primaryKeyName = primaryKeyName;
    this.context = context;
  }

  @Override public V getByKey(String key) {
    validatePrimaryKeyFieldName();
    V value = null;
    VR valueRealm = getRealm().where(type).equalTo(primaryKeyName, key).findFirst();
    if (valueRealm != null) {
      value = mapperToDb.reverseMap(valueRealm);
    }
    closeRealm();
    return value;
  }

  @Override public Collection<V> getAll() {
    Collection<V> values = new LinkedList<>();
    RealmResults<VR> result = getRealm().allObjects(type);
    for (VR valueRealm : result) {
      V value = mapperToDb.reverseMap(valueRealm);
      values.add(value);
    }
    closeRealm();
    return values;
  }

  @Override public V addOrUpdate(V value) {
    Realm realm = getRealm();
    beginTransaction();
    VR valueRealm = mapperToDb.map(value);
    realm.copyToRealmOrUpdate(valueRealm);
    commitTransaction();
    closeRealm();
    lastItemsUpdate = timeProvider.currentTimeMillis();
    return value;
  }

  @Override public Collection<V> addOrUpdateAll(Collection<V> values) {
    Realm realm = getRealm();
    beginTransaction();
    Collection<VR> valuesRealm = mapperToDb.map(values);
    realm.copyToRealmOrUpdate(valuesRealm);
    commitTransaction();
    closeRealm();
    lastItemsUpdate = timeProvider.currentTimeMillis();
    return values;
  }

  @Override public void deleteByKey(String key) {
    validatePrimaryKeyFieldName();
    beginTransaction();
    VR first = getRealm().where(type).equalTo(primaryKeyName, key).findFirst();
    if (first != null) {
      first.removeFromRealm();
    }
    commitTransaction();
    closeRealm();
    lastItemsUpdate = 0;
  }

  @Override public void deleteAll() {
    Realm realm = getRealm();
    beginTransaction();
    realm.allObjects(type).clear();
    commitTransaction();
    closeRealm();
    lastItemsUpdate = 0;
  }

  @Override public boolean isValid(V value) {
    return timeProvider.currentTimeMillis() - lastItemsUpdate < ttlInMillis;
  }

  protected Realm getRealm() {
    if (realm == null) {
      RealmConfiguration config =
          new RealmConfiguration.Builder(context).deleteRealmIfMigrationNeeded().build();
      realm = Realm.getInstance(config);
    }
    return realm;
  }

  protected void closeRealm() {
    if (realm != null) {
      realm.close();
      realm = null;
    }
  }

  protected void beginTransaction() {
    getRealm().beginTransaction();
  }

  protected void commitTransaction() {
    getRealm().commitTransaction();
  }

  private void validatePrimaryKeyFieldName() {
    if (primaryKeyName == null || primaryKeyName.isEmpty()) {
      throw new IllegalStateException(
          "You are trying to do an operation by Key, but you Realm model don't have an PrimaryKey ");
    }
  }
}
