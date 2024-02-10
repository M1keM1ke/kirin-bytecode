package ru.mike.kirinbytecode.asm.generator.node.method.parameter;

import org.objectweb.asm.tree.MethodNode;
import ru.mike.kirinbytecode.asm.definition.MethodDefinition;

public interface MethodParameterGenerator<T> {

    void visitMethodParameters(MethodDefinition<T> methodDefinition, MethodNode mn);

}
