/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package org.apache.iceberg.aws.s3;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.Map;
import org.apache.iceberg.aws.AwsClientProperties;
import org.apache.iceberg.relocated.com.google.common.collect.ImmutableMap;
import org.junit.jupiter.api.Test;
import software.amazon.awssdk.services.s3.S3AsyncClient;
import software.amazon.awssdk.services.s3.S3Client;

@SuppressWarnings("resource")
public class TestPrefixedS3Client {

  @Test
  public void invalidParameters() {
    assertThatThrownBy(() -> new PrefixedS3Client(null, null, null, null))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessage("Invalid storage prefix: null or empty");

    assertThatThrownBy(() -> new PrefixedS3Client("", null, null, null))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessage("Invalid storage prefix: null or empty");

    assertThatThrownBy(() -> new PrefixedS3Client("s3://bucket", null, null, null))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessage("Invalid properties: null");
  }

  @Test
  public void validParameters() {
    Map<String, String> properties =
        ImmutableMap.of(AwsClientProperties.CLIENT_REGION, "us-east-1");
    PrefixedS3Client client = new PrefixedS3Client("s3", properties, null, null);
    assertThat(client.storagePrefix()).isEqualTo("s3");
    assertThat(client.s3FileIOProperties().properties())
        .isEqualTo(new S3FileIOProperties(properties).properties());
    assertThat(client.s3()).isInstanceOf(S3Client.class);
    assertThat(client.s3Async()).isInstanceOf(S3AsyncClient.class);
  }
}
