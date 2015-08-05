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

import com.karumi.rosie.UnitTest;
import com.karumi.rosie.doubles.AnyCacheableItem;
import java.util.Collection;
import java.util.LinkedList;
import org.junit.Test;
import org.mockito.Mock;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class RosieRepositoryTest extends UnitTest {

  @Mock private DataSource<AnyCacheableItem> cacheDataSource;
  @Mock private DataSource<AnyCacheableItem> apiDataSource;

  @Test public void shouldReturnNullIfThereAreNoDataSourcesWithData() throws Exception {
    givenTheDataSourcesHasNoData();
    RosieRepository<AnyCacheableItem> repository = givenARepositoryWithTwoDataSources();

    Collection<AnyCacheableItem> data = repository.getAll();

    assertNull(data);
  }

  @Test public void shouldReturnDataFromTheFirstDataSourceIfTheDataIsValid() throws Exception {
    Collection<AnyCacheableItem> cacheData = givenTheCacheReturnsValidData();
    RosieRepository<AnyCacheableItem> repository = givenARepositoryWithTwoDataSources();

    Collection<AnyCacheableItem> data = repository.getAll();

    assertEquals(cacheData, data);
  }

  @Test public void shouldReturnDataFromTheLastDataSourceIfTheFirstOneReturnNullOnGetAll()
      throws Exception {
    Collection<AnyCacheableItem> apiData = givenTheCacheReturnsNullAndTheApiReturnSomeData();
    RosieRepository<AnyCacheableItem> repository = givenARepositoryWithTwoDataSources();

    Collection<AnyCacheableItem> data = repository.getAll();

    assertEquals(apiData, data);
  }

  @Test public void shouldReturnDataFromTheLastDataSourceIfTheFirstOneReturnsNoValidData()
      throws Exception {
    Collection<AnyCacheableItem> apiData = givenTheCacheReturnsNoValidDataAndTheApiReturnsData();
    RosieRepository<AnyCacheableItem> repository = givenARepositoryWithTwoDataSources();

    Collection<AnyCacheableItem> data = repository.getAll();

    assertEquals(apiData, data);
  }

  @Test public void shouldReturnDataFromTheLastDataSourceEvenIfIsMarkedAsInvalid()
      throws Exception {
    Collection<AnyCacheableItem> apiData = givenCachedDataIsNotValidNeitherAPI();
    RosieRepository<AnyCacheableItem> repository = givenARepositoryWithTwoDataSources();

    Collection<AnyCacheableItem> data = repository.getAll();

    assertEquals(apiData, data);
  }

  @Test(expected = Exception.class) public void shouldPropagateExceptionsThrownByAnyDataSource()
      throws Exception {
    givenAnyDataSourceThrowsAnException();
    RosieRepository<AnyCacheableItem> repository = givenARepositoryWithTwoDataSources();

    repository.getAll();
  }

  @Test public void shouldGetDataFromTheLastDataSourceIfTheForceLoadFlagIsEnabled()
      throws Exception {
    givenDataSourcesReturnValidData();
    RosieRepository<AnyCacheableItem> repository = givenARepositoryWithTwoDataSources();

    boolean forceLoad = true;
    repository.getAll(forceLoad);

    verify(cacheDataSource, never()).getAll();
    verify(apiDataSource).getAll();
  }

  @Test public void shouldReturnJustDataMatchingWithThePredicate() throws Exception {
    final Collection<AnyCacheableItem> cacheData = givenTheCacheReturnsValidData();
    RosieRepository<AnyCacheableItem> repository = givenARepositoryWithTwoDataSources();

    Collection<AnyCacheableItem> filteredData = repository.get(new Predicate<AnyCacheableItem>() {
      @Override public boolean isValid(AnyCacheableItem item) {
        return item.getId().equals("1") || item.getId().equals("2");
      }
    });

    assertEquals(2, filteredData.size());
    assertTrue(filteredData.size() < cacheData.size());
    assertTrue(filteredData.contains(new AnyCacheableItem("1")));
    assertTrue(filteredData.contains(new AnyCacheableItem("2")));
  }

  @Test public void shouldPopulateFasterDataSources() throws Exception {
    final Collection<AnyCacheableItem> cacheData =
        givenTheCacheReturnsNoValidDataAndTheApiReturnsData();
    RosieRepository<AnyCacheableItem> repository = givenARepositoryWithTwoDataSources();

    repository.getAll();

    verify(cacheDataSource).addOrUpdate(cacheData);
  }

  @Test public void shouldDeleteAllFromTheDataSourceIfTheDataIsNotValid() throws Exception {
    givenTheCacheReturnsNoValidDataAndTheApiReturnsData();
    RosieRepository<AnyCacheableItem> repository = givenARepositoryWithTwoDataSources();

    repository.getAll();

    verify(cacheDataSource).deleteAll();
  }

  @Test(expected = IllegalArgumentException.class) public void shouldNotAcceptNullIds()
      throws Exception {
    RosieRepository<AnyCacheableItem> repository = givenARepositoryWithTwoDataSources();

    String nullId = null;
    repository.get(nullId);
  }

  @Test(expected = IllegalArgumentException.class) public void shouldNotAcceptNullPredicates()
      throws Exception {
    RosieRepository<AnyCacheableItem> repository = givenARepositoryWithTwoDataSources();

    Predicate nullPredicate = null;
    repository.get(nullPredicate);
  }

  private void givenDataSourcesReturnValidData() throws Exception {
    Collection<AnyCacheableItem> cacheData = getSomeItems();
    Collection<AnyCacheableItem> apiData = getSomeItems();
    when(cacheDataSource.getAll()).thenReturn(cacheData);
    when(cacheDataSource.isValid(any(AnyCacheableItem.class))).thenReturn(true);
    when(apiDataSource.getAll()).thenReturn(apiData);
    when(apiDataSource.isValid(any(AnyCacheableItem.class))).thenReturn(true);
  }

  private void givenAnyDataSourceThrowsAnException() throws Exception {
    when(apiDataSource.getAll()).thenThrow(new Exception());
  }

  private Collection<AnyCacheableItem> givenCachedDataIsNotValidNeitherAPI() throws Exception {
    Collection<AnyCacheableItem> cacheData = getSomeItems();
    Collection<AnyCacheableItem> apiData = getSomeItems();
    when(cacheDataSource.getAll()).thenReturn(cacheData);
    when(cacheDataSource.isValid(any(AnyCacheableItem.class))).thenReturn(false);
    when(apiDataSource.getAll()).thenReturn(apiData);
    when(apiDataSource.isValid(any(AnyCacheableItem.class))).thenReturn(false);
    return apiData;
  }

  private Collection<AnyCacheableItem> givenTheCacheReturnsNoValidDataAndTheApiReturnsData()
      throws Exception {
    Collection<AnyCacheableItem> cacheData = getSomeItems();
    Collection<AnyCacheableItem> apiData = getSomeItems();
    when(cacheDataSource.getAll()).thenReturn(cacheData);
    when(cacheDataSource.isValid(any(AnyCacheableItem.class))).thenReturn(false);
    when(apiDataSource.getAll()).thenReturn(apiData);
    return apiData;
  }

  private Collection<AnyCacheableItem> givenTheCacheReturnsValidData() throws Exception {
    Collection<AnyCacheableItem> data = getSomeItems();
    when(cacheDataSource.getAll()).thenReturn(data);
    when(cacheDataSource.isValid(any(AnyCacheableItem.class))).thenReturn(true);
    when(apiDataSource.getAll()).thenReturn(null);
    return data;
  }

  private void givenTheDataSourcesHasNoData() throws Exception {
    when(cacheDataSource.getAll()).thenReturn(null);
    when(apiDataSource.getAll()).thenReturn(null);
  }

  private Collection<AnyCacheableItem> givenTheCacheReturnsNullAndTheApiReturnSomeData()
      throws Exception {
    when(cacheDataSource.getAll()).thenReturn(null);
    Collection<AnyCacheableItem> apiData = getSomeItems();
    when(apiDataSource.getAll()).thenReturn(apiData);
    return apiData;
  }

  private RosieRepository<AnyCacheableItem> givenARepositoryWithTwoDataSources() {
    return RosieRepository.with(cacheDataSource, apiDataSource);
  }

  private LinkedList<AnyCacheableItem> getSomeItems() {
    LinkedList<AnyCacheableItem> anyCacheableItems = new LinkedList<>();
    for (int i = 0; i < 10; i++) {
      anyCacheableItems.add(new AnyCacheableItem(String.valueOf(i)));
    }
    return anyCacheableItems;
  }
}