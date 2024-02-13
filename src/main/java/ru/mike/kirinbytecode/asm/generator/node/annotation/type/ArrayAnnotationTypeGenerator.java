package ru.mike.kirinbytecode.asm.generator.node.annotation.type;

import lombok.SneakyThrows;
import org.objectweb.asm.AnnotationVisitor;

import javax.annotation.Nullable;

public class ArrayAnnotationTypeGenerator extends AbstractAnnotationTypeGenerator {

    @Override
    @SneakyThrows
    public void visit(AnnotationVisitor annotationVisitor, Object value, String descriptor, @Nullable String name) {
        annotationNodeVisitor.visitArray(annotationVisitor, value, name);
    }
}
