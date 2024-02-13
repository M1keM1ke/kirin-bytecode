package ru.mike.kirinbytecode.asm.generator.node.method.parameter;

import org.objectweb.asm.tree.MethodNode;
import ru.mike.kirinbytecode.asm.definition.MethodDefinition;
import ru.mike.kirinbytecode.asm.definition.parameter.ParameterDefinition;

import java.util.List;

public interface MethodParameterGenerator<T> {

    void visitMethodParameters(MethodDefinition<T> methodDefinition, MethodNode mn);

    void visitParametersAnnotations(List<ParameterDefinition> parameterDefinitions, MethodNode mn);

}
