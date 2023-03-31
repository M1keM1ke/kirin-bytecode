package ru.mike.kirinbytecode.asm.builder.method;

import ru.mike.kirinbytecode.asm.builder.Builder;
import ru.mike.kirinbytecode.asm.builder.InterceptorImplementation;
import ru.mike.kirinbytecode.asm.generator.node.method.DefaultMethodGenerator;

public interface MethodDefinitionBuilder<T> {

    MethodDefinitionBuilder<T> intercept(InterceptorImplementation implementation);

    /**
     * Метод, выполняющий код до проксируемого метода.
     * Создает {@link ru.mike.kirinbytecode.asm.definition.AfterMethodDefinition}
     * для проксируемого метода и устанавливает его в {@link ru.mike.kirinbytecode.asm.definition.MethodDefinition}.
     * Как это работает: создается поле в {@link DefaultMethodGenerator}
     * типа {@link Runnable}, вызывается через INVOKEINTERFACE и заполняется
     * в {@link ru.mike.kirinbytecode.asm.builder.LoadedType#fillEmptyFields(T)}
     *
     * @param runnable параметр, принимающий функциональный интерфейс {@link Runnable} с кодом,
     * который должен выполниться до выполнения проксируемого метода
     * @return {@link MethodDefinitionBuilder}
     */
    MethodDefinitionBuilder<T> before(Runnable runnable);

    /**
     * Метод, выполняющий код после проксируемого метода.
     * Создает {@link ru.mike.kirinbytecode.asm.definition.AfterMethodDefinition}
     * для проксируемого метода и устанавливает его в {@link ru.mike.kirinbytecode.asm.definition.MethodDefinition}.
     * Как это работает: создается поле в {@link DefaultMethodGenerator}
     * типа {@link Runnable}, вызывается через INVOKEINTERFACE и заполняется
     * в {@link ru.mike.kirinbytecode.asm.builder.LoadedType#fillEmptyFields(T)}
     *
     * @param runnable параметр, принимающий функциональный интерфейс {@link Runnable} с кодом,
     * который должен выполниться после выполнения проксируемого метода
     * @return {@link MethodDefinitionBuilder}
     */
    MethodDefinitionBuilder<T> after(Runnable runnable);

    Builder<T> and();
}
