package ru.mike.kirinbytecode.asm.builder.method;

import ru.mike.kirinbytecode.asm.builder.Builder;
import ru.mike.kirinbytecode.asm.builder.InterceptorImplementation;
import ru.mike.kirinbytecode.asm.builder.SubclassDynamicTypeBuilder;
import ru.mike.kirinbytecode.asm.definition.AfterMethodDefinition;
import ru.mike.kirinbytecode.asm.definition.BeforeMethodDefinition;
import ru.mike.kirinbytecode.asm.definition.InterceptedMethodDefinition;
import ru.mike.kirinbytecode.asm.definition.InterceptedMethodsDefinition;
import ru.mike.kirinbytecode.asm.generator.name.FieldNameGenerator;

import java.util.List;

public class DefaultMethodInterceptionBuilder<T> implements MethodInterceptionStagesBuilder<T> {
    private InterceptedMethodsDefinition<T> interceptedMethodsDefinition;
    private SubclassDynamicTypeBuilder<T> subclassDynamicTypeBuilder;

    public DefaultMethodInterceptionBuilder(
            InterceptedMethodsDefinition<T> interceptedMethodsDefinition,
            SubclassDynamicTypeBuilder<T> subclassDynamicTypeBuilder
    ) {
        this.interceptedMethodsDefinition = interceptedMethodsDefinition;
        this.subclassDynamicTypeBuilder = subclassDynamicTypeBuilder;
    }

    public MethodInterceptionStagesBuilder<T> intercept(InterceptorImplementation implementation) {
        for (InterceptedMethodDefinition<T> methodDefinition : interceptedMethodsDefinition.getProxyMethods()) {
            methodDefinition.setImplementation(implementation);
            methodDefinition.getMethodGenerator().setMethodDefinition(methodDefinition);
        }

        return this;
    }

    @Override
    public MethodInterceptionBuilder<T> annotateMethod(AnnotationDefinition annotationDefinition) {
        List<InterceptedMethodDefinition<T>> proxyMethods = interceptedMethodsDefinition.getProxyMethods();

        for (InterceptedMethodDefinition<T> methodDefinition : proxyMethods) {
            methodDefinition.getAnnotationDefinitions().add(annotationDefinition);
        }

        return this;
    }

    @Override
    public MethodInterceptionStagesBuilder<T> before(Runnable runnable) {
        for (InterceptedMethodDefinition<T> methodDefinition : interceptedMethodsDefinition.getProxyMethods()) {
            BeforeMethodDefinition beforeMethodDefinition = new BeforeMethodDefinition(runnable, new FieldNameGenerator());
            methodDefinition.setBeforeMethodDefinition(beforeMethodDefinition);
        }

        return this;
    }

    @Override
    public MethodInterceptionStagesBuilder<T> after(Runnable runnable) {
        for (InterceptedMethodDefinition<T> methodDefinition : interceptedMethodsDefinition.getProxyMethods()) {
            AfterMethodDefinition afterMethodDefinition = new AfterMethodDefinition(runnable, new FieldNameGenerator());
            methodDefinition.setAfterMethodDefinition(afterMethodDefinition);
        }

        return this;
    }

    @Override
    public Builder<T> and() {
        return subclassDynamicTypeBuilder;
    }
}
