package com.example.myapplication;

import java.lang.reflect.Field;

public class PropertyCopier {
    public static <T, U> void copyProperties(T source, U target) throws IllegalAccessException {
        Class<?> sourceClass = source.getClass();
        Class<?> targetClass = target.getClass();

        Field[] sourceFields = sourceClass.getDeclaredFields();

        for (Field sourceField : sourceFields) {
            sourceField.setAccessible(true);
            Object fieldValue = sourceField.get(source);
            Field targetField = null;
            try {
                targetField = targetClass.getDeclaredField(sourceField.getName());
                targetField.setAccessible(true);
                targetField.set(target, fieldValue);
            } catch (NoSuchFieldException e) {
                // ignore fields that don't exist in target
            }
        }
    }
}
