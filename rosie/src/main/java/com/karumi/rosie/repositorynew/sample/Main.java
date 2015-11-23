/*
 * Copyright (C) 2015 Karumi.
 */

package com.karumi.rosie.repositorynew.sample;

import com.karumi.rosie.repository.PaginatedCollection;
import com.karumi.rosie.repositorynew.Repository;

public class Main {

  public static void main(String[] args) throws Exception {
    SampleRepository repository = new SampleRepository(new SampleReadable(), new SampleWriteable(),
        new SampleInMemoryCache());

    SamplePaginatedRepository paginatedRepository =
        new SamplePaginatedRepository(new SamplePaginatedCache());

    PaginatedCollection<SampleValue> values = paginatedRepository.get(2, 2);
    for (SampleValue value : values.getItems()) {
      System.out.println(value);
    }

    testCacheUsage(repository);
    testReadPolicies(repository);
  }

  private static void testCacheUsage(SampleRepository repository) throws Exception {
    repository.deleteAll();

    System.out.println("Testing cache usage...");
    System.out.println("Get 0 [non cached]");
    repository.get(new SampleKey(0)); // Non cached
    System.out.println("Get 1 [non cached]");
    repository.get(new SampleKey(1)); // Non cached
    System.out.println("Get 1 [cached]");
    repository.get(new SampleKey(1)); // Cached
    System.out.println("Get 0 [cached]");
    repository.get(new SampleKey(0)); // Cached
    System.out.println("Get 2 [non cached]");
    repository.get(new SampleKey(2)); // Non cached
    System.out.println("Delete all");
    repository.deleteAll();
    System.out.println("Get 0 [non cached]");
    repository.get(new SampleKey(0));
  }

  private static void testReadPolicies(SampleRepository repository) throws Exception {
    repository.deleteAll();

    System.out.println("------------------------");
    System.out.println("Testing read policies...");
    System.out.println("Get 0 [cache only]");
    repository.get(new SampleKey(0), Repository.ReadPolicy.CACHE_ONLY); // No item
    System.out.println("Get 0 [readable only]");
    repository.get(new SampleKey(0), Repository.ReadPolicy.READABLE_AND_POPULATE); // Item
    System.out.println("Get 0 [cache only]");
    repository.get(new SampleKey(0), Repository.ReadPolicy.CACHE_ONLY); // Item
    System.out.println("Get 1 [all]");
    repository.get(new SampleKey(1), Repository.ReadPolicy.READ_ALL_AND_POPULATE); // Item
  }
}
