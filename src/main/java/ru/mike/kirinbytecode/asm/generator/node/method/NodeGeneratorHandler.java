package ru.mike.kirinbytecode.asm.generator.node.method;

import org.objectweb.asm.tree.MethodNode;
import ru.mike.kirinbytecode.asm.definition.MethodDefinition;
import ru.mike.kirinbytecode.asm.definition.proxy.ProxyClassDefinition;

public interface NodeGeneratorHandler<T> {

    boolean isSuitableHandler(MethodDefinition<T> methodDefinition);

    MethodNode generateMethodNode(ProxyClassDefinition<T> definition, MethodDefinition<T> methodDefinition);




}
