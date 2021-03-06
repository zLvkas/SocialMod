package de.socialmod.addon.utils;

import java.lang.reflect.Field;

public class ReflectionUtils {

    private static final Field MODIFIERS_FIELD;

    static {
        try {
            MODIFIERS_FIELD = Field.class.getDeclaredField("modifiers");
            MODIFIERS_FIELD.setAccessible(true);
        } catch (NoSuchFieldException exception) {
            // unreachable code
            throw new RuntimeException(exception);
        }
    }

    private ReflectionUtils() {
        throw new UnsupportedOperationException();
    }

    public static Field getFieldByName(Class<?> clazz, String fieldName) {
        try {
            return clazz.getDeclaredField(fieldName);
        } catch (NoSuchFieldException exception) {
            return null;
        }
    }

    public static <T> T get(Class<T> unused, Class<?> source, Object instance, String... fieldNames) {
        for (String fieldName : fieldNames) {
            Field field = getFieldByName(source, fieldName);
            if (field != null) {
                return get(unused, field, instance);
            }
        }

        return null;
    }

    @SuppressWarnings("unchecked")
    public static <T> T get(Class<T> unused, Field field, Object instance) {
        try {
            if (field.isAccessible()) {
                return (T) field.get(instance);
            }

            field.setAccessible(true);
            return (T) field.get(instance);
        } catch (IllegalAccessException exception) {
            return null;
        }
    }

}