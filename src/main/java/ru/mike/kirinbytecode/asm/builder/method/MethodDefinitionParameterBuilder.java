package ru.mike.kirinbytecode.asm.builder.method;

public interface MethodDefinitionParameterBuilder<T> extends MethodDefinitionExceptionBuilder<T> {
    MethodDefinitionAnnotatableBuilder<T> withParameter(String name, Class<?> type);

}
