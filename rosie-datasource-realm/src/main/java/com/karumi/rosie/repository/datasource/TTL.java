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

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class Ttl extends RealmObject {

  static final String CLASS_NAME = "className";

  @PrimaryKey private String className;
  private long lastUpdate;

  public String getClassName() {
    return className;
  }

  public void setClassName(String className) {
    this.className = className;
  }

  public long getLastUpdate() {
    return lastUpdate;
  }

  public void setLastUpdate(long lastUpdate) {
    this.lastUpdate = lastUpdate;
  }
}
