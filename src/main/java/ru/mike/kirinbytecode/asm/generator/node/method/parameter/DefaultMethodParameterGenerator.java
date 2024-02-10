package ru.mike.kirinbytecode.asm.generator.node.method.parameter;

import org.objectweb.asm.tree.MethodNode;
import ru.mike.kirinbytecode.asm.definition.MethodDefinition;
import ru.mike.kirinbytecode.asm.definition.parameter.ParameterDefinition;

import java.util.List;

public class DefaultMethodParameterGenerator<T> implements MethodParameterGenerator<T> {

    @Override
    public void visitMethodParameters(MethodDefinition<T> methodDefinition, MethodNode mn) {
        List<ParameterDefinition> parameterDefinitions = methodDefinition.getParameterDefinitions();

        for (int i = 0, parameterDefinitionsSize = parameterDefinitions.size(); i < parameterDefinitionsSize; i++) {
            ParameterDefinition parameterDefinition = parameterDefinitions.get(i);
            mn.visitParameter(parameterDefinition.getName(), 0);
        }
    }
}
