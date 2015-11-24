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

import com.karumi.rosie.repository.datasource.ReadableDataSource;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class SampleReadableDataSource implements ReadableDataSource<SampleKey, SampleValue> {

  private static final Map<Integer, SampleValue> VALUES = new HashMap<>();

  static {
    VALUES.put(0, new SampleValue(new SampleKey(0), "Sergio", "Gutiérrez", 27));
    VALUES.put(1, new SampleValue(new SampleKey(1), "Jorge", "Barroso", 28));
    VALUES.put(2, new SampleValue(new SampleKey(2), "Pedro", "Gómez", 29));
    VALUES.put(3, new SampleValue(new SampleKey(3), "Davide", "Mendolia", 30));
    VALUES.put(4, new SampleValue(new SampleKey(4), "Alberto", "Gragera", 31));
    VALUES.put(5, new SampleValue(new SampleKey(5), "Irene", "Herranz", 32));
  }

  @Override public SampleValue getByKey(SampleKey key) {
    SampleValue sampleValue = VALUES.get(key.getIndex());
    System.out.println("Get from readable [" + key + "] -> " + sampleValue);
    return sampleValue;
  }

  @Override public Collection<SampleValue> getAll() {
    Collection<SampleValue> values = VALUES.values();
    System.out.println("Get all from readable -> " + values);
    return values;
  }
}
