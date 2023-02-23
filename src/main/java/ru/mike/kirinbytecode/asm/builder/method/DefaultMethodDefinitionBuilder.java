package ru.mike.kirinbytecode.asm.builder.method;

import ru.mike.kirinbytecode.asm.builder.Builder;
import ru.mike.kirinbytecode.asm.builder.InterceptorImplementation;
import ru.mike.kirinbytecode.asm.builder.SubclassDynamicTypeBuilder;
import ru.mike.kirinbytecode.asm.definition.InterceptedMethodsDefinition;
import ru.mike.kirinbytecode.asm.definition.MethodDefinition;

public class DefaultMethodDefinitionBuilder<T> implements MethodDefinitionBuilder<T> {
    private InterceptedMethodsDefinition<T> interceptedMethodsDefinition;
    private SubclassDynamicTypeBuilder<T> subclassDynamicTypeBuilder;

    public DefaultMethodDefinitionBuilder(InterceptedMethodsDefinition<T> interceptedMethodsDefinition,
                                          SubclassDynamicTypeBuilder<T> subclassDynamicTypeBuilder
    ) {
        this.interceptedMethodsDefinition = interceptedMethodsDefinition;
        this.subclassDynamicTypeBuilder = subclassDynamicTypeBuilder;
    }

    public Builder<T> intercept(InterceptorImplementation implementation) {
        for (MethodDefinition<T> methodDefinition : interceptedMethodsDefinition.getProxyMethods().values()) {
            methodDefinition.setImplementation(implementation);
            methodDefinition.getMethodGenerator().setMethodDefinition(methodDefinition);
        }

        return subclassDynamicTypeBuilder;
    }
}
