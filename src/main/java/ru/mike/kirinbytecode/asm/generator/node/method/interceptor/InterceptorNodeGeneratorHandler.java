package ru.mike.kirinbytecode.asm.generator.node.method.interceptor;

import ru.mike.kirinbytecode.asm.definition.proxy.ProxyClassDefinition;
import ru.mike.kirinbytecode.asm.generator.node.method.NodeGeneratorHandler;

public interface InterceptorNodeGeneratorHandler<T> extends NodeGeneratorHandler<T> {

    void checkModifiersCorrectOrThrow(ProxyClassDefinition<T> definition, String interceptedMethodName, int modifiers);
}
