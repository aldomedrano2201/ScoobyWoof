package com.example.myapplication;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

public class PropertyExtractor {
    public static Map<String, Object> extractProperties(Object value) throws IllegalAccessException {
        Map<String, Object> properties = new HashMap<>();
        if (value != null) {
            Class<?> resultClass = value.getClass();
            Field[] fields = resultClass.getDeclaredFields();

            for (Field field : fields) {
                field.setAccessible(true);
                String fieldName = field.getName();
                String fieldValue = field.get(value).toString();
                properties.put(fieldName, fieldValue);

            }
        }

        return properties;
    }
}

