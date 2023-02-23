package ru.mike.kirinbytecode.asm.generator.name;

import ru.mike.kirinbytecode.asm.generator.NameGenerator;

import java.util.Objects;
import java.util.UUID;

public class DefaultUUIDLazyProxyClassNameGenerator implements NameGenerator {
    private String generatedName;

    @Override
    public String getGeneratedName(String name) {
        if (Objects.isNull(generatedName)) {
            generatedName = name + UUID.randomUUID().toString().replaceAll("-", "");
        }

        return generatedName;
    }
}
