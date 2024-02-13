package ru.mike.kirinbytecode.asm.definition;

import lombok.Getter;
import lombok.Setter;
import ru.mike.kirinbytecode.asm.builder.InterceptorImplementation;
import ru.mike.kirinbytecode.asm.builder.method.AnnotationDefinition;
import ru.mike.kirinbytecode.asm.definition.parameter.ParameterDefinition;
import ru.mike.kirinbytecode.asm.generator.MethodGenerator;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public abstract class MethodDefinition<T> implements Definition {
    protected String name;
    protected Class<?> returnType;
    protected int modifiers;
    protected String methodDescriptor;
    protected List<ParameterDefinition> parameterDefinitions = new ArrayList<>();
    protected int parameterCount;
    protected List<AnnotationDefinition> annotationDefinitions = new ArrayList<>();
    protected MethodGenerator<T> methodGenerator;
    protected InterceptorImplementation implementation;

}
