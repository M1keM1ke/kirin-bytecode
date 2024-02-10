package ru.mike.kirinbytecode.asm.builder.method;

import java.lang.reflect.Type;

public interface MethodDefinitionExceptionBuilder<T> extends MethodInterceptionBuilder<T> {
    MethodDefinitionBuilder<T> throwing(Type type);

}
