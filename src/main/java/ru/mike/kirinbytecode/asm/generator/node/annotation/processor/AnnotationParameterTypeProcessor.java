package ru.mike.kirinbytecode.asm.generator.node.annotation.processor;

import ru.mike.kirinbytecode.asm.generator.node.annotation.AnnotationGenerationContext;

public interface AnnotationParameterTypeProcessor {

    Boolean isSuitableProcessor(AnnotationGenerationContext annotationGenerationContext);

    void process(AnnotationGenerationContext annotationGenerationContext);
}
