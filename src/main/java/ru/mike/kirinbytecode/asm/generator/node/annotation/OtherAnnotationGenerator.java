package ru.mike.kirinbytecode.asm.generator.node.annotation;

import org.objectweb.asm.AnnotationVisitor;

import javax.annotation.Nullable;

public class OtherAnnotationGenerator extends AbstractAnnotationGenerator {

    @Override
    public void visit(AnnotationVisitor annotationVisitor, Object value, String descriptor, @Nullable String name) {
        defaultAnnotationGenerator.visitOther(annotationVisitor, name, value);
    }
}
