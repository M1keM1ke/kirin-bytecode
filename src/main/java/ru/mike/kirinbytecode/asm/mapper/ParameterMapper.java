package ru.mike.kirinbytecode.asm.mapper;

import ru.mike.kirinbytecode.asm.definition.parameter.ParameterDefinition;

import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.List;

public class ParameterMapper {

    private ParameterMapper() {
        throw new RuntimeException();
    }

    public static ParameterDefinition toParameterDefinition(Parameter parameter) {
        return ParameterDefinition.builder()
                .name(parameter.getName())
                .type(parameter.getType())
                .build()
        ;
    }

    public static ParameterDefinition toParameterDefinition(String name, Class<?> type, Integer index) {
        return ParameterDefinition.builder()
                .name(name)
                .type(type)
                .index(index)
                .build()
        ;
    }

    public static List<ParameterDefinition> toParameterDefinitions(Parameter[] parameters) {
        List<ParameterDefinition> parameterDefinitions = new ArrayList<>();

        for (Parameter parameter : parameters) {
            parameterDefinitions.add(toParameterDefinition(parameter));
        }

        return parameterDefinitions;
    }
}
