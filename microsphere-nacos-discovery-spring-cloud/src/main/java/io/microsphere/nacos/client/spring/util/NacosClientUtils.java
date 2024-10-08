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
package io.microsphere.nacos.client.spring.util;

import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.MapPropertySource;
import org.springframework.core.env.PropertySource;

import java.util.Map;

import static io.microsphere.spring.util.PropertySourcesUtils.getSubProperties;

/**
 * The utilities class for Nacos Client
 *
 * @author <a href="mailto:mercyblitz@gmail.com">Mercy</a>
 * @since 1.0.0
 */
public abstract class NacosClientUtils {

    public static final String NACOS_CLIENTS_PROPERTY_NAME_PREFIX = "microsphere.nacos.clients.";

    public static String buildNacosClientPropertyNamePrefix(String nacosClientName) {
        return NACOS_CLIENTS_PROPERTY_NAME_PREFIX + nacosClientName + ".";
    }

    public static Map<String, Object> getNacosClientProperties(ConfigurableEnvironment environment, String nacosClientName) {
        String prefix = buildNacosClientPropertyNamePrefix(nacosClientName);
        return getSubProperties(environment, prefix);
    }

    public static PropertySource getNacosCilentPropertySource(ConfigurableEnvironment environment, String nacosClientName) {
        Map properties = getNacosClientProperties(environment, nacosClientName);
        return new MapPropertySource(nacosClientName, properties);
    }

}
