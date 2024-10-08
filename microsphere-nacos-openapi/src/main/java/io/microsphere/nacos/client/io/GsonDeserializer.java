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
package io.microsphere.nacos.client.io;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;
import java.util.function.Function;

/**
 * The Gson's {@link JsonDeserializer} abstract class provides the template method
 *
 * @param <T> type for which the deserializer is being registered.
 *            It is possible that a deserializer may be asked to deserialize a specific generic type of the T.
 * @author <a href="mailto:mercyblitz@gmail.com">Mercy<a/>
 * @see JsonDeserializer
 * @since 1.0.0
 */
public abstract class GsonDeserializer<T> implements JsonDeserializer<T> {

    private Deserializer deserializer;

    @Override
    public T deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        T result = deserialize(json, typeOfT);
        return result == null ? context.deserialize(json, typeOfT) : result;
    }

    /**
     * Deserialize method should be override by the sub-class
     *
     * @param json    The Json data being deserialized
     * @param typeOfT The type of the Object to deserialize to
     * @return a deserialized object of the specified type typeOfT which is a subclass of {@code T}
     * @throws JsonParseException if json is not in the expected format of {@code typeofT}
     */
    protected T deserialize(JsonElement json, Type typeOfT) throws JsonParseException {
        return null;
    }

    /**
     * Get the {@link Deserializer}
     *
     * @return non-null
     */
    protected Deserializer getDeserializer() {
        return deserializer;
    }

    /**
     * Set the {@link Deserializer}
     *
     * @param deserializer {@link Deserializer}
     */
    void setDeserializer(Deserializer deserializer) {
        this.deserializer = deserializer;
    }

    /**
     * Get the {@link Boolean} value from the {@link JsonObject} by field name
     *
     * @param jsonObject {@link JsonObject}
     * @param memberName the member name
     * @return the field value as {@link Boolean} if found,or <code>null</code>
     */
    protected Boolean getBoolean(JsonObject jsonObject, String memberName, String... otherMemberNames) {
        return getFieldValue(jsonObject, memberName, otherMemberNames, JsonElement::getAsBoolean);
    }

    /**
     * Get the {@link Integer} value from the {@link JsonObject} by field name
     *
     * @param jsonObject {@link JsonObject}
     * @param memberName the member name
     * @return the field value as {@link Integer} if found,or <code>null</code>
     */
    protected Integer getInteger(JsonObject jsonObject, String memberName, String... otherMemberNames) {
        return getFieldValue(jsonObject, memberName, otherMemberNames, JsonElement::getAsInt);
    }

    /**
     * Get the {@link Long} value from the {@link JsonObject} by field name
     *
     * @param jsonObject {@link JsonObject}
     * @param memberName the member name
     * @return the field value as {@link Long} if found,or <code>null</code>
     */
    protected Long getLong(JsonObject jsonObject, String memberName, String... otherMemberNames) {
        return getFieldValue(jsonObject, memberName, otherMemberNames, JsonElement::getAsLong);
    }

    /**
     * Get the {@link Float} value from the {@link JsonObject} by field name
     *
     * @param jsonObject {@link JsonObject}
     * @param memberName the member name
     * @return the field value as {@link Float} if found,or <code>null</code>
     */
    protected Float getFloat(JsonObject jsonObject, String memberName, String... otherMemberNames) {
        return getFieldValue(jsonObject, memberName, otherMemberNames, JsonElement::getAsFloat);
    }

    /**
     * Get the string value from the {@link JsonObject} by field name
     *
     * @param jsonObject {@link JsonObject}
     * @param memberName the member name
     * @return the field value as {@link String} if found,or <code>null</code>
     */
    protected String getString(JsonObject jsonObject, String memberName, String... otherMemberNames) {
        return getFieldValue(jsonObject, memberName, otherMemberNames, JsonElement::getAsString);
    }

    protected <T> T getFieldValue(JsonObject jsonObject, String memberName, String[] otherMemberNames, Function<JsonElement, T> asTypeFunction) {
        JsonElement fieldElement = get(jsonObject, memberName);
        if (fieldElement == null) {
            for (String otherMemberName : otherMemberNames) {
                fieldElement = get(jsonObject, otherMemberName);
                if (fieldElement != null) {
                    break;
                }
            }
        }
        if (fieldElement == null) {
            return null;
        }
        return asTypeFunction.apply(fieldElement);
    }

    private JsonElement get(JsonObject jsonObject, String memberName) {
        JsonElement fieldElement = jsonObject.get(memberName);
        if (fieldElement == null || fieldElement instanceof JsonNull) {
            return null;
        }
        return fieldElement;
    }
}
