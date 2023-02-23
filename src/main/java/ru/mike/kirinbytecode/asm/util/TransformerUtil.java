package ru.mike.kirinbytecode.asm.util;

public class TransformerUtil {

    public static <T> String transformClassNameToAsmClassName(Class<T> clazz) {
        return clazz.getName().replace(".", "/");
    }

    public static <T> String transformClassNameToAsmPackageName(Class<T> clazz) {
        String name = clazz.getName();

        String[] splittedClassName = name.split("\\.");
        StringBuilder packageNameBuilder = new StringBuilder();

        for (int i = 0; i < splittedClassName.length - 1; i++) {
            packageNameBuilder.append(splittedClassName[i]).append(".");
        }

        packageNameBuilder.replace(packageNameBuilder.toString().length() - 1, packageNameBuilder.toString().length(), "");

        return packageNameBuilder.toString();
    }
}
