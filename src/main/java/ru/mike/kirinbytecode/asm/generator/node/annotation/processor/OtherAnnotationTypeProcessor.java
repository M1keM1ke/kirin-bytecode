package ru.mike.kirinbytecode.asm.generator.node.annotation.processor;

import com.google.auto.service.AutoService;
import jdk.dynalink.linker.support.TypeUtilities;
import ru.mike.kirinbytecode.asm.generator.node.annotation.AnnotationGenerationContext;

@AutoService(AnnotationParameterTypeProcessor.class)
public class OtherAnnotationTypeProcessor implements AnnotationParameterTypeProcessor {
    @Override
    public Boolean isSuitableProcessor(AnnotationGenerationContext annotationGenerationContext) {
        Class<?> valueClass = annotationGenerationContext.getValue().getClass();

        return TypeUtilities.isWrapperType(valueClass) || valueClass.isAssignableFrom(String.class);
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
