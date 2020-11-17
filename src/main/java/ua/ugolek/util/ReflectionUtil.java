package ua.ugolek.util;

import lombok.experimental.UtilityClass;

import java.lang.reflect.ParameterizedType;

@UtilityClass
public class ReflectionUtil
{
    public static Class<?> getGenericClass(Class<?> sourceClass, int parameterizedClassIndex) {
        ParameterizedType parameterizedType = (ParameterizedType) sourceClass.getGenericSuperclass();
        return (Class<?>) parameterizedType.getActualTypeArguments()[parameterizedClassIndex];
    }
}
