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
package io.microsphere.nacos.client.common.auth.model;

import io.microsphere.nacos.client.common.auth.AuthorizationManager;
import io.microsphere.nacos.client.common.model.Model;

/**
 * The result class for Authentication
 *
 * @author <a href="mailto:mercyblitz@gmail.com">Mercy<a/>
 * @see AuthorizationManager
 * @since 1.0.0
 */
public class Authentication implements Model {

    private static final long serialVersionUID = 5851202743115834552L;

    private String accessToken;

    private long tokenTtl;

    private boolean globalAdmin;

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public long getTokenTtl() {
        return tokenTtl;
    }

    public void setTokenTtl(long tokenTtl) {
        this.tokenTtl = tokenTtl;
    }

    public boolean isGlobalAdmin() {
        return globalAdmin;
    }

    public void setGlobalAdmin(boolean globalAdmin) {
        this.globalAdmin = globalAdmin;
    }
}
