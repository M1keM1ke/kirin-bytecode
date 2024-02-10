package ru.mike.kirinbytecode.asm.generator.node.annotation;

import org.objectweb.asm.AnnotationVisitor;

import javax.annotation.Nullable;

public interface AnnotationGenerator {

    void visit(AnnotationVisitor annotationVisitor, Object value, String descriptor, @Nullable String name);

}
