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
package io.microsphere.nacos.client.common.config.io;

import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import io.microsphere.nacos.client.common.config.ConfigClient;
import io.microsphere.nacos.client.common.config.ConfigOperationType;
import io.microsphere.nacos.client.common.config.model.HistoryConfig;
import io.microsphere.nacos.client.io.GsonDeserializer;

import java.lang.reflect.Type;
import java.text.ParseException;
import java.text.SimpleDateFormat;

/**
 * The {@link GsonDeserializer} class for {@link HistoryConfig}
 *
 * @author <a href="mailto:mercyblitz@gmail.com">Mercy<a/>
 * @see HistoryConfig
 * @see HistoryConfigPageDeserializer
 * @see ConfigClient#getHistoryConfigs(String, String, String, int, int)
 * @since 1.0.0
 */
public class HistoryConfigDeserializer extends BaseConfigDeserializer<HistoryConfig> {

    private static final String REVISION_MEMBER_NAME = "id";

    private static final String LAST_REVISION_MEMBER_NAME = "lastId";

    private static final String OPERATOR_IP_MEMBER_NAME = "srcIp";

    private static final String OPERATOR_MEMBER_NAME = "srcUser";

    private static final String CONFIG_OPERATION_TYPE_MEMBER_NAME = "opType";

    private static final String CREATED_TIME_TYPE_MEMBER_NAME = "createdTime";

    private static final String LAST_MODIFIED_TIME_TYPE_MEMBER_NAME = "lastModifiedTime";

    /**
     * e.g : 2010-05-05T00:00:00.000+08:00
     */
    private static final String DATE_FORMAT = "YYYY-MM-DD'T'HH:mm:ss.SSSXXX";

    @Override
    protected void deserialize(JsonObject jsonObject, HistoryConfig historyConfig, Type typeOfT) throws JsonParseException {
        String revision = getString(jsonObject, REVISION_MEMBER_NAME);
        Long lastRevision = getLong(jsonObject, LAST_REVISION_MEMBER_NAME);
        String operationType = getString(jsonObject, CONFIG_OPERATION_TYPE_MEMBER_NAME);
        String createdTime = getString(jsonObject, CREATED_TIME_TYPE_MEMBER_NAME);
        String lastModifiedTime = getString(jsonObject, LAST_MODIFIED_TIME_TYPE_MEMBER_NAME);

        SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT);

        historyConfig.setRevision(Long.parseLong(revision));
        historyConfig.setLastRevision(lastRevision);
        historyConfig.setOperationType(ConfigOperationType.of(operationType));
        try {
            historyConfig.setCreatedTime(dateFormat.parse(createdTime).getTime());
            historyConfig.setLastModifiedTime(dateFormat.parse(lastModifiedTime).getTime());
        } catch (ParseException e) {
            throw new JsonParseException(e);
        }
    }

    @Override
    protected HistoryConfig newConfig() {
        return new HistoryConfig();
    }

    @Override
    protected String getOperatorMemberName() {
        return OPERATOR_MEMBER_NAME;
    }

    @Override
    protected String getOperatorIpMemberName() {
        return OPERATOR_IP_MEMBER_NAME;
    }
}
