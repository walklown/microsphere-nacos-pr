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
package io.microsphere.nacos.client.common.discovery;

import io.microsphere.nacos.client.Client;
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
import io.microsphere.nacos.client.common.namespace.model.Namespace;
import io.microsphere.nacos.client.constants.Constants;

import java.util.Map;

import static io.microsphere.nacos.client.common.discovery.ConsistencyType.EPHEMERAL;
import static io.microsphere.nacos.client.constants.Constants.DEFAULT_APPLICATION_NAME;
import static io.microsphere.nacos.client.constants.Constants.DEFAULT_CLUSTER_NAME;
import static io.microsphere.nacos.client.constants.Constants.DEFAULT_GROUP_NAME;
import static io.microsphere.nacos.client.constants.Constants.DEFAULT_HEALTHY_ONLY;
import static io.microsphere.nacos.client.constants.Constants.DEFAULT_NAMESPACE_ID;

/**
 * The Client for Nacos {@link Service} {@link Instance}
 *
 * @author <a href="mailto:mercyblitz@gmail.com">Mercy<a/>
 * @see InstancesList
 * @see Instance
 * @see Service
 * @see ServiceClient
 * @since 1.0.0
 */
public interface InstanceClient extends Client {

    /**
     * Register {@link NewInstance a new instance} with parameters :
     * <table>
     * <thead>
     * <tr>
     * <th>Name</th>
     * <th>Type</th>
     * <th>Required</th>
     * <th>Description</th>
     * </tr>
     * </thead>
     * <tbody>
     * <tr>
     * <td>ip</td>
     * <td>String</td>
     * <td>yes</td>
     * <td>IP of instance</td>
     * </tr>
     * <tr>
     * <td>port</td>
     * <td>int</td>
     * <td>yes</td>
     * <td>Port of instance</td>
     * </tr>
     * <tr>
     * <td>namespaceId</td>
     * <td>String</td>
     * <td>no</td>
     * <td>ID of namespace</td>
     * </tr>
     * <tr>
     * <td>weight</td>
     * <td>double</td>
     * <td>no</td>
     * <td>Weight</td>
     * </tr>
     * <tr>
     * <td>enabled</td>
     * <td>boolean</td>
     * <td>no</td>
     * <td>enabled or not</td>
     * </tr>
     * <tr>
     * <td>healthy</td>
     * <td>boolean</td>
     * <td>no</td>
     * <td>healthy or not</td>
     * </tr>
     * <tr>
     * <td>metadata</td>
     * <td>String</td>
     * <td>no</td>
     * <td>extended information</td>
     * </tr>
     * <tr>
     * <td>clusterName</td>
     * <td>String</td>
     * <td>no</td>
     * <td>cluster name</td>
     * </tr>
     * <tr>
     * <td>serviceName</td>
     * <td>String</td>
     * <td>yes</td>
     * <td>service name</td>
     * </tr>
     * <tr>
     * <td>groupName</td>
     * <td>String</td>
     * <td>no</td>
     * <td>group name</td>
     * </tr>
     * <tr>
     * <td>ephemeral</td>
     * <td>boolean</td>
     * <td>no</td>
     * <td>if instance is ephemeral</td>
     * </tr>
     * </tbody>
     * </table>
     *
     * @param newInstance {@link NewInstance a new instance}
     * @return <code>true</code> if register successfully, otherwise <code>false</code>
     */
    boolean register(NewInstance newInstance);

    /**
     * Deregister a {@link DeleteInstance} with parameters :
     * <table>
     * <thead>
     * <tr>
     * <th>Name</th>
     * <th>Type</th>
     * <th>Required</th>
     * <th>Description</th>
     * </tr>
     * </thead>
     * <tbody>
     * <tr>
     * <td>serviceName</td>
     * <td>String</td>
     * <td>yes</td>
     * <td>Service name</td>
     * </tr>
     * <tr>
     * <td>groupName</td>
     * <td>String</td>
     * <td>no</td>
     * <td>group name</td>
     * </tr>
     * <tr>
     * <td>ephemeral</td>
     * <td>boolean</td>
     * <td>no</td>
     * <td>if instance is ephemeral</td>
     * </tr>
     * <tr>
     * <td>ip</td>
     * <td>String</td>
     * <td>yes</td>
     * <td>IP of instance</td>
     * </tr>
     * <tr>
     * <td>port</td>
     * <td>int</td>
     * <td>yes</td>
     * <td>Port of instance</td>
     * </tr>
     * <tr>
     * <td>clusterName</td>
     * <td>String</td>
     * <td>no</td>
     * <td>Cluster name</td>
     * </tr>
     * <tr>
     * <td>namespaceId</td>
     * <td>String</td>
     * <td>no</td>
     * <td>ID of namespace</td>
     * </tr>
     * </tbody>
     * </table>
     *
     * @param deleteInstance {@link DeleteInstance an instance to be deregistered}
     * @return <code>true</code> if deregister successfully, otherwise <code>false</code>
     */
    boolean deregister(DeleteInstance deleteInstance);

    /**
     * Refresh the registered {@link UpdateInstance} with parameters:
     * <table>
     * <thead>
     * <tr>
     * <th>Name</th>
     * <th>Type</th>
     * <th>Required</th>
     * <th>Description</th>
     * </tr>
     * </thead>
     * <tbody>
     * <tr>
     * <td>serviceName</td>
     * <td>String</td>
     * <td>yes</td>
     * <td>Service name</td>
     * </tr>
     * <tr>
     * <td>groupName</td>
     * <td>String</td>
     * <td>no</td>
     * <td>group name</td>
     * </tr>
     * <tr>
     * <td>ephemeral</td>
     * <td>boolean</td>
     * <td>no</td>
     * <td>if instance is ephemeral</td>
     * </tr>
     * <tr>
     * <td>ip</td>
     * <td>String</td>
     * <td>yes</td>
     * <td>IP of instance</td>
     * </tr>
     * <tr>
     * <td>port</td>
     * <td>int</td>
     * <td>yes</td>
     * <td>Port of instance</td>
     * </tr>
     * <tr>
     * <td>clusterName</td>
     * <td>String</td>
     * <td>no</td>
     * <td>Cluster name</td>
     * </tr>
     * <tr>
     * <td>namespaceId</td>
     * <td>String</td>
     * <td>no</td>
     * <td>ID of namespace</td>
     * </tr>
     * <tr>
     * <td>weight</td>
     * <td>double</td>
     * <td>no</td>
     * <td>Weight</td>
     * </tr>
     * <tr>
     * <td>enabled</td>
     * <td>boolean</td>
     * <td>no</td>
     * <td>If enabled</td>
     * </tr>
     * <tr>
     * <td>metadata</td>
     * <td>JSON</td>
     * <td>no</td>
     * <td>Extended information</td>
     * </tr>
     * </tbody>
     * </table>
     *
     * @param updateInstance {@link UpdateInstance an instance to be refreshed}
     * @return <code>true</code> if refresh successfully, otherwise <code>false</code>
     */
    boolean refresh(UpdateInstance updateInstance);

    /**
     * Get the {@link Instance} for the {@link Constants#DEFAULT_NAMESPACE_ID "public" namespace},
     * {@link Constants#DEFAULT_GROUP_NAME "DEFAULT_GROUP"} and the {@link Constants#DEFAULT_CLUSTER_NAME "DEFAULT" cluster}
     * by {@code serviceName}, {@code ip} and {@code port}
     *
     * @param serviceName the name of {@link Service}
     * @param ip          the IP of instance
     * @param port        the port of instance
     * @return non-null {@link Instance}
     */
    default Instance getInstance(String serviceName, String ip, int port) {
        return getInstance(DEFAULT_CLUSTER_NAME, serviceName, ip, port);
    }

    /**
     * Get the {@link Instance} for the {@link Constants#DEFAULT_NAMESPACE_ID "public" namespace} and
     * {@link Constants#DEFAULT_GROUP_NAME "DEFAULT_GROUP"} by {@code clusterName}, {@code serviceName}, {@code ip} and {@code port}
     *
     * @param clusterName (optional) the name of cluster, if not specified, the {@link Constants#DEFAULT_CLUSTER_NAME "DEFAULT" cluster} will be used.
     * @param serviceName the name of {@link Service}.
     * @param ip          the IP of instance
     * @param port        the port of instance
     * @return non-null {@link Instance}
     */
    default Instance getInstance(String clusterName, String serviceName, String ip, int port) {
        return getInstance(DEFAULT_GROUP_NAME, clusterName, serviceName, ip, port);
    }

    /**
     * Get the {@link Instance} for the {@link Constants#DEFAULT_NAMESPACE_ID "public" namespace} by
     * {@code groupName}, {@code clusterName}, {@code serviceName}, {@code ip} and {@code port}
     *
     * @param groupName   (optional) the name of group, if not specified, the {@link Constants#DEFAULT_GROUP_NAME "DEFAULT_GROUP"} will be used.
     * @param clusterName (optional) the name of cluster, if not specified, the {@link Constants#DEFAULT_CLUSTER_NAME "DEFAULT" cluster} will be used.
     * @param serviceName the name of {@link Service}.
     * @param ip          the IP of instance
     * @param port        the port of instance
     * @return non-null {@link Instance}
     */
    default Instance getInstance(String groupName, String clusterName, String serviceName, String ip, int port) {
        return getInstance(DEFAULT_NAMESPACE_ID, groupName, clusterName, serviceName, ip, port);
    }

    /**
     * Get the {@link Instance} by the specified {@code namespaceId}, {@code groupName}, {@code clusterName},
     * {@code serviceName}, {@code ip} and {@code port}
     *
     * @param namespaceId (optional) {@link Namespace#getNamespaceId() the id of namespace}, if not specified,
     *                    the {@link Constants#DEFAULT_NAMESPACE_ID "public" namespace} will be used.
     * @param groupName   (optional) the name of group, if not specified, the {@link Constants#DEFAULT_GROUP_NAME "DEFAULT_GROUP"} will be used.
     * @param clusterName (optional) the name of cluster, if not specified, the {@link Constants#DEFAULT_CLUSTER_NAME "DEFAULT" cluster} will be used.
     * @param serviceName the name of {@link Service}.
     * @param ip          the IP of instance
     * @param port        the port of instance
     * @return non-null {@link Instance}
     */
    default Instance getInstance(String namespaceId, String groupName, String clusterName, String serviceName, String ip, int port) {
        QueryInstance queryInstance = new QueryInstance();
        queryInstance.setNamespaceId(namespaceId);
        queryInstance.setGroupName(groupName);
        queryInstance.setClusterName(clusterName);
        queryInstance.setServiceName(serviceName);
        queryInstance.setIp(ip);
        queryInstance.setPort(port);
        return getInstance(queryInstance);
    }

    /**
     * Get the {@link Instance} of the specified {@link QueryInstance} with parameters :
     * <table>
     * <thead>
     * <tr>
     * <th>Name</th>
     * <th>Type</th>
     * <th>Required</th>
     * <th>Description</th>
     * </tr>
     * </thead>
     * <tbody>
     * <tr>
     * <td>namespaceId</td>
     * <td>String</td>
     * <td>no</td>
     * <td>ID of namespace</td>
     * </tr>
     * <tr>
     * <td>serviceName</td>
     * <td>String</td>
     * <td>yes</td>
     * <td>Service name</td>
     * </tr>
     * <tr>
     * <td>groupName</td>
     * <td>String</td>
     * <td>no</td>
     * <td>group name</td>
     * </tr>
     * <tr>
     * <td>ephemeral</td>
     * <td>boolean</td>
     * <td>no</td>
     * <td>if instance is ephemeral</td>
     * </tr>
     * <tr>
     * <td>ip</td>
     * <td>String</td>
     * <td>yes</td>
     * <td>IP of instance</td>
     * </tr>
     * <tr>
     * <td>port</td>
     * <td>String</td>
     * <td>yes</td>
     * <td>Port of instance</td>
     * </tr>
     * <tr>
     * <td>cluster</td>
     * <td>String</td>
     * <td>no</td>
     * <td>Cluster name</td>
     * </tr>
     * </tbody>
     * </table>
     *
     * @param queryInstance {@link QueryInstance}
     * @return non-null {@link Instance}
     */
    Instance getInstance(QueryInstance queryInstance);

    /**
     * Get the {@link InstancesList} of the specified {@code serviceName}
     *
     * @param serviceName the name of {@link Service}.
     * @return non-null {@link InstancesList}
     */
    default InstancesList getInstancesList(String serviceName) {
        return getInstancesList(serviceName, (String) null, (Integer) null);
    }

    /**
     * Get the {@link InstancesList} of the specified {@code serviceName}, {@code ip} and {@code port}
     *
     * @param serviceName the name of {@link Service}.
     * @param ip          (optional) the IP of instance.
     * @param port        (optional) the port of instance.
     * @return non-null {@link InstancesList}
     */
    default InstancesList getInstancesList(String serviceName, String ip, Integer port) {
        return getInstancesList(DEFAULT_NAMESPACE_ID, serviceName, ip, port);
    }

    /**
     * Get the {@link InstancesList} of the specified {@code namespaceId} and {@code serviceName}
     *
     * @param namespaceId (optional) {@link Namespace#getNamespaceId() the id of namespace}, if not specified,
     *                    the {@link Constants#DEFAULT_NAMESPACE_ID "public" namespace} will be used.
     * @param serviceName the name of {@link Service}.
     * @return non-null {@link InstancesList}
     */
    default InstancesList getInstancesList(String namespaceId, String serviceName) {
        return getInstancesList(namespaceId, serviceName, null, (Integer) null);
    }

    /**
     * Get the {@link InstancesList} of the specified {@code namespaceId}, {@code serviceName}, {@code ip} and {@code port}
     *
     * @param namespaceId (optional) {@link Namespace#getNamespaceId() the id of namespace}, if not specified,
     *                    the {@link Constants#DEFAULT_NAMESPACE_ID "public" namespace} will be used.
     * @param serviceName the name of {@link Service}.
     * @param ip          (optional) the IP of instance.
     * @param port        (optional) the port of instance.
     * @return non-null {@link InstancesList}
     */
    default InstancesList getInstancesList(String namespaceId, String serviceName, String ip, Integer port) {
        return getInstancesList(namespaceId, DEFAULT_GROUP_NAME, serviceName, ip, port);
    }

    /**
     * Get the {@link InstancesList} of the specified {@code namespaceId}, {@code groupName} and {@code serviceName}
     *
     * @param namespaceId (optional) {@link Namespace#getNamespaceId() the id of namespace}, if not specified,
     *                    the {@link Constants#DEFAULT_NAMESPACE_ID "public" namespace} will be used.
     * @param groupName   (optional) the name of group, if not specified, the {@link Constants#DEFAULT_GROUP_NAME "DEFAULT_GROUP"} will be used.
     * @param serviceName the name of {@link Service}.
     * @return non-null {@link InstancesList}
     */
    default InstancesList getInstancesList(String namespaceId, String groupName, String serviceName) {
        return getInstancesList(namespaceId, groupName, serviceName, null, (Integer) null);
    }

    /**
     * Get the {@link InstancesList} of the specified {@code namespaceId}, {@code groupName}, {@code serviceName}, {@code ip} and {@code port}
     *
     * @param namespaceId (optional) {@link Namespace#getNamespaceId() the id of namespace}, if not specified,
     *                    the {@link Constants#DEFAULT_NAMESPACE_ID "public" namespace} will be used.
     * @param groupName   (optional) the name of group, if not specified, the {@link Constants#DEFAULT_GROUP_NAME "DEFAULT_GROUP"} will be used.
     * @param serviceName the name of {@link Service}.
     * @param ip          (optional) the IP of instance.
     * @param port        (optional) the port of instance.
     * @return non-null {@link InstancesList}
     */
    default InstancesList getInstancesList(String namespaceId, String groupName, String serviceName, String ip, Integer port) {
        return getInstancesList(namespaceId, groupName, DEFAULT_CLUSTER_NAME, serviceName, ip, port);
    }

    /**
     * Get the {@link InstancesList} of the specified {@code namespaceId}, {@code groupName}, {@code clusterName} and
     * {@code serviceName}
     *
     * @param namespaceId (optional) {@link Namespace#getNamespaceId() the id of namespace}, if not specified,
     *                    the {@link Constants#DEFAULT_NAMESPACE_ID "public" namespace} will be used.
     * @param groupName   (optional) the name of group, if not specified, the {@link Constants#DEFAULT_GROUP_NAME "DEFAULT_GROUP"} will be used.
     * @param clusterName (optional) the name of cluster, if not specified, the {@link Constants#DEFAULT_CLUSTER_NAME "DEFAULT" cluster} will be used.
     * @param serviceName the name of {@link Service}.
     * @return non-null {@link InstancesList}
     */
    default InstancesList getInstancesList(String namespaceId, String groupName, String clusterName, String serviceName) {
        return getInstancesList(namespaceId, groupName, clusterName, serviceName, (Boolean) null);
    }

    /**
     * Get the {@link InstancesList} of the specified {@code namespaceId}, {@code groupName}, {@code clusterName},
     * {@code serviceName}, {@code ip} and {@code port}
     *
     * @param namespaceId (optional) {@link Namespace#getNamespaceId() the id of namespace}, if not specified,
     *                    the {@link Constants#DEFAULT_NAMESPACE_ID "public" namespace} will be used.
     * @param groupName   (optional) the name of group, if not specified, the {@link Constants#DEFAULT_GROUP_NAME "DEFAULT_GROUP"} will be used.
     * @param clusterName (optional) the name of cluster, if not specified, the {@link Constants#DEFAULT_CLUSTER_NAME "DEFAULT" cluster} will be used.
     * @param serviceName the name of {@link Service}.
     * @param ip          (optional) the IP of instance.
     * @param port        (optional) the port of instance.
     * @return non-null {@link InstancesList}
     */
    default InstancesList getInstancesList(String namespaceId, String groupName, String clusterName, String serviceName,
                                           String ip, Integer port) {
        return getInstancesList(namespaceId, groupName, clusterName, serviceName, ip, port, DEFAULT_HEALTHY_ONLY);
    }

    /**
     * Get the {@link InstancesList} of the specified {@code namespaceId}, {@code groupName}, {@code clusterName},
     * {@code serviceName} and {@code healthyOnly}
     *
     * @param namespaceId (optional) {@link Namespace#getNamespaceId() the id of namespace}, if not specified,
     *                    the {@link Constants#DEFAULT_NAMESPACE_ID "public" namespace} will be used.
     * @param groupName   (optional) the name of group, if not specified, the {@link Constants#DEFAULT_GROUP_NAME "DEFAULT_GROUP"} will be used.
     * @param clusterName (optional) the name of cluster, if not specified, the {@link Constants#DEFAULT_CLUSTER_NAME "DEFAULT" cluster} will be used.
     * @param serviceName the name of {@link Service}.
     * @param healthyOnly (optional) the healthy only, if not specified, {@link Constants#DEFAULT_HEALTHY_ONLY false} will be used
     * @return non-null {@link InstancesList}
     */
    default InstancesList getInstancesList(String namespaceId, String groupName, String clusterName, String serviceName,
                                           Boolean healthyOnly) {
        return getInstancesList(namespaceId, groupName, clusterName, serviceName, healthyOnly, DEFAULT_APPLICATION_NAME);
    }

    /**
     * Get the {@link InstancesList} of the specified {@code namespaceId}, {@code groupName}, {@code clusterName},
     * {@code serviceName}, {@code ip}, {@code port} and {@code healthyOnly}
     *
     * @param namespaceId (optional) {@link Namespace#getNamespaceId() the id of namespace}, if not specified,
     *                    the {@link Constants#DEFAULT_NAMESPACE_ID "public" namespace} will be used.
     * @param groupName   (optional) the name of group, if not specified, the {@link Constants#DEFAULT_GROUP_NAME "DEFAULT_GROUP"} will be used.
     * @param clusterName (optional) the name of cluster, if not specified, the {@link Constants#DEFAULT_CLUSTER_NAME "DEFAULT" cluster} will be used.
     * @param serviceName the name of {@link Service}.
     * @param ip          (optional) the IP of instance.
     * @param port        (optional) the port of instance.
     * @param healthyOnly (optional) the healthy only, if not specified, {@link Constants#DEFAULT_HEALTHY_ONLY false} will be used
     * @return non-null {@link InstancesList}
     */
    default InstancesList getInstancesList(String namespaceId, String groupName, String clusterName, String serviceName,
                                           String ip, Integer port, Boolean healthyOnly) {
        return getInstancesList(namespaceId, groupName, clusterName, serviceName, ip, port, healthyOnly, DEFAULT_APPLICATION_NAME);
    }

    /**
     * Get the {@link InstancesList} of the specified {@code namespaceId}, {@code groupName}, {@code clusterName},
     * {@code serviceName} and {@code healthyOnly}
     *
     * @param namespaceId (optional) {@link Namespace#getNamespaceId() the id of namespace}, if not specified,
     *                    the {@link Constants#DEFAULT_NAMESPACE_ID "public" namespace} will be used.
     * @param groupName   (optional) the name of group, if not specified, the {@link Constants#DEFAULT_GROUP_NAME "DEFAULT_GROUP"} will be used.
     * @param clusterName (optional) the name of cluster, if not specified, the {@link Constants#DEFAULT_CLUSTER_NAME "DEFAULT" cluster} will be used.
     * @param serviceName the name of {@link Service}.
     * @param healthyOnly (optional) the healthy only, if not specified, {@link Constants#DEFAULT_HEALTHY_ONLY false} will be used
     * @param app         (optional) the app that calls this method, if not specified,
     *                    the {@link Constants#DEFAULT_APPLICATION_NAME "microsphere-nacos-client"} will be used.
     * @return non-null {@link InstancesList}
     */
    default InstancesList getInstancesList(String namespaceId, String groupName, String clusterName, String serviceName,
                                           Boolean healthyOnly, String app) {
        return getInstancesList(namespaceId, groupName, clusterName, serviceName, null, null, healthyOnly, app);
    }

    /**
     * Get the {@link InstancesList} of the specified {@code namespaceId}, {@code groupName}, {@code clusterName},
     * {@code serviceName}, {@code ip}, {@code port}, {@code healthyOnly} and {@code app}
     *
     * @param namespaceId (optional) {@link Namespace#getNamespaceId() the id of namespace}, if not specified,
     *                    the {@link Constants#DEFAULT_NAMESPACE_ID "public" namespace} will be used.
     * @param groupName   (optional) the name of group, if not specified, the {@link Constants#DEFAULT_GROUP_NAME "DEFAULT_GROUP"} will be used.
     * @param clusterName (optional) the name of cluster, if not specified, the {@link Constants#DEFAULT_CLUSTER_NAME "DEFAULT" cluster} will be used.
     * @param serviceName the name of {@link Service}.
     * @param ip          (optional) the IP of instance.
     * @param port        (optional) the port of instance.
     * @param healthyOnly (optional) the healthy only, if not specified, {@link Constants#DEFAULT_HEALTHY_ONLY false} will be used
     * @param app         (optional) the app that calls this method, if not specified,
     *                    the {@link Constants#DEFAULT_APPLICATION_NAME "microsphere-nacos-client"} will be used.
     * @return non-null {@link InstancesList}
     */
    InstancesList getInstancesList(String namespaceId, String groupName, String clusterName, String serviceName,
                                   String ip, Integer port, Boolean healthyOnly, String app);

    /**
     * Send {@link Instance Instance's} Heartbeat to Nacos Server
     *
     * @param instance {@link Instance}
     * @return {@link Heartbeat} instance
     */
    Heartbeat sendHeartbeat(Instance instance);

    /**
     * {@link UpdateHealthInstance Update Instances' Health}
     *
     * @param updateHealthInstance {@link UpdateHealthInstance}
     * @return <code>true</code> if update successfully, otherwise <code>false</code>
     */
    boolean updateHealth(UpdateHealthInstance updateHealthInstance);

    /**
     * Batch Update Service Instances' Metadata
     *
     * @param instances one or more {@link Instance instances}
     * @param metadata  Service Instances' Metadata
     * @return non-null {@link BatchMetadataResult}
     * @since Nacos 1.4.x (Beta)
     */
    default BatchMetadataResult batchUpdateMetadata(Iterable<Instance> instances, Map<String, String> metadata) {
        return batchUpdateMetadata(instances, metadata, EPHEMERAL);
    }

    /**
     * Batch Update Service Instances' Metadata
     *
     * @param instances       one or more {@link Instance instances}
     * @param metadata        Service Instances' Metadata
     * @param consistencyType {@link ConsistencyType}
     * @return non-null  {@link BatchMetadataResult}
     * @since Nacos 1.4.x (Beta)
     */
    BatchMetadataResult batchUpdateMetadata(Iterable<Instance> instances, Map<String, String> metadata, ConsistencyType consistencyType);

    /**
     * Batch Delete Service Instances' Metadata
     *
     * @param instances one or more {@link Instance instances}
     * @param metadata  Service Instances' Metadata
     * @return non-null {@link BatchMetadataResult}
     * @since Nacos 1.4.x (Beta)
     */
    default BatchMetadataResult batchDeleteMetadata(Iterable<Instance> instances, Map<String, String> metadata) {
        return batchDeleteMetadata(instances, metadata, EPHEMERAL);
    }

    /**
     * Batch Delete Service Instances' Metadata
     *
     * @param instances       one or more {@link Instance instances}
     * @param metadata        Service Instances' Metadata
     * @param consistencyType {@link ConsistencyType}
     * @return non-null {@link BatchMetadataResult}
     * @since Nacos 1.4.x (Beta)
     */
    BatchMetadataResult batchDeleteMetadata(Iterable<Instance> instances, Map<String, String> metadata, ConsistencyType consistencyType);
}
