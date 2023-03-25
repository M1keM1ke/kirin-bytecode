package ru.mike.kirinbytecode.asm.generator.name;

import ru.mike.kirinbytecode.asm.generator.NameGenerator;

import java.util.Objects;
import java.util.UUID;

public class FieldNameGenerator implements NameGenerator {
    private String generatedName;

    @Override
    public String getGeneratedName(String name) {
        if (Objects.isNull(generatedName)) {
            generatedName = "generatedField" + UUID.randomUUID().toString().replaceAll("-", "");
        }
        return generatedName;
    }
}
