package com.crawler.utils;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;

public class JsonUtils {

    private static final Logger logger = LoggerFactory.getLogger(JsonUtils.class);
    public static final ObjectMapper objectMapper = new ObjectMapper();

    public JsonUtils() {
    }

    public static ObjectMapper getObjectMapper() {
        return objectMapper;
    }

    public static String writeValueAsString(Object obj) {
        if (obj == null) {
            return "";
        } else {
            try {
                return objectMapper.writeValueAsString(obj);
            } catch (IOException var2) {
                logger.warn("{}, {}", var2.getMessage(), var2);
                return "";
            }
        }
    }

    public static <T> T readValue(File file, TypeReference<T> typeReference) {
        if (file == null) {
            return null;
        } else {
            try {
                return objectMapper.readValue(file, typeReference);
            } catch (IOException var3) {
                logger.warn(var3.getMessage(), var3);
                return null;
            }
        }
    }

    public static <T> T readValue(String json, TypeReference<T> typeReference) {
        if (StringUtils.isEmpty(json)) {
            return null;
        } else {
            try {
                return objectMapper.readValue(json, typeReference);
            } catch (IOException var3) {
                logger.warn(var3.getMessage(), var3);
                return null;
            }
        }
    }

    public static <T> T readValue(String json, Class<T> clazz) {
        if (StringUtils.isEmpty(json)) {
            return null;
        } else {
            try {
                return objectMapper.readValue(json, clazz);
            } catch (IOException var3) {
                logger.warn("{}, {}", var3.getMessage(), var3);
                return null;
            }
        }
    }

    public static <T> T convertValue(Object object, TypeReference<T> typeReference) {
        return object == null ? null : objectMapper.convertValue(object, typeReference);
    }

    static {
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
    }
}

