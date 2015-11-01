package com.tapwisdom.core.common.util;

import com.google.gson.Gson;

import java.lang.reflect.Type;

/**
 * Created by srividyak on 26/07/15.
 */
public class Utils {

    private static final Gson gson = new Gson();

    public static <T> T getObjectFromString(String json, Class<T> clazz) {
        return gson.fromJson(json, clazz);
    }

    public static <T> String getStringFromObject(T o) {
        return gson.toJson(o);
    }

    public static <T> T getObjectFromString(String json, Type type) {
        return gson.fromJson(json, type);
    }
    
}
