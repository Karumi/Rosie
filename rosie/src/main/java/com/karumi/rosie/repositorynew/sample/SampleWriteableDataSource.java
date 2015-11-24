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

package com.karumi.rosie.repositorynew.sample;

import com.karumi.rosie.repositorynew.datasource.WriteableDataSource;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class SampleWriteableDataSource implements WriteableDataSource<SampleKey, SampleValue> {

  private final Map<SampleKey, SampleValue> writes = new HashMap<>();

  @Override public SampleValue addOrUpdate(SampleValue value) {
    return writes.put(value.getKey(), value);
  }

  @Override public Collection<SampleValue> addOrUpdateAll(Collection<SampleValue> values) {
    for (SampleValue value : values) {
      addOrUpdate(value);
    }
    return values;
  }

  @Override public void deleteByKey(SampleKey key) {
    writes.remove(key);
  }

  @Override public void deleteAll() {
    writes.clear();
  }
}
