package ru.mike.kirinbytecode.asm.util;

import org.objectweb.asm.Type;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Objects;
import java.util.Optional;

public class AsmUtil {

    public static Optional<String> getMethodDescriptor(Class<?> clazz, String methodName, Class<?>[] parametersTypes) {
        for (Method method : clazz.getDeclaredMethods()) {
            if (Objects.equals(methodName, method.getName()) && Arrays.deepEquals(parametersTypes, method.getParameterTypes())) {
                return Optional.of(Type.getMethodDescriptor(method));
            }
        }

        return Optional.empty();
    }
}
