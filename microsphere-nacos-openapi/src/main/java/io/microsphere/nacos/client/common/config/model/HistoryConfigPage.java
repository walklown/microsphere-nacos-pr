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
package io.microsphere.nacos.client.common.config.model;

import io.microsphere.nacos.client.common.model.Page;

import java.util.List;

/**
 * The {@link Page} {@link Class} for {@link HistoryConfig}
 *
 * @author <a href="mailto:mercyblitz@gmail.com">Mercy<a/>
 * @see HistoryConfig
 * @see Page
 * @since 1.0.0
 */
public class HistoryConfigPage extends Page<HistoryConfig> {

    public HistoryConfigPage(int totalElements, List<HistoryConfig> elements, int pageNumber, int pageSize) {
        super(totalElements, elements, pageNumber, pageSize);
    }

    public HistoryConfigPage(int totalPages, int totalElements, List<HistoryConfig> elements) {
        super(totalPages, totalElements, elements);
    }
}
