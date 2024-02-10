package ru.mike.kirinbytecode.asm.builder.method;

import ru.mike.kirinbytecode.asm.builder.InterceptorImplementation;
import ru.mike.kirinbytecode.asm.builder.other.Junction;

public interface MethodInterceptionBuilder<T> extends Junction<T> {

    MethodInterceptionBuilder<T> intercept(InterceptorImplementation implementation);


}
