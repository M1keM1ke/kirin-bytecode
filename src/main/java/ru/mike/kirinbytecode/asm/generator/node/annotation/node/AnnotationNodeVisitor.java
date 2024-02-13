package ru.mike.kirinbytecode.asm.generator.node.annotation.node;

import org.objectweb.asm.AnnotationVisitor;

import javax.annotation.Nullable;

public interface AnnotationNodeVisitor {

    /**
     * Инициирует метод с типом возвращаемого значения
     * в виде аннотации {@link java.lang.annotation.Annotation} в аннотации
     *
     * @param annotationVisitor asm-visitor аннотации
     * @param name имя метода аннотации
     * @param value значение метода аннотации
     */
    void visitAnnotation(AnnotationVisitor annotationVisitor, Object value, String descriptor, @Nullable String name);

    /**
     * Инициирует метод с типом возвращаемого значения в виде массива в аннотации
     *
     * @param annotationVisitor asm-visitor аннотации
     * @param name имя метода аннотации
     * @param value значение метода аннотации
     */
    void visitArray(AnnotationVisitor annotationVisitor, Object value, @Nullable String name);

    /**
     * Инициирует {@link Enum<?>} метод в аннотации
     *
     * @param annotationVisitor asm-visitor аннотации
     * @param name имя метода аннотации
     * @param value значение метода аннотации
     */
    void visitEnum(AnnotationVisitor annotationVisitor, Object value, @Nullable String name, String descriptor);

    /**
     * Инициирует {@link Class<?>} метод в аннотации
     *
     * @param annotationVisitor asm-visitor аннотации
     * @param name имя метода аннотации
     * @param value значение метода аннотации
     */
    void visitClass(AnnotationVisitor annotationVisitor, @Nullable String name, Object value);

    /**
     * Инициирует метод с возвращаемым значением в виде примитивов или {@link String} в аннотации
     *
     * @param annotationVisitor asm-visitor аннотации
     * @param name имя метода аннотации
     * @param value значение метода аннотации
     */
    void visitOther(AnnotationVisitor annotationVisitor, @Nullable String name, Object value);
}
