package ru.mike.kirinbytecode.asm.generator.node.method.parameter;

import org.objectweb.asm.tree.MethodNode;
import ru.mike.kirinbytecode.asm.builder.method.AnnotationDefinition;
import ru.mike.kirinbytecode.asm.definition.MethodDefinition;
import ru.mike.kirinbytecode.asm.definition.parameter.ParameterDefinition;
import ru.mike.kirinbytecode.asm.generator.node.annotation.node.AbstractAnnotationNodeGenerator;
import ru.mike.kirinbytecode.asm.generator.node.annotation.node.AnnotationMethodParameterGenerator;
import ru.mike.kirinbytecode.asm.generator.node.annotation.node.AnnotationNodeContext;

import java.util.List;

public class DefaultMethodParameterGenerator<T> implements MethodParameterGenerator<T> {
    private AbstractAnnotationNodeGenerator annotationNodeVisitor;

    public DefaultMethodParameterGenerator() {
        annotationNodeVisitor = new AnnotationMethodParameterGenerator();
    }

    @Override
    public void visitMethodParameters(MethodDefinition<T> methodDefinition, MethodNode mn) {
        List<ParameterDefinition> parameterDefinitions = methodDefinition.getParameterDefinitions();

        for (int i = 0, parameterDefinitionsSize = parameterDefinitions.size(); i < parameterDefinitionsSize; i++) {
            ParameterDefinition parameterDefinition = parameterDefinitions.get(i);
            mn.visitParameter(parameterDefinition.getName(), 0);
        }
    }

    @Override
    public void visitParametersAnnotations(List<ParameterDefinition> parameterDefinitions, MethodNode mn) {
        for (ParameterDefinition parameterDefinition : parameterDefinitions) {
            List<AnnotationDefinition> parameterAnnoDefinitions = parameterDefinition.getAnnotationDefinitions();

            AnnotationNodeContext annotationNodeContext = AnnotationNodeContext.builder()
                    .annotationMethodNodeContext(
                            AnnotationNodeContext.AnnotationMethodNodeContext.builder()
                                    .methodNode(mn)
                                    .methodParameterNum(parameterDefinition.getIndex())
                                    .build()
                    )
                    .build();

//          для каждого параметра проставляем аннотации
            for (AnnotationDefinition annotationDefinition : parameterAnnoDefinitions) {
                annotationNodeVisitor.visitNodeAnnotation(annotationNodeContext, annotationDefinition);
            }
        }
    }
}
