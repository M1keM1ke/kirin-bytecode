package ru.mike.kirinbytecode.asm.generator.node.annotation;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;
import org.objectweb.asm.AnnotationVisitor;

import javax.annotation.Nullable;

@AllArgsConstructor
@Builder
@Getter
@ToString
public class AnnotationGenerationContext {
    private AnnotationVisitor annotationVisitor;

    @Nullable
    private String name;

    private Object value;

    @Nullable
    private String descriptor;

    private AnnotationGenerator annotationGenerator;
}
