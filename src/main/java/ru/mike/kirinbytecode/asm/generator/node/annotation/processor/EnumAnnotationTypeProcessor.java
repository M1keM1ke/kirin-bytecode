package ru.mike.kirinbytecode.asm.generator.node.annotation.processor;

import com.google.auto.service.AutoService;
import org.objectweb.asm.Type;
import ru.mike.kirinbytecode.asm.generator.node.annotation.AnnotationGenerationContext;

@AutoService(AnnotationParameterTypeProcessor.class)
public class EnumAnnotationTypeProcessor implements AnnotationParameterTypeProcessor {
    @Override
    public Boolean isSuitableProcessor(AnnotationGenerationContext annotationGenerationContext) {
        return annotationGenerationContext.getValue().getClass().isEnum();
    }

    @Override
    public void process(AnnotationGenerationContext annotationGenerationContext) {
        Object value = annotationGenerationContext.getValue();

        annotationGenerationContext.getAnnotationGenerator()
                .visit(
                        annotationGenerationContext.getAnnotationVisitor(),
                        value,
                        Type.getDescriptor(value.getClass()),
                        annotationGenerationContext.getName()
                );
    }
}
