package ru.mike.kirinbytecode.asm.generator;

@FunctionalInterface
public interface NameGenerator {
    String getGeneratedName(String name);
}
