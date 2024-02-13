package ru.mike.kirinbytecode.asm.generator.node.method.annotation;

import org.objectweb.asm.tree.MethodNode;
import ru.mike.kirinbytecode.asm.builder.method.AnnotationDefinition;
import ru.mike.kirinbytecode.asm.definition.MethodDefinition;
import ru.mike.kirinbytecode.asm.generator.node.annotation.node.AbstractAnnotationNodeGenerator;
import ru.mike.kirinbytecode.asm.generator.node.annotation.node.AnnotationMethodGenerator;
import ru.mike.kirinbytecode.asm.generator.node.annotation.node.AnnotationNodeContext;

import java.util.List;

public class MethodAnnotationGenerator {
    private AbstractAnnotationNodeGenerator annotationNodeVisitor;

    public MethodAnnotationGenerator() {
        this.annotationNodeVisitor = new AnnotationMethodGenerator();
    }

    public void visitMethodAnnotations(MethodDefinition<?> definedMethodDefinition, MethodNode mn) {
        List<AnnotationDefinition> annotationDefinitions = definedMethodDefinition.getAnnotationDefinitions();
        AnnotationNodeContext annotationNodeContext = AnnotationNodeContext.builder()
                .annotationMethodNodeContext(
                        AnnotationNodeContext.AnnotationMethodNodeContext.builder()
                                .methodNode(mn)
                                .build()
                )
                .build();

        for (AnnotationDefinition annotationDefinition : annotationDefinitions) {
            annotationNodeVisitor.visitNodeAnnotation(annotationNodeContext, annotationDefinition);
        }
    }
}
