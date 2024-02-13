package ru.mike.kirinbytecode.asm.generator.node.annotation.type;


import ru.mike.kirinbytecode.asm.generator.node.annotation.node.AbstractAnnotationNodeVisitor;
import ru.mike.kirinbytecode.asm.generator.node.annotation.node.AnnotationNodeVisitorImpl;

public abstract class AbstractAnnotationTypeGenerator implements AnnotationTypeGenerator {
    protected AbstractAnnotationNodeVisitor annotationNodeVisitor = new AnnotationNodeVisitorImpl();
}
