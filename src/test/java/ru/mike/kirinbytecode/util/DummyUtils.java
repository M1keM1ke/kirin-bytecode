package ru.mike.kirinbytecode.util;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class DummyUtils {
    public static final String DUMMY_WRAPPER_RANDOM_METHODS_PREFIX = "dummyWrapperMethodRandom";
    public static final String DUMMY_PRIMITIVE_RANDOM_METHODS_PREFIX = "dummyPrimitiveMethodRandom";

    public static Method getRandomDummyWrapperMethod() {
        return getRandomMethod(getDummyWrapperRandomMethods());
    }

    public static Method getRandomDummyPrimitiveMethod() {
        return getRandomMethod(getDummyPrimitiveRandomMethods());
    }

    private static Method getRandomMethod(List<Method> methods) {
        Collections.shuffle(methods);
        return methods.iterator().next();
    }

    public static List<Method> getDummyWrapperRandomMethods() {
        return getMethodsByNamePrefix(DummyClassA.class, DUMMY_WRAPPER_RANDOM_METHODS_PREFIX);
    }

    public static List<Method> getDummyPrimitiveRandomMethods() {
        return getMethodsByNamePrefix(DummyClassA.class, DUMMY_PRIMITIVE_RANDOM_METHODS_PREFIX);
    }

    public static List<Method> getMethodsByNamePrefix(Class<?> clazz, String namePrefix) {
        return Arrays.stream(clazz.getDeclaredMethods())
                .filter(m -> m.getName().contains(namePrefix))
                .collect(Collectors.toList());
    }
}
