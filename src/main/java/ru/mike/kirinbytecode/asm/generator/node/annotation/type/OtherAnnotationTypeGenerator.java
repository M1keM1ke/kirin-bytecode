package ru.mike.kirinbytecode.asm.generator.node.annotation.type;

import org.objectweb.asm.AnnotationVisitor;

import javax.annotation.Nullable;

public class OtherAnnotationTypeGenerator extends AbstractAnnotationTypeGenerator {

    @Override
    public void visit(AnnotationVisitor annotationVisitor, Object value, String descriptor, @Nullable String name) {
        annotationNodeVisitor.visitOther(annotationVisitor, name, value);
    }
}
