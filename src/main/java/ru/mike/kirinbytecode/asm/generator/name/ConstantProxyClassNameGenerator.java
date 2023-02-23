package ru.mike.kirinbytecode.asm.generator.name;

import ru.mike.kirinbytecode.asm.generator.NameGenerator;

import java.util.Objects;

public class ConstantProxyClassNameGenerator implements NameGenerator {
    private String generatedName;

    public ConstantProxyClassNameGenerator(String name) {
        this.generatedName = name;
    }

    @Override
    public String getGeneratedName(String name) {
        if (Objects.isNull(generatedName)) {
            this.generatedName = name;
        }

        return generatedName;
    }
}
