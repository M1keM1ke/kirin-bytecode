package ru.mike.kirinbytecode.asm.generator;

import org.objectweb.asm.tree.MethodNode;
import ru.mike.kirinbytecode.asm.definition.MethodDefinition;

public interface MethodGenerator<T> extends Generator<T> {

    void setMethodDefinition(MethodDefinition<T> methodDefinition);

    MethodNode getMn();
}
