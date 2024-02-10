package ru.mike.kirinbytecode.asm.builder.method;

import ru.mike.kirinbytecode.asm.definition.parameter.ParameterDefinition;

public class DefaultMethodDefinitionAnnotatableBuilder<T> implements MethodDefinitionAnnotatableBuilder<T> {
    private MethodDefinitionBuilder<T> methodDefinitionBuilder;
    private ParameterDefinition parameterDefinition;

    public DefaultMethodDefinitionAnnotatableBuilder(DefaultMethodDefinitionBuilder<T> methodDefinitionBuilder, ParameterDefinition parameterDefinition) {
        this.methodDefinitionBuilder = methodDefinitionBuilder;
        this.parameterDefinition = parameterDefinition;
    }

    public MethodDefinitionAnnotatableBuilder<T> annotateParameter(AnnotationDefinition annotationDefinition) {
        parameterDefinition.getAnnotationDefinitions().add(annotationDefinition);

        return this;
    }

    @Override
    public MethodDefinitionAnnotatableBuilder<T> addModifiersForParameter(Integer modifiers) {
        //TODO: доделать
        return this;
    }

    @Override
    public MethodDefinitionBuilder<T> build() {
        return methodDefinitionBuilder;
    }
}
