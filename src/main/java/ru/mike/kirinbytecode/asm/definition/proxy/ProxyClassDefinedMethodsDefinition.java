package ru.mike.kirinbytecode.asm.definition.proxy;

import lombok.Getter;
import ru.mike.kirinbytecode.asm.definition.DefinedMethodDefinition;
import ru.mike.kirinbytecode.asm.definition.DefinedMethodDefinitionBuilder;
import ru.mike.kirinbytecode.asm.definition.Definition;
import ru.mike.kirinbytecode.asm.definition.MethodDefinition;

import java.util.HashMap;
import java.util.Map;

public class ProxyClassDefinedMethodsDefinition<T> implements Definition {
    private ProxyClassDefinition<T> proxyClassDefinition;

    @Getter
    private DefinedMethodDefinitionBuilder<T> definedMethodDefinitionBuilder;
    @Getter
    private Map<String, MethodDefinition<T>> definedMethods;

    public ProxyClassDefinedMethodsDefinition(ProxyClassDefinition<T> proxyClassDefinition) {
        this.proxyClassDefinition = proxyClassDefinition;
        this.definedMethodDefinitionBuilder = new DefinedMethodDefinitionBuilder();
        this.definedMethods = new HashMap<>();
    }

    public MethodDefinition<T> addNewMethod(String name, Class<?> returnType, int modifiers) {
        MethodDefinition<T> methodDefinition =
                new DefinedMethodDefinition<>(proxyClassDefinition, name, returnType, modifiers);
        String methodKey = generateKey(methodDefinition.getMethodDescriptor(), methodDefinition.getName());
        definedMethods.put(methodKey, methodDefinition);

        return methodDefinition;
    }

    public String generateKey(String descriptor, String methodName) {
        return descriptor + methodName;
    }
}
