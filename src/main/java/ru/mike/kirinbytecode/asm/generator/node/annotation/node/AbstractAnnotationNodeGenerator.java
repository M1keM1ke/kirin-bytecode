package ru.mike.kirinbytecode.asm.generator.node.annotation.node;

import ru.mike.kirinbytecode.asm.builder.method.AnnotationDefinition;
import ru.mike.kirinbytecode.asm.generator.node.annotation.type.AnnotationTypeGeneratorChooser;

public abstract class AbstractAnnotationNodeGenerator extends AbstractAnnotationNodeVisitor {
    protected AnnotationTypeGeneratorChooser annotationTypeGeneratorChooser;

    public AbstractAnnotationNodeGenerator() {
        annotationTypeGeneratorChooser = new AnnotationTypeGeneratorChooser();
    }

    public abstract void visitNodeAnnotation(AnnotationNodeContext annotationNodeContext, AnnotationDefinition annotationDefinition);

}
