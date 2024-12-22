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
package io.microsphere.nacos.client.v2;

import io.microsphere.nacos.client.NacosClientConfig;
import io.microsphere.nacos.client.OpenApiVersion;
import io.microsphere.nacos.client.common.OpenApiTemplateClient;
import io.microsphere.nacos.client.common.auth.AuthenticationClient;
import io.microsphere.nacos.client.common.auth.OpenApiAuthenticationClient;
import io.microsphere.nacos.client.common.auth.model.Authentication;
import io.microsphere.nacos.client.common.config.ConfigClient;
import io.microsphere.nacos.client.common.config.ConfigType;
import io.microsphere.nacos.client.common.config.event.ConfigChangedListener;
import io.microsphere.nacos.client.common.config.model.Config;
import io.microsphere.nacos.client.common.config.model.HistoryConfig;
import io.microsphere.nacos.client.common.config.model.NewConfig;
import io.microsphere.nacos.client.common.discovery.ConsistencyType;
import io.microsphere.nacos.client.common.discovery.InstanceClient;
import io.microsphere.nacos.client.common.discovery.ServiceClient;
import io.microsphere.nacos.client.common.discovery.model.BatchMetadataResult;
import io.microsphere.nacos.client.common.discovery.model.DeleteInstance;
import io.microsphere.nacos.client.common.discovery.model.Heartbeat;
import io.microsphere.nacos.client.common.discovery.model.Instance;
import io.microsphere.nacos.client.common.discovery.model.InstancesList;
import io.microsphere.nacos.client.common.discovery.model.NewInstance;
import io.microsphere.nacos.client.common.discovery.model.QueryInstance;
import io.microsphere.nacos.client.common.discovery.model.Service;
import io.microsphere.nacos.client.common.discovery.model.UpdateHealthInstance;
import io.microsphere.nacos.client.common.discovery.model.UpdateInstance;
import io.microsphere.nacos.client.common.model.Page;
import io.microsphere.nacos.client.common.namespace.NamespaceClient;
import io.microsphere.nacos.client.common.namespace.model.Namespace;
import io.microsphere.nacos.client.transport.OpenApiClient;
import io.microsphere.nacos.client.transport.OpenApiHttpClient;
import io.microsphere.nacos.client.transport.OpenApiRequest;
import io.microsphere.nacos.client.v1.config.OpenApiConfigClient;
import io.microsphere.nacos.client.v1.discovery.OpenApiInstanceClient;
import io.microsphere.nacos.client.v1.discovery.OpenApiServiceClient;
import io.microsphere.nacos.client.v1.namespace.OpenApiNamespaceClient;
import io.microsphere.nacos.client.v1.raft.OpenApiRaftClient;
import io.microsphere.nacos.client.v1.raft.RaftClient;
import io.microsphere.nacos.client.v1.server.OpenApiServerClient;
import io.microsphere.nacos.client.v1.server.ServerClient;
import io.microsphere.nacos.client.v2.client.model.ClientDetail;
import io.microsphere.nacos.client.v2.client.model.ClientInfo;
import io.microsphere.nacos.client.v2.client.model.ClientInstance;
import io.microsphere.nacos.client.v2.client.model.ClientSubscriber;
import io.microsphere.nacos.client.v2.config.OpenApiConfigClientV2;
import io.microsphere.nacos.client.v2.discovery.OpenApiInstanceClientV2;
import io.microsphere.nacos.client.v2.discovery.OpenApiServiceClientV2;
import io.microsphere.nacos.client.v2.namespace.OpenApiNamespaceClientV2;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import static io.microsphere.nacos.client.OpenApiVersion.V2;
import static io.microsphere.nacos.client.common.discovery.ConsistencyType.EPHEMERAL;
import static io.microsphere.nacos.client.http.HttpMethod.GET;
import static io.microsphere.nacos.client.transport.OpenApiRequestParam.CLIENT_ID;
import static io.microsphere.nacos.client.transport.OpenApiRequestParam.INSTANCE_EPHEMERAL;
import static io.microsphere.nacos.client.transport.OpenApiRequestParam.INSTANCE_IP;
import static io.microsphere.nacos.client.transport.OpenApiRequestParam.INSTANCE_PORT;
import static io.microsphere.nacos.client.transport.OpenApiRequestParam.NAMESPACE_ID;
import static io.microsphere.nacos.client.transport.OpenApiRequestParam.SERVICE_GROUP_NAME;
import static io.microsphere.nacos.client.transport.OpenApiRequestParam.SERVICE_NAME;
import static io.microsphere.nacos.client.util.TypeUtils.ofParameterizedType;

/**
 * {@link NacosClientV2} for Open API
 *
 * @author <a href="mailto:mercyblitz@gmail.com">Mercy<a/>
 * @see NacosClientV2
 * @since 1.0.0
 */
public class OpenApiNacosClientV2 extends OpenApiTemplateClient implements NacosClientV2 {

    protected static final String CLIENT_ENDPOINT = "/v2/ns/client";

    protected static final String CLIENT_LIST_ENDPOINT = CLIENT_ENDPOINT + "/list";

    protected static final String CLIENT_REGISTERED_INSTANCES_ENDPOINT = CLIENT_ENDPOINT + "/publish/list";

    protected static final String CLIENT_SUBSCRIBERS_ENDPOINT = CLIENT_ENDPOINT + "/subscribe/list";

    protected static final String REGISTERED_CLIENTS_ENDPOINT = CLIENT_ENDPOINT + "/service/publisher/list";

    protected static final String SUBSCRIBED_CLIENTS_ENDPOINT = CLIENT_ENDPOINT + "/service/subscriber/list";

    private final AuthenticationClient authenticationClient;

    private final ConfigClient configClient;

    private final ServiceClient serviceClient;

    private final InstanceClient instanceClient;

    private final NamespaceClient namespaceClient;

    private final ServerClient serverClient;

    private final RaftClient raftClient;

    public OpenApiNacosClientV2(NacosClientConfig nacosClientConfig) {
        this(new OpenApiHttpClient(nacosClientConfig), nacosClientConfig);
    }

    public OpenApiNacosClientV2(OpenApiClient openApiClient, NacosClientConfig nacosClientConfig) {
        super(openApiClient, nacosClientConfig);
        this.authenticationClient = new OpenApiAuthenticationClient(openApiClient, nacosClientConfig);
        this.configClient = new OpenApiConfigClientV2(openApiClient, nacosClientConfig);
        this.serviceClient = new OpenApiServiceClientV2(openApiClient, nacosClientConfig);
        this.instanceClient = new OpenApiInstanceClientV2(openApiClient, nacosClientConfig);
        this.namespaceClient = new OpenApiNamespaceClientV2(openApiClient, nacosClientConfig);
        this.serverClient = new OpenApiServerClient(openApiClient, nacosClientConfig);
        this.raftClient = new OpenApiRaftClient(openApiClient, nacosClientConfig);
    }

    @Override
    public OpenApiVersion getOpenApiVersion() {
        return V2;
    }

    public AuthenticationClient getAuthenticationClient() {
        return authenticationClient;
    }

    public ConfigClient getConfigClient() {
        return configClient;
    }

    public ServiceClient getServiceClient() {
        return serviceClient;
    }

    public InstanceClient getInstanceClient() {
        return instanceClient;
    }

    public NamespaceClient getNamespaceClient() {
        return namespaceClient;
    }

    public ServerClient getServerClient() {
        return serverClient;
    }

    public RaftClient getRaftClient() {
        return raftClient;
    }

    @Override
    public Authentication authenticate() {
        return authenticationClient.authenticate();
    }

    @Override
    public Authentication authenticate(String userName, String password) {
        return authenticationClient.authenticate(userName, password);
    }

    @Override
    public String getConfigContent(String dataId) {
        return configClient.getConfigContent(dataId);
    }

    @Override
    public String getConfigContent(String group, String dataId) {
        return configClient.getConfigContent(group, dataId);
    }

    @Override
    public String getConfigContent(String namespaceId, String group, String dataId) {
        return configClient.getConfigContent(namespaceId, group, dataId);
    }

    @Override
    public String getConfigContent(String namespaceId, String group, String dataId, String tag) {
        return configClient.getConfigContent(namespaceId, group, dataId, tag);
    }

    @Override
    public Config getConfig(String group, String dataId) {
        return configClient.getConfig(group, dataId);
    }

    @Override
    public Config getConfig(String namespaceId, String group, String dataId) {
        return configClient.getConfig(namespaceId, group, dataId);
    }

    @Override
    public boolean publishConfigContent(String group, String dataId, String content) {
        return configClient.publishConfigContent(group, dataId, content);
    }

    @Override
    public boolean publishConfigContent(String namespaceId, String group, String dataId, String content) {
        return configClient.publishConfigContent(namespaceId, group, dataId, content);
    }

    @Override
    public boolean publishConfigContent(String namespaceId, String group, String dataId, String content, ConfigType configType) {
        return configClient.publishConfigContent(namespaceId, group, dataId, content, configType);
    }

    @Override
    public boolean publishConfigContent(String namespaceId, String group, String dataId, String content, String tag, ConfigType configType) {
        return configClient.publishConfigContent(namespaceId, group, dataId, content, tag, configType);
    }

    @Override
    public boolean publishConfig(NewConfig newConfig) {
        return configClient.publishConfig(newConfig);
    }

    @Override
    public boolean deleteConfig(String dataId) {
        return configClient.deleteConfig(dataId);
    }

    @Override
    public boolean deleteConfig(String group, String dataId) {
        return configClient.deleteConfig(group, dataId);
    }

    @Override
    public boolean deleteConfig(String namespaceId, String group, String dataId) {
        return configClient.deleteConfig(namespaceId, group, dataId);
    }

    @Override
    public boolean deleteConfig(String namespaceId, String group, String dataId, String tag) {
        return configClient.deleteConfig(namespaceId, group, dataId, tag);
    }

    @Override
    public Page<HistoryConfig> getHistoryConfigs(String dataId) {
        return configClient.getHistoryConfigs(dataId);
    }

    @Override
    public Page<HistoryConfig> getHistoryConfigs(String group, String dataId) {
        return configClient.getHistoryConfigs(group, dataId);
    }

    @Override
    public Page<HistoryConfig> getHistoryConfigs(String namespaceId, String group, String dataId) {
        return configClient.getHistoryConfigs(namespaceId, group, dataId);
    }

    @Override
    public Page<HistoryConfig> getHistoryConfigs(String namespaceId, String group, String dataId, int pageNumber, int pageSize) {
        return configClient.getHistoryConfigs(namespaceId, group, dataId, pageNumber, pageSize);
    }

    @Override
    public HistoryConfig getHistoryConfig(String dataId, long revision) {
        return configClient.getHistoryConfig(dataId, revision);
    }

    @Override
    public HistoryConfig getHistoryConfig(String group, String dataId, long revision) {
        return configClient.getHistoryConfig(group, dataId, revision);
    }

    @Override
    public HistoryConfig getHistoryConfig(String namespaceId, String group, String dataId, long revision) {
        return configClient.getHistoryConfig(namespaceId, group, dataId, revision);
    }

    @Override
    public HistoryConfig getPreviousHistoryConfig(String dataId, String id) {
        return configClient.getPreviousHistoryConfig(dataId, id);
    }

    @Override
    public HistoryConfig getPreviousHistoryConfig(String group, String dataId, String id) {
        return configClient.getPreviousHistoryConfig(group, dataId, id);
    }

    @Override
    public HistoryConfig getPreviousHistoryConfig(String namespaceId, String group, String dataId, String id) {
        return configClient.getPreviousHistoryConfig(namespaceId, group, dataId, id);
    }

    @Override
    public void addEventListener(String dataId, ConfigChangedListener listener) {
        configClient.addEventListener(dataId, listener);
    }

    @Override
    public void addEventListener(String group, String dataId, ConfigChangedListener listener) {
        configClient.addEventListener(group, dataId, listener);
    }

    @Override
    public void addEventListener(String namespaceId, String group, String dataId, ConfigChangedListener listener) {
        configClient.addEventListener(namespaceId, group, dataId, listener);
    }

    @Override
    public void removeEventListener(String dataId, ConfigChangedListener listener) {
        configClient.removeEventListener(dataId, listener);
    }

    @Override
    public void removeEventListener(String group, String dataId, ConfigChangedListener listener) {
        configClient.removeEventListener(group, dataId, listener);
    }

    @Override
    public void removeEventListener(String namespaceId, String group, String dataId, ConfigChangedListener listener) {
        configClient.removeEventListener(namespaceId, group, dataId, listener);
    }

    @Override
    public boolean createService(Service service) {
        return serviceClient.createService(service);
    }

    @Override
    public boolean deleteService(String namespaceId, String serviceName) {
        return serviceClient.deleteService(namespaceId, serviceName);
    }

    @Override
    public boolean deleteService(String namespaceId, String groupName, String serviceName) {
        return serviceClient.deleteService(namespaceId, groupName, serviceName);
    }

    @Override
    public boolean updateService(Service service) {
        return serviceClient.updateService(service);
    }

    @Override
    public Service getService(String namespaceId, String serviceName) {
        return serviceClient.getService(namespaceId, serviceName);
    }

    @Override
    public Service getService(String namespaceId, String groupName, String serviceName) {
        return serviceClient.getService(namespaceId, groupName, serviceName);
    }

    @Override
    public Page<String> getServiceNames(String namespaceId) {
        return serviceClient.getServiceNames(namespaceId);
    }

    @Override
    public Page<String> getServiceNames(String namespaceId, int pageNumber) {
        return serviceClient.getServiceNames(namespaceId, pageNumber);
    }

    @Override
    public Page<String> getServiceNames(String namespaceId, int pageNumber, int pageSize) {
        return serviceClient.getServiceNames(namespaceId, pageNumber, pageSize);
    }

    @Override
    public Page<String> getServiceNames(String namespaceId, String groupName) {
        return serviceClient.getServiceNames(namespaceId, groupName);
    }

    @Override
    public Page<String> getServiceNames(String namespaceId, String groupName, int pageNumber) {
        return serviceClient.getServiceNames(namespaceId, groupName, pageNumber);
    }

    @Override
    public Page<String> getServiceNames(String namespaceId, String groupName, int pageNumber, int pageSize) {
        return serviceClient.getServiceNames(namespaceId, groupName, pageNumber, pageSize);
    }

    @Override
    public boolean register(NewInstance newInstance) {
        return instanceClient.register(newInstance);
    }

    @Override
    public boolean deregister(DeleteInstance deleteInstance) {
        return instanceClient.deregister(deleteInstance);
    }

    @Override
    public boolean refresh(UpdateInstance updateInstance) {
        return instanceClient.refresh(updateInstance);
    }

    @Override
    public Instance getInstance(String serviceName, String ip, int port) {
        return instanceClient.getInstance(serviceName, ip, port);
    }

    @Override
    public Instance getInstance(String clusterName, String serviceName, String ip, int port) {
        return instanceClient.getInstance(clusterName, serviceName, ip, port);
    }

    @Override
    public Instance getInstance(String groupName, String clusterName, String serviceName, String ip, int port) {
        return instanceClient.getInstance(groupName, clusterName, serviceName, ip, port);
    }

    @Override
    public Instance getInstance(String namespaceId, String groupName, String clusterName, String serviceName, String ip, int port) {
        return instanceClient.getInstance(namespaceId, groupName, clusterName, serviceName, ip, port);
    }

    @Override
    public Instance getInstance(QueryInstance queryInstance) {
        return instanceClient.getInstance(queryInstance);
    }

    @Override
    public InstancesList getInstancesList(String serviceName) {
        return instanceClient.getInstancesList(serviceName);
    }

    @Override
    public InstancesList getInstancesList(String serviceName, String ip, Integer port) {
        return instanceClient.getInstancesList(serviceName, ip, port);
    }

    @Override
    public InstancesList getInstancesList(String namespaceId, String serviceName) {
        return instanceClient.getInstancesList(namespaceId, serviceName);
    }

    @Override
    public InstancesList getInstancesList(String namespaceId, String serviceName, String ip, Integer port) {
        return instanceClient.getInstancesList(namespaceId, serviceName, ip, port);
    }

    @Override
    public InstancesList getInstancesList(String namespaceId, String groupName, String serviceName) {
        return instanceClient.getInstancesList(namespaceId, groupName, serviceName);
    }

    @Override
    public InstancesList getInstancesList(String namespaceId, String groupName, String serviceName, String ip, Integer port) {
        return instanceClient.getInstancesList(namespaceId, groupName, serviceName, ip, port);
    }

    @Override
    public InstancesList getInstancesList(String namespaceId, String groupName, String clusterName, String serviceName) {
        return instanceClient.getInstancesList(namespaceId, groupName, clusterName, serviceName);
    }

    @Override
    public InstancesList getInstancesList(String namespaceId, String groupName, String clusterName, String serviceName, String ip, Integer port) {
        return instanceClient.getInstancesList(namespaceId, groupName, clusterName, serviceName, ip, port);
    }

    @Override
    public InstancesList getInstancesList(String namespaceId, String groupName, String clusterName, String serviceName, Boolean healthyOnly) {
        return instanceClient.getInstancesList(namespaceId, groupName, clusterName, serviceName, healthyOnly);
    }

    @Override
    public InstancesList getInstancesList(String namespaceId, String groupName, String clusterName, String serviceName, String ip, Integer port, Boolean healthyOnly) {
        return instanceClient.getInstancesList(namespaceId, groupName, clusterName, serviceName, ip, port, healthyOnly);
    }

    @Override
    public InstancesList getInstancesList(String namespaceId, String groupName, String clusterName, String serviceName, Boolean healthyOnly, String app) {
        return instanceClient.getInstancesList(namespaceId, groupName, clusterName, serviceName, healthyOnly, app);
    }

    @Override
    public InstancesList getInstancesList(String namespaceId, String groupName, String clusterName, String serviceName, String ip, Integer port, Boolean healthyOnly, String app) {
        return instanceClient.getInstancesList(namespaceId, groupName, clusterName, serviceName, ip, port, healthyOnly, app);
    }

    @Override
    public Heartbeat sendHeartbeat(Instance instance) {
        return instanceClient.sendHeartbeat(instance);
    }

    @Override
    public boolean updateHealth(UpdateHealthInstance updateHealthInstance) {
        return instanceClient.updateHealth(updateHealthInstance);
    }

    @Override
    public BatchMetadataResult batchUpdateMetadata(Iterable<Instance> instances, Map<String, String> metadata) {
        return instanceClient.batchUpdateMetadata(instances, metadata);
    }

    @Override
    public BatchMetadataResult batchUpdateMetadata(Iterable<Instance> instances, Map<String, String> metadata, ConsistencyType consistencyType) {
        return instanceClient.batchUpdateMetadata(instances, metadata, consistencyType);
    }

    @Override
    public BatchMetadataResult batchDeleteMetadata(Iterable<Instance> instances, Map<String, String> metadata) {
        return instanceClient.batchDeleteMetadata(instances, metadata);
    }

    @Override
    public BatchMetadataResult batchDeleteMetadata(Iterable<Instance> instances, Map<String, String> metadata, ConsistencyType consistencyType) {
        return instanceClient.batchDeleteMetadata(instances, metadata, consistencyType);
    }

    @Override
    public List<Namespace> getAllNamespaces() {
        return namespaceClient.getAllNamespaces();
    }

    @Override
    public Namespace getNamespace(String namespaceId) {
        return namespaceClient.getNamespace(namespaceId);
    }

    @Override
    public boolean createNamespace(String namespaceId, String namespaceName) {
        return namespaceClient.createNamespace(namespaceId, namespaceName);
    }

    @Override
    public boolean createNamespace(String namespaceId, String namespaceName, String namespaceDesc) {
        return namespaceClient.createNamespace(namespaceId, namespaceName, namespaceDesc);
    }

    @Override
    public boolean updateNamespace(String namespaceId, String namespaceName) {
        return namespaceClient.updateNamespace(namespaceId, namespaceName);
    }

    @Override
    public boolean updateNamespace(String namespaceId, String namespaceName, String namespaceDesc) {
        return namespaceClient.updateNamespace(namespaceId, namespaceName, namespaceDesc);
    }

    @Override
    public boolean deleteNamespace(String namespaceId) {
        return namespaceClient.deleteNamespace(namespaceId);
    }

    @Override
    public List<String> getAllClientIds() {
        OpenApiRequest request = OpenApiRequest.Builder.create(CLIENT_LIST_ENDPOINT)
                .method(GET)
                .build();
        return response(request, LinkedList.class);
    }

    @Override
    public ClientDetail getClientDetail(String clientId) {
        OpenApiRequest request = clientRequest(CLIENT_ENDPOINT, clientId);
        return response(request, ClientDetail.class);
    }

    @Override
    public List<ClientInstance> getRegisteredInstances(String clientId) {
        OpenApiRequest request = clientRequest(CLIENT_REGISTERED_INSTANCES_ENDPOINT, clientId);
        return response(request, ofParameterizedType(List.class, ClientInstance.class));
    }

    @Override
    public List<ClientSubscriber> getSubscribers(String clientId) {
        OpenApiRequest request = clientRequest(CLIENT_SUBSCRIBERS_ENDPOINT, clientId);
        return response(request, ofParameterizedType(List.class, ClientSubscriber.class));
    }

    @Override
    public List<ClientInfo> getRegisteredClients(String namespaceId, String groupName, String serviceName,
                                                 ConsistencyType consistencyType, String ip, Integer port) {
        return getClients(REGISTERED_CLIENTS_ENDPOINT, namespaceId, groupName, serviceName, consistencyType, ip, port);
    }

    @Override
    public List<ClientInfo> getSubscribedClients(String namespaceId, String groupName, String serviceName,
                                                 ConsistencyType consistencyType, String ip, Integer port) {
        return getClients(SUBSCRIBED_CLIENTS_ENDPOINT, namespaceId, groupName, serviceName, consistencyType, ip, port);
    }

    private List<ClientInfo> getClients(String endpoint, String namespaceId, String groupName, String serviceName,
                                        ConsistencyType consistencyType, String ip, Integer port) {
        OpenApiRequest request = OpenApiRequest.Builder.create(endpoint)
                .method(GET)
                .queryParameter(NAMESPACE_ID, namespaceId)
                .queryParameter(SERVICE_GROUP_NAME, groupName)
                .queryParameter(SERVICE_NAME, serviceName)
                .queryParameter(INSTANCE_EPHEMERAL, EPHEMERAL.equals(consistencyType))
                .queryParameter(INSTANCE_IP, ip)
                .queryParameter(INSTANCE_PORT, port)
                .build();
        return response(request, ofParameterizedType(List.class, ClientInfo.class));
    }

    private OpenApiRequest clientRequest(String endpoint, String clientId) {
        return OpenApiRequest.Builder.create(endpoint)
                .method(GET)
                .queryParameter(CLIENT_ID, clientId)
                .build();
    }
}
