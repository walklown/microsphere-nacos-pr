/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.microsphere.nacos.client;

import io.microsphere.nacos.client.transport.OpenApiClient;
import io.microsphere.nacos.client.transport.OpenApiHttpClient;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;

import java.util.concurrent.TimeUnit;

import static java.lang.String.format;

/**
 * Abstract Test class for Open API
 * The sub-class must
 *
 * @author <a href="mailto:mercyblitz@gmail.com">Mercy<a/>
 * @see OpenApiHttpClient
 * @since 1.0.0
 */
@ExtendWith(OpenApiTestContainersExtension.class)
public abstract class OpenApiTest {

    protected static final String TEST_NAMESPACE_ID = "test";

    protected static final String TEST_GROUP_NAME = "test-group";

    protected static final String SERVER_ADDRESS_PROPERTY_NAME = "SERVER_ADDRESS";

    protected static final String USER_NAME = "nacos";

    protected static final String PASSWORD = "nacos";

    protected OpenApiClient openApiClient;

    protected NacosClientConfig nacosClientConfig;

    static String getServerAddress() {
        String key = SERVER_ADDRESS_PROPERTY_NAME;
        return System.getProperty(key, System.getenv(key));
    }

    @BeforeEach
    public void init() {
        String serverAddress = getServerAddress();
        if (serverAddress == null) {
            String errorMessage = format("The Java System Property[ name : '%s' ] for Nacos Server must be set!", SERVER_ADDRESS_PROPERTY_NAME);
            throw new IllegalArgumentException(errorMessage);
        }
        NacosClientConfig config = new NacosClientConfig();
        config.setServerAddress(serverAddress);
        customize(config);
        this.openApiClient = new OpenApiHttpClient(config);
        this.nacosClientConfig = config;

        setup();
    }

    /**
     * Customize the {@link NacosClientConfig}
     *
     * @param nacosClientConfig {@link NacosClientConfig}
     */
    protected void customize(NacosClientConfig nacosClientConfig) {
    }

    protected void setup() {
    }

    protected void await(long waitTimeInSeconds) {
        long waitTime = TimeUnit.SECONDS.toMillis(waitTimeInSeconds);
        try {
            Thread.sleep(waitTime);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    @AfterEach
    public void destroy() throws Exception {
        if (openApiClient != null) {
            openApiClient.close();
        }
    }
}
