package ru.mike.kirinbytecode.asm.builder.method;

public interface MethodDefinitionBuilder<T> extends MethodDefinitionParameterBuilder<T> {

    MethodDefinitionBuilder<T> withParameters(Class<?>[] parametersTypes);

}
