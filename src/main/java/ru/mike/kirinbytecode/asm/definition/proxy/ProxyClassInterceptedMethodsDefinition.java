package ru.mike.kirinbytecode.asm.definition.proxy;

import lombok.Getter;
import ru.mike.kirinbytecode.asm.definition.Definition;
import ru.mike.kirinbytecode.asm.definition.InterceptedMethodDefinition;
import ru.mike.kirinbytecode.asm.definition.InterceptedMethodsDefinition;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Getter
public class ProxyClassInterceptedMethodsDefinition<T> implements Definition {
    private List<InterceptedMethodsDefinition<T>> interceptedMethodsDefinitions;
    private ProxyClassDefinition<T> proxyClassDefinition;


    public ProxyClassInterceptedMethodsDefinition(ProxyClassDefinition<T> proxyClassDefinition) {
        this.proxyClassDefinition = proxyClassDefinition;
        interceptedMethodsDefinitions = new ArrayList<>();
    }

    public void addInterceptedMethodsDefinition(InterceptedMethodsDefinition<T> interceptedMethodsDefinition) {
        interceptedMethodsDefinitions.add(interceptedMethodsDefinition);
    }

    public List<InterceptedMethodDefinition<T>> getAllMethodsDefinitions() {
        return interceptedMethodsDefinitions.stream()
                .flatMap(imd -> imd.getProxyMethods().stream())
                .collect(Collectors.toList());
    }
}
