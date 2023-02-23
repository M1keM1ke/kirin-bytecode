package ru.mike.kirinbytecode.asm.definition.proxy;

import lombok.Getter;
import ru.mike.kirinbytecode.asm.definition.Definition;
import ru.mike.kirinbytecode.asm.definition.InterfaceDefinition;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

@Getter
public class ProxyClassInterfacesDefinition<T> implements Definition {
    private Map<String, InterfaceDefinition<T>> proxyInterfaces;
    private ProxyClassDefinition<T> definition;

    public ProxyClassInterfacesDefinition(ProxyClassDefinition<T> definition) {
        this.definition = definition;
        proxyInterfaces = new HashMap<>();
    }

    public void addProxyInterface(Type type) {
        InterfaceDefinition<T> interfaceDefinition = new InterfaceDefinition<T>(type);

        proxyInterfaces.put(interfaceDefinition.getFullName(), interfaceDefinition);
    }
}
