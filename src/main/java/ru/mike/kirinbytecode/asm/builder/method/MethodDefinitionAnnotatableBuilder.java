package ru.mike.kirinbytecode.asm.builder.method;


public interface MethodDefinitionAnnotatableBuilder<T> {
    MethodDefinitionAnnotatableBuilder<T> annotateParameter(AnnotationDefinition annotationDefinition);

    MethodDefinitionAnnotatableBuilder<T> addModifiersForParameter(Integer modifiers);

    MethodDefinitionBuilder<T> build();

}
