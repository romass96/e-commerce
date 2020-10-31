package ua.ugolek.util;

import java.lang.reflect.ParameterizedType;

public class ReflectionUtil
{
    public static Class<?> getGenericClass(Class<?> sourceClass) {
        ParameterizedType parameterizedType = (ParameterizedType) sourceClass.getGenericSuperclass();
        return (Class<?>) parameterizedType.getActualTypeArguments()[0];
    }
}
