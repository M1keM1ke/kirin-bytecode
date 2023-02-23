package ru.mike.kirinbytecode.asm.builder.method;

import ru.mike.kirinbytecode.asm.builder.Builder;
import ru.mike.kirinbytecode.asm.builder.InterceptorImplementation;

public interface MethodDefinitionBuilder<T> {

    Builder<T> intercept(InterceptorImplementation implementation);
}
