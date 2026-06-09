package org.example;

import java.lang.reflect.Field;

public class NumberValidator {
    public static void validateNumber(Object obj) throws IllegalAccessException, IllegalArgumentException {
        Class<?> classOfObj = obj.getClass();
        for (Field field : classOfObj.getDeclaredFields()) {
            field.setAccessible(true);
            if (field.isAnnotationPresent(NaturalNumber.class)) {
                if (!field.getType().equals(int.class) && !field.getType().equals(long.class) && !field.getType().equals(short.class)
                        && !field.getType().equals(byte.class) && !Number.class.isAssignableFrom(field.getType())) {
                    throw new IllegalArgumentException(field.getName() + " is not an integer number type");
                }
                if (field.getLong(obj) < 0) {
                    throw new IllegalArgumentException(field.getName() + " is not a natural number");
                }
            }
        }
    }
}
