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

package com.karumi.rosie.repository.sample;

import com.karumi.rosie.repository.PaginatedCollection;
import com.karumi.rosie.repository.policy.ReadPolicy;

public class Main {

  public static void main(String[] args) throws Exception {
    SampleRosieRepository repository =
        new SampleRosieRepository(new SampleReadableDataSource(), new SampleWriteableDataSource(),
            new SampleInMemoryCacheDataSource());

    SamplePaginatedRosieRepository paginatedRepository =
        new SamplePaginatedRosieRepository(new SamplePaginatedCacheDataSource());

    PaginatedCollection<SampleValue> values = paginatedRepository.getPage(2, 2);
    for (SampleValue value : values.getItems()) {
      System.out.println(value);
    }

    testCacheUsage(repository);
    testReadPolicies(repository);
  }

  private static void testCacheUsage(SampleRosieRepository repository) throws Exception {
    repository.deleteAll();

    System.out.println("Testing cache usage...");
    System.out.println("Get 0 [non cached]");
    repository.getByKey(new SampleKey(0)); // Non cached
    System.out.println("Get 1 [non cached]");
    repository.getByKey(new SampleKey(1)); // Non cached
    System.out.println("Get 1 [cached]");
    repository.getByKey(new SampleKey(1)); // Cached
    System.out.println("Get 0 [cached]");
    repository.getByKey(new SampleKey(0)); // Cached
    System.out.println("Get 2 [non cached]");
    repository.getByKey(new SampleKey(2)); // Non cached
    System.out.println("Delete all");
    repository.deleteAll();
    System.out.println("Get 0 [non cached]");
    repository.getByKey(new SampleKey(0));
  }

  private static void testReadPolicies(SampleRosieRepository repository) throws Exception {
    repository.deleteAll();

    System.out.println("------------------------");
    System.out.println("Testing read policies...");
    System.out.println("Get 0 [cache only]");
    repository.getByKey(new SampleKey(0), ReadPolicy.CACHE_ONLY); // No item
    System.out.println("Get 0 [readable only]");
    repository.getByKey(new SampleKey(0), ReadPolicy.READABLE_ONLY_AND_POPULATE); // Item
    System.out.println("Get 0 [cache only]");
    repository.getByKey(new SampleKey(0), ReadPolicy.CACHE_ONLY); // Item
    System.out.println("Get 1 [all]");
    repository.getByKey(new SampleKey(1), ReadPolicy.READ_ALL_AND_POPULATE); // Item
  }
}
