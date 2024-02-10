package ru.mike.kirinbytecode.asm.util;

import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class AnnotationUtil {


    public static boolean isAnnotation(Object value) {
        return isJvmAnnotation(value) || isUserAnnotationClass(value);
    }

    public static boolean isJvmAnnotation(Object value) {
        Class<?> valueClass = value.getClass();

        if (Objects.equals(Proxy.class.getName(), valueClass.getSuperclass().getName())) {
            return true;
        }

        return valueClass.isAnnotation();
    }

    public static boolean isUserAnnotationClass(Object value) {
        List<Class<?>> annotationInterfaces = getAnnotationInterfaces(value.getClass());

        return annotationInterfaces.size() == 1;
    }

    public static List<Class<?>> getAnnotationInterfaces(Class<?> valueClass) {
        return Arrays.stream(valueClass.getInterfaces())
                .filter(Class::isAnnotation)
                .collect(Collectors.toList());
    }

    public static Class<?> getAnnotationInterfaceOrThrow(Class<?> valueClass) {
        List<Class<?>> annotationInterfaces = getAnnotationInterfaces(valueClass);

        if (annotationInterfaces.size() != 1) {
            String msg = String.format("Unsupported annotation interfaces count:%d " +
                            "for user annotation proxy class %s. Annotation interfaces:%s",
                    annotationInterfaces.size(), valueClass, annotationInterfaces

            );
            throw new RuntimeException(msg);
        }

        return annotationInterfaces.get(0);
    }

    public static List<Method> getAnnotationMethods(Object value) {
        Class<?> valueClass = value.getClass();

//      JVM аннотации в рантайме - это прокси класс extends Proxy implements SomeAnnotation
        boolean isJvmAnnotation = Objects.equals(Proxy.class.getName(), valueClass.getSuperclass().getName());

        if (isJvmAnnotation) {
            List<String> uselessMethods = List.of("equals", "toString", "hashCode", "annotationType");

            return Arrays.stream(valueClass.getDeclaredMethods())
                    .filter(m -> !uselessMethods.contains(m.getName()))
                    .collect(Collectors.toList());
        }

        Class<?> annotationInterface = getAnnotationInterfaceOrThrow(valueClass);

        return Arrays.asList(annotationInterface.getDeclaredMethods());
    }

}
