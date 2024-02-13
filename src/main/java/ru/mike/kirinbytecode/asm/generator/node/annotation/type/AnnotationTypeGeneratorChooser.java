package ru.mike.kirinbytecode.asm.generator.node.annotation.type;

import org.objectweb.asm.AnnotationVisitor;
import ru.mike.kirinbytecode.asm.builder.method.AnnotationValue;
import ru.mike.kirinbytecode.asm.generator.node.annotation.AnnotationGenerationContext;
import ru.mike.kirinbytecode.asm.generator.node.annotation.processor.AnnotationParameterTypeProcessor;

import java.util.ServiceLoader;
import java.util.stream.StreamSupport;

public class AnnotationTypeGeneratorChooser {
    ServiceLoader<AnnotationParameterTypeProcessor> loader;

    public AnnotationTypeGeneratorChooser() {
        this.loader = ServiceLoader.load(AnnotationParameterTypeProcessor.class);
    }

    public void choose(AnnotationVisitor annotationVisitor, String methodName, AnnotationValue<?> annotationValue) {
        Object value = annotationValue.getValue();
        AnnotationGenerationContext agc = AnnotationGenerationContext.builder()
                .annotationVisitor(annotationVisitor)
                .name(methodName)
                .value(value)
                .annotationGenerator(annotationValue.getAnnotationGenerator())
                .build();

        StreamSupport.stream(loader.spliterator(), false)
                .filter(ap -> ap.isSuitableProcessor(agc))
                .findFirst()
                .orElseThrow(() -> new RuntimeException(
                        "Not found suitable AnnotationParameterTypeProcessor. Context:" + agc))
                .process(agc);
    }
}
