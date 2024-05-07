package com.demiphea.utils.json;

import com.alibaba.fastjson2.JSONObject;

import java.util.Map;
import java.util.function.Supplier;

/**
 * JsonObjectBuilder
 *
 * @author demiphea
 * @since 17.0.9
 */
public class JsonObjectBuilder {
    private final JSONObject jsonObject;

    public JsonObjectBuilder(JSONObject jsonObject) {
        this.jsonObject = jsonObject;
    }

    public static JsonObjectBuilder create() {
        return new JsonObjectBuilder(new JSONObject());
    }

    public static JsonObjectBuilder create(JSONObject jsonObject) {
        return new JsonObjectBuilder(jsonObject);
    }

    public JsonObjectBuilder put(String key, Object value) {
        this.jsonObject.put(key, value);
        return this;
    }

    public JsonObjectBuilder put(boolean condition, String key, Object value) {
        if (condition) {
            this.put(key, value);
        }
        return this;
    }

    public JsonObjectBuilder put(boolean condition, String key, Supplier<Object> supplier) {
        if (condition) {
            this.put(key, supplier.get());
        }
        return this;
    }

    public JsonObjectBuilder putAll(Map<String, Object> map) {
        this.jsonObject.putAll(map);
        return this;
    }

    public JsonObjectBuilder putAll(JSONObject jsonObject) {
        this.jsonObject.putAll(jsonObject);
        return this;
    }

    public JsonObjectBuilder clear() {
        this.jsonObject.clear();
        return this;
    }

    public JSONObject jsonObject() {
        return this.jsonObject;
    }

    public JSONObject build() {
        return this.jsonObject();
    }

}
