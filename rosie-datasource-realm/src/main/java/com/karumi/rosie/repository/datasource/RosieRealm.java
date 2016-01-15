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
import io.realm.Realm;
import io.realm.RealmConfiguration;

class RosieRealm {

  public static final String REALM_MODULE_NAME = "library.rosie.datasource.realm";
  private final Context context;
  private Realm realm;

  RosieRealm(Context context) {
    this.context = context;
  }

  Realm getRealm() {
    if (realm == null) {
      RealmConfiguration config =
          new RealmConfiguration.Builder(context).name(REALM_MODULE_NAME)
              .setModules(Realm.getDefaultModule(), new DataSourceRealmModule())
              .deleteRealmIfMigrationNeeded()
              .build();
      realm = Realm.getInstance(config);
    }
    return realm;
  }

  void closeRealm() {
    if (realm != null) {
      realm.close();
      realm = null;
    }
  }

  void beginTransaction() {
    getRealm().beginTransaction();
  }

  void commitTransaction() {
    getRealm().commitTransaction();
  }

  public void cancelTransaction() {
    getRealm().cancelTransaction();
  }
}
