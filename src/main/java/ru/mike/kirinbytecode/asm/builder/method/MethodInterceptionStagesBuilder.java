package ru.mike.kirinbytecode.asm.builder.method;

import ru.mike.kirinbytecode.asm.builder.InterceptorImplementation;
import ru.mike.kirinbytecode.asm.definition.InterceptedMethodDefinition;
import ru.mike.kirinbytecode.asm.generator.node.method.DefaultMethodGenerator;

public interface MethodInterceptionStagesBuilder<T> extends MethodInterceptionBuilder<T> {

    MethodInterceptionStagesBuilder<T> intercept(InterceptorImplementation implementation);

    /**
     * Метод, выполняющий код до проксируемого метода.
     * Создает {@link ru.mike.kirinbytecode.asm.definition.AfterMethodDefinition}
     * для проксируемого метода и устанавливает его в {@link InterceptedMethodDefinition}.
     * Как это работает: создается поле в {@link DefaultMethodGenerator}
     * типа {@link Runnable}, вызывается через INVOKEINTERFACE и заполняется
     * в {@link ru.mike.kirinbytecode.asm.builder.LoadedType#fillEmptyFields(T)}
     *
     * @param runnable параметр, принимающий функциональный интерфейс {@link Runnable} с кодом,
     * который должен выполниться до выполнения проксируемого метода
     * @return {@link MethodInterceptionBuilder}
     */
    MethodInterceptionStagesBuilder<T> before(Runnable runnable);

    /**
     * Метод, выполняющий код после проксируемого метода.
     * Создает {@link ru.mike.kirinbytecode.asm.definition.AfterMethodDefinition}
     * для проксируемого метода и устанавливает его в {@link InterceptedMethodDefinition}.
     * Как это работает: создается поле в {@link DefaultMethodGenerator}
     * типа {@link Runnable}, вызывается через INVOKEINTERFACE и заполняется
     * в {@link ru.mike.kirinbytecode.asm.builder.LoadedType#fillEmptyFields(T)}
     *
     * @param runnable параметр, принимающий функциональный интерфейс {@link Runnable} с кодом,
     * который должен выполниться после выполнения проксируемого метода
     * @return {@link MethodInterceptionBuilder}
     */
    MethodInterceptionStagesBuilder<T> after(Runnable runnable);
}
