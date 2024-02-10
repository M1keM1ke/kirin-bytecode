package ru.mike.kirinbytecode.asm.generator.node.annotation.processor;

import com.google.auto.service.AutoService;
import org.objectweb.asm.Type;
import ru.mike.kirinbytecode.asm.generator.node.annotation.AnnotationGenerationContext;

@AutoService(AnnotationParameterTypeProcessor.class)
public class ClassAnnotationTypeProcessor implements AnnotationParameterTypeProcessor {
    @Override
    public Boolean isSuitableProcessor(AnnotationGenerationContext annotationGenerationContext) {
        return Class.class.isAssignableFrom(annotationGenerationContext.getValue().getClass());
    }

    @Override
    public void process(AnnotationGenerationContext annotationGenerationContext) {
        annotationGenerationContext.getAnnotationGenerator()
                .visit(
                        annotationGenerationContext.getAnnotationVisitor(),
                        Type.getType((Class<?>)annotationGenerationContext.getValue()),
                        annotationGenerationContext.getDescriptor(),
                        annotationGenerationContext.getName()
                );
    }
}
