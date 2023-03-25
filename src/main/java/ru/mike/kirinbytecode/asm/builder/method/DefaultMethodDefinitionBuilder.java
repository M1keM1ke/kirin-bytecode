package ru.mike.kirinbytecode.asm.builder.method;

import ru.mike.kirinbytecode.asm.builder.Builder;
import ru.mike.kirinbytecode.asm.builder.InterceptorImplementation;
import ru.mike.kirinbytecode.asm.builder.SubclassDynamicTypeBuilder;
import ru.mike.kirinbytecode.asm.definition.AfterMethodDefinition;
import ru.mike.kirinbytecode.asm.definition.BeforeMethodDefinition;
import ru.mike.kirinbytecode.asm.definition.InterceptedMethodsDefinition;
import ru.mike.kirinbytecode.asm.definition.MethodDefinition;
import ru.mike.kirinbytecode.asm.generator.name.FieldNameGenerator;

public class DefaultMethodDefinitionBuilder<T> implements MethodDefinitionBuilder<T> {
    private InterceptedMethodsDefinition<T> interceptedMethodsDefinition;
    private SubclassDynamicTypeBuilder<T> subclassDynamicTypeBuilder;

    public DefaultMethodDefinitionBuilder(InterceptedMethodsDefinition<T> interceptedMethodsDefinition,
                                          SubclassDynamicTypeBuilder<T> subclassDynamicTypeBuilder
    ) {
        this.interceptedMethodsDefinition = interceptedMethodsDefinition;
        this.subclassDynamicTypeBuilder = subclassDynamicTypeBuilder;
    }

    public MethodDefinitionBuilder<T> intercept(InterceptorImplementation implementation) {
        for (MethodDefinition<T> methodDefinition : interceptedMethodsDefinition.getProxyMethods().values()) {
            methodDefinition.setImplementation(implementation);
            methodDefinition.getMethodGenerator().setMethodDefinition(methodDefinition);
        }

        return this;
    }

    @Override
    public MethodDefinitionBuilder<T> before(Runnable runnable) {
        for (MethodDefinition<T> methodDefinition : interceptedMethodsDefinition.getProxyMethods().values()) {
            BeforeMethodDefinition beforeMethodDefinition = new BeforeMethodDefinition(runnable, new FieldNameGenerator());
            methodDefinition.setBeforeMethodDefinition(beforeMethodDefinition);
        }

        return this;
    }

    @Override
    public MethodDefinitionBuilder<T> after(Runnable runnable) {
        for (MethodDefinition<T> methodDefinition : interceptedMethodsDefinition.getProxyMethods().values()) {
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
