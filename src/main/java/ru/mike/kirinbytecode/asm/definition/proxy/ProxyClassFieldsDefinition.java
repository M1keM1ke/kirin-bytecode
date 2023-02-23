package ru.mike.kirinbytecode.asm.definition.proxy;

import lombok.Getter;
import ru.mike.kirinbytecode.asm.definition.Definition;
import ru.mike.kirinbytecode.asm.definition.FieldDefinition;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

@Getter
public class ProxyClassFieldsDefinition<T> implements Definition {
    private Map<String, FieldDefinition<T>> proxyFields;
    private ProxyClassDefinition<T> definition;

    public ProxyClassFieldsDefinition(ProxyClassDefinition<T> definition) {
        this.definition = definition;
        proxyFields = new HashMap<>();
    }

    public void addProxyField(String name, Type type, Integer... modifiers) {
        FieldDefinition<T> fieldDefinition = new FieldDefinition<T>(definition, name, type, modifiers);

        proxyFields.put(fieldDefinition.getName(), fieldDefinition);
    }


}
