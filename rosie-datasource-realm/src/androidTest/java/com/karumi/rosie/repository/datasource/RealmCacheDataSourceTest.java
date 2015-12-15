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

import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;
import com.karumi.rosie.dummy.FakeMapper;
import com.karumi.rosie.dummy.FakeObject;
import com.karumi.rosie.dummy.FakeRealmObject;
import com.karumi.rosie.dummy.FakeTimeProvider;
import com.karumi.rosie.time.TimeProvider;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;

import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertNull;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

@RunWith(AndroidJUnit4.class) public class RealmCacheDataSourceTest {

  private static final long ANY_TTL = 500;
  private static final String ANY_VALUE = "any_value";
  private static final String ANY_ID = "ANY";
  private static final long INIT_TIME = 5000;

  @After public void tearDown() throws Exception {
    RealmCacheDataSource<FakeObject, FakeRealmObject> realmCacheDataSource =
        givenRealmCacheDataSource(givenFakeTimeProvider());

    realmCacheDataSource.deleteAll();
  }

  @Test public void shouldReturnValueWhenAddAValueAndGetByKey() {
    RealmCacheDataSource<FakeObject, FakeRealmObject> realmCacheDataSource =
        givenRealmCacheDataSource(givenFakeTimeProvider());
    realmCacheDataSource.addOrUpdate(givenDummyWithId("1"));

    FakeObject result = realmCacheDataSource.getByKey("1");

    assertEquals("1", result.getId());
  }

  @Test public void shouldReturnValueWhenAddMoreThanOneDifferentIdsValueAndGetById() {
    RealmCacheDataSource<FakeObject, FakeRealmObject> realmCacheDataSource =
        givenRealmCacheDataSource(givenFakeTimeProvider());
    realmCacheDataSource.addOrUpdate(givenDummyWithId("1"));
    realmCacheDataSource.addOrUpdate(givenDummyWithId("2"));

    FakeObject resultForOne = realmCacheDataSource.getByKey("1");
    FakeObject resultForTwo = realmCacheDataSource.getByKey("2");

    assertEquals("1", resultForOne.getId());
    assertEquals("2", resultForTwo.getId());
  }

  @Test public void shouldReturnNullGettingItemsNotPreviouslyAdded() {
    RealmCacheDataSource<FakeObject, FakeRealmObject> realmCacheDataSource =
        givenRealmCacheDataSource(givenFakeTimeProvider());
    realmCacheDataSource.addOrUpdate(givenDummyWithId("1"));
    realmCacheDataSource.addOrUpdate(givenDummyWithId("2"));

    FakeObject result = realmCacheDataSource.getByKey("3");

    assertNull(result);
  }

  @Test public void shouldReturnUpdateObjectWhenAddTwoObjectWithSameId() {
    RealmCacheDataSource<FakeObject, FakeRealmObject> realmCacheDataSource =
        givenRealmCacheDataSource(givenFakeTimeProvider());
    realmCacheDataSource.addOrUpdate(givenDummyWithId("1"));
    realmCacheDataSource.addOrUpdate(givenDummyWithIdAndValue("1", "Updated Value"));

    FakeObject result = realmCacheDataSource.getByKey("1");

    assertEquals("Updated Value", result.getValue());
  }

  @Test public void shouldReturnAllPersistedItems() {
    RealmCacheDataSource<FakeObject, FakeRealmObject> realmCacheDataSource =
        givenRealmCacheDataSource(givenFakeTimeProvider());
    realmCacheDataSource.addOrUpdate(givenDummyWithId("1"));
    realmCacheDataSource.addOrUpdate(givenDummyWithId("2"));

    Collection<FakeObject> all = realmCacheDataSource.getAll();

    assertEquals(2, all.size());
    List<FakeObject> objects = new ArrayList<>(all);
    assertEquals("1", objects.get(0).getId());
    assertEquals("2", objects.get(1).getId());
  }

  @Test public void shouldPersistACollectionOfItems() {
    RealmCacheDataSource<FakeObject, FakeRealmObject> realmCacheDataSource =
        givenRealmCacheDataSource(givenFakeTimeProvider());
    List<FakeObject> fakeObjects = new ArrayList<>();
    fakeObjects.add(givenDummyWithId("1"));
    fakeObjects.add(givenDummyWithId("2"));

    realmCacheDataSource.addOrUpdateAll(fakeObjects);

    Collection<FakeObject> all = realmCacheDataSource.getAll();
    assertEquals(2, all.size());
    List<FakeObject> objects = new ArrayList<>(all);
    assertEquals("1", objects.get(0).getId());
    assertEquals("2", objects.get(1).getId());
  }

  @Test public void shouldReturnIsNotValidWhenDataSourceIsInitialized() {
    FakeTimeProvider fakeTimeProvider = givenFakeTimeProvider();
    fakeTimeProvider.setTime(INIT_TIME);
    RealmCacheDataSource<FakeObject, FakeRealmObject> realmCacheDataSource =
        givenRealmCacheDataSource(fakeTimeProvider);

    boolean valid = realmCacheDataSource.isValid(givenDummyWithId(ANY_ID));

    assertFalse(valid);
  }

  @Test public void shouldReturnIsValidIfTheTtlHasNotExpired() {
    FakeTimeProvider fakeTimeProvider = givenFakeTimeProvider();
    fakeTimeProvider.setTime(INIT_TIME);
    RealmCacheDataSource<FakeObject, FakeRealmObject> realmCacheDataSource =
        givenRealmCacheDataSource(fakeTimeProvider);
    FakeObject fakeObject = givenDummyWithId(ANY_ID);
    realmCacheDataSource.addOrUpdate(fakeObject);

    boolean valid = realmCacheDataSource.isValid(fakeObject);

    assertTrue(valid);
  }

  @Test public void shouldReturnIsValidIfTheTtlHasNotExpiredAndTheDataIsAddedAsACollection() {
    FakeTimeProvider fakeTimeProvider = givenFakeTimeProvider();
    fakeTimeProvider.setTime(INIT_TIME);
    RealmCacheDataSource<FakeObject, FakeRealmObject> realmCacheDataSource =
        givenRealmCacheDataSource(fakeTimeProvider);
    FakeObject fakeObject = givenDummyWithId(ANY_ID);
    List<FakeObject> fakeObjects = new ArrayList<>();
    fakeObjects.add(fakeObject);
    realmCacheDataSource.addOrUpdateAll(fakeObjects);

    boolean valid = realmCacheDataSource.isValid(fakeObject);

    assertTrue(valid);
  }

  @Test public void shouldReturnIsInValidWhenDataIsAddedAndTimePassOverTTL() {
    FakeTimeProvider fakeTimeProvider = givenFakeTimeProvider();
    fakeTimeProvider.setTime(INIT_TIME);
    RealmCacheDataSource<FakeObject, FakeRealmObject> realmCacheDataSource =
        givenRealmCacheDataSource(fakeTimeProvider);
    FakeObject fakeObject = givenDummyWithId(ANY_ID);
    List<FakeObject> fakeObjects = new ArrayList<>();
    fakeObjects.add(fakeObject);
    realmCacheDataSource.addOrUpdateAll(fakeObjects);

    fakeTimeProvider.incTime(ANY_TTL + 1);
    boolean valid = realmCacheDataSource.isValid(fakeObject);

    assertFalse(valid);
  }

  @Test public void shouldClearTheDataBaseOnDeleteAll() {
    RealmCacheDataSource<FakeObject, FakeRealmObject> realmCacheDataSource =
        givenRealmCacheDataSource(givenFakeTimeProvider());
    realmCacheDataSource.addOrUpdate(givenDummyWithId("1"));

    realmCacheDataSource.deleteAll();

    Collection<FakeObject> all = realmCacheDataSource.getAll();
    assertEquals(0, all.size());
  }

  @Test public void shouldIsNotValidWhenDeleteAll() {
    FakeTimeProvider fakeTimeProvider = givenFakeTimeProvider();
    fakeTimeProvider.setTime(INIT_TIME);
    RealmCacheDataSource<FakeObject, FakeRealmObject> realmCacheDataSource =
        givenRealmCacheDataSource(fakeTimeProvider);
    FakeObject anyFakeObject = givenDummyWithId("1");
    realmCacheDataSource.addOrUpdate(anyFakeObject);

    realmCacheDataSource.deleteAll();

    assertFalse(realmCacheDataSource.isValid(anyFakeObject));
  }

  @Test public void shouldRemoveObjectWhenRemoveById() {
    RealmCacheDataSource<FakeObject, FakeRealmObject> realmCacheDataSource =
        givenRealmCacheDataSource(givenFakeTimeProvider());
    realmCacheDataSource.addOrUpdate(givenDummyWithId("1"));

    realmCacheDataSource.deleteByKey("1");

    FakeObject fakeObject = realmCacheDataSource.getByKey("1");
    assertNull(fakeObject);
  }

  @Test public void shouldBeAgnosticWhenRemoveByIdAndIdDoesNotExist() {
    RealmCacheDataSource<FakeObject, FakeRealmObject> realmCacheDataSource =
        givenRealmCacheDataSource(givenFakeTimeProvider());
    realmCacheDataSource.addOrUpdate(givenDummyWithId("1"));

    realmCacheDataSource.deleteByKey("2");

    FakeObject fakeObject = realmCacheDataSource.getByKey("1");
    assertNotNull(fakeObject);
  }

  private FakeObject givenDummyWithId(String id) {
    return givenDummyWithIdAndValue(id, ANY_VALUE);
  }

  private FakeObject givenDummyWithIdAndValue(String id, String value) {
    return new FakeObject(id, value);
  }

  private FakeTimeProvider givenFakeTimeProvider() {
    return new FakeTimeProvider();
  }

  private RealmCacheDataSource<FakeObject, FakeRealmObject> givenRealmCacheDataSource(
      TimeProvider timeProvider) {
    return new RealmCacheDataSource<>(new FakeMapper(), FakeRealmObject.class,
        FakeRealmObject.PRIMARY_KEY_NAME, timeProvider, ANY_TTL,
        InstrumentationRegistry.getContext());
  }
}