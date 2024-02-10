package ru.mike.kirinbytecode.asm.generator.node.annotation;

import lombok.SneakyThrows;
import org.objectweb.asm.AnnotationVisitor;

import javax.annotation.Nullable;

public class EnumAnnotationGenerator extends AbstractAnnotationGenerator {

    @Override
    @SneakyThrows
    public void visit(AnnotationVisitor annotationVisitor, Object value, String descriptor, @Nullable String name) {
        defaultAnnotationGenerator.visitEnum(annotationVisitor, value, name, descriptor);
    }
}
