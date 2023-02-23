package ru.mike.kirinbytecode.asm.definition.proxy;

import lombok.Getter;
import ru.mike.kirinbytecode.asm.definition.Definition;
import ru.mike.kirinbytecode.asm.definition.InterceptedMethodsDefinition;
import ru.mike.kirinbytecode.asm.definition.MethodDefinition;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Getter
public class ProxyClassMethodsDefinition<T> implements Definition {
    private List<InterceptedMethodsDefinition<T>> interceptedMethodsDefinitions;
    private ProxyClassDefinition<T> proxyClassDefinition;


    public ProxyClassMethodsDefinition(ProxyClassDefinition<T> proxyClassDefinition) {
        this.proxyClassDefinition = proxyClassDefinition;
        interceptedMethodsDefinitions = new ArrayList<>();
    }

    public void addInterceptedMethodsDefinition(InterceptedMethodsDefinition<T> interceptedMethodsDefinition) {
        interceptedMethodsDefinitions.add(interceptedMethodsDefinition);
    }

    public List<MethodDefinition<T>> getAllMethodsDefinitions() {
        return interceptedMethodsDefinitions.stream()
                .flatMap(imd -> imd.getProxyMethods().values().stream())
                .collect(Collectors.toList());
    }
}
