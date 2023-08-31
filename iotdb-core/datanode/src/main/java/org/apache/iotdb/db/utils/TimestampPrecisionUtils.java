/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package org.apache.iotdb.db.utils;

import org.apache.iotdb.commons.conf.CommonDescriptor;

import java.util.concurrent.TimeUnit;

public class TimestampPrecisionUtils {
  private static final String TIMESTAMP_PRECISION =
      CommonDescriptor.getInstance().getConfig().getTimestampPrecision();

  private static final long FACTOR;

  @FunctionalInterface
  private interface ConvertFunction<T1, T2, R> {
    R apply(T1 t1, T2 t2);
  }

  private static final ConvertFunction<Long, TimeUnit, Long> convertFunction;

  static {
    switch (TIMESTAMP_PRECISION) {
      case "ms":
        convertFunction = TimeUnit.MILLISECONDS::convert;
        FACTOR = 1_000_000L;
        break;
      case "us":
        convertFunction = TimeUnit.MICROSECONDS::convert;
        FACTOR = 1_000L;
        break;
      case "ns":
        convertFunction = TimeUnit.NANOSECONDS::convert;
        FACTOR = 1L;
        break;
        // this case will never reach
      default:
        convertFunction = null;
        FACTOR = 0;
    }
  }

  /** convert specific precision timestamp to current precision timestamp */
  public static long convertToCurrPrecision(long sourceTime, TimeUnit sourceUnit) {
    return convertFunction.apply(sourceTime, sourceUnit);
  }

  public static long getFactor() {
    return FACTOR;
  }
}
