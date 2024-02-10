package ru.mike.kirinbytecode.asm.generator.node.annotation.processor;

import com.google.auto.service.AutoService;
import ru.mike.kirinbytecode.asm.generator.node.annotation.AnnotationGenerationContext;

@AutoService(AnnotationParameterTypeProcessor.class)
public class ArrayAnnotationTypeProcessor implements AnnotationParameterTypeProcessor {
    @Override
    public Boolean isSuitableProcessor(AnnotationGenerationContext annotationGenerationContext) {
        return annotationGenerationContext.getValue().getClass().isArray();
    }

    @Override
    public void process(AnnotationGenerationContext annotationGenerationContext) {
        annotationGenerationContext.getAnnotationGenerator()
                .visit(
                        annotationGenerationContext.getAnnotationVisitor(),
                        annotationGenerationContext.getValue(),
                        annotationGenerationContext.getDescriptor(),
                        annotationGenerationContext.getName()
                );
    }
}
