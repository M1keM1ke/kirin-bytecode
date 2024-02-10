package ru.mike.kirinbytecode.asm.builder.method;

import lombok.RequiredArgsConstructor;

import java.lang.annotation.Annotation;
import java.util.Map;

@RequiredArgsConstructor
public class DefaultAnnotationDefinition implements AnnotationDefinition {
    private final Class<? extends Annotation> annotationType;
    private final Map<String, AnnotationValue<?>> annotationValues;

    @Override
    public Map<String, AnnotationValue<?>> getValues() {
        return annotationValues;
    }

    @Override
    public Class<? extends Annotation> getType() {
        return annotationType;
    }


}
