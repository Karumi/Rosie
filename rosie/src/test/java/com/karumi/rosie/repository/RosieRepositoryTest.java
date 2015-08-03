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
import java.util.Collection;
import java.util.LinkedList;
import org.junit.Test;
import org.mockito.Mock;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

public class RosieRepositoryTest extends UnitTest {

  @Mock public DataSource<Object> inMemoryDataSource;
  @Mock public DataSource<Object> apiDataSource;

  @Test public void shouldReturnDataFromTheLastDataSourceIfTheFirstOneReturnNullOnGetAll() {
    Collection<Object> allData = givenTheCacheReturnsNullAndTheApiReturnSomeData();
    RosieRepository<Object> repository = givenARepositoryWithTwoDataSources();

    Collection<Object> data = repository.getAll();

    assertEquals(allData, data);
  }

  @Test public void shouldReturnNullIfThereAreNoDataSourcesWithContent(){
    RosieRepository<Object> repository = givenARepositoryWithTwoDataSources();

    Collection<Object> data = repository.getAll();

    assertEquals(allData, data);
  }

  private Collection<Object> givenTheCacheReturnsNullAndTheApiReturnSomeData() {
    when(inMemoryDataSource.getAll()).thenReturn(null);
    Collection<Object> allData = new LinkedList<>();
    when(apiDataSource.getAll()).thenReturn(allData);
    return allData;
  }

  private RosieRepository<Object> givenARepositoryWithTwoDataSources() {
    return RosieRepository.with(inMemoryDataSource, apiDataSource);
  }
}