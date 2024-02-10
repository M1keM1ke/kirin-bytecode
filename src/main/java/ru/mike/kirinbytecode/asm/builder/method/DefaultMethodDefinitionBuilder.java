package ru.mike.kirinbytecode.asm.builder.method;

import ru.mike.kirinbytecode.asm.builder.Builder;
import ru.mike.kirinbytecode.asm.builder.InterceptorImplementation;
import ru.mike.kirinbytecode.asm.builder.SubclassDynamicTypeBuilder;
import ru.mike.kirinbytecode.asm.definition.MethodDefinition;
import ru.mike.kirinbytecode.asm.definition.parameter.ParameterDefinition;
import ru.mike.kirinbytecode.asm.definition.proxy.ProxyClassDefinition;

import java.lang.reflect.Type;

public class DefaultMethodDefinitionBuilder<T> implements MethodDefinitionBuilder<T> {
    private ProxyClassDefinition<T> definition;
    private MethodDefinition<T> methodDefinition;
    private SubclassDynamicTypeBuilder<T> subclassDynamicTypeBuilder;

    public DefaultMethodDefinitionBuilder(
            ProxyClassDefinition<T> definition,
            MethodDefinition<T> methodDefinition,
            SubclassDynamicTypeBuilder<T> subclassDynamicTypeBuilder
    ) {
        this.definition = definition;
        this.methodDefinition = methodDefinition;
        this.subclassDynamicTypeBuilder = subclassDynamicTypeBuilder;
    }

    @Override
    public MethodDefinitionBuilder<T> withParameters(Class<?>[] parametersTypes) {
        definition.getProxyClassDefinedMethodsDefinition()
                .getDefinedMethodDefinitionBuilder()
                .addAnonymousParametersAndUpdateMethodDescriptor(methodDefinition, parametersTypes)
        ;

        return this;
    }

    @Override
    public MethodDefinitionBuilder<T> throwing(Type type) {
        //TODO: доделать
        return this;
    }

    @Override
    public MethodDefinitionAnnotatableBuilder<T> withParameter(String name, Class<?> type) {
        ParameterDefinition parameterDefinition = definition.getProxyClassDefinedMethodsDefinition()
                .getDefinedMethodDefinitionBuilder()
                .addNamedParameterAndUpdateMethodDescriptor(methodDefinition, name, type);

        return new DefaultMethodDefinitionAnnotatableBuilder<>(this, parameterDefinition);
    }

    @Override
    public MethodInterceptionBuilder<T> intercept(InterceptorImplementation implementation) {
        methodDefinition.setImplementation(implementation);
        methodDefinition.getMethodGenerator().setMethodDefinition(methodDefinition);

        return this;
    }

    @Override
    public Builder<T> and() {
        return subclassDynamicTypeBuilder;
    }
}
