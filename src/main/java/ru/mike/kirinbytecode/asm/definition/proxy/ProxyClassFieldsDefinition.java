package ru.mike.kirinbytecode.asm.definition.proxy;

import lombok.Getter;
import org.objectweb.asm.Opcodes;
import ru.mike.kirinbytecode.asm.definition.Definition;
import ru.mike.kirinbytecode.asm.definition.FieldDefinition;
import ru.mike.kirinbytecode.asm.exception.ProxyFieldAlreadyExistsException;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Getter
public class ProxyClassFieldsDefinition<T> implements Definition, Opcodes {
    private Map<String, FieldDefinition<T>> proxyFields;
    private ProxyClassDefinition<T> definition;

    public ProxyClassFieldsDefinition(ProxyClassDefinition<T> definition) {
        this.definition = definition;
        proxyFields = new HashMap<>();
    }

    public void createFieldDefinition(String name, Type type, Integer... modifiers) throws ProxyFieldAlreadyExistsException {
        if (proxyFields.containsKey(name)) {
            throw new ProxyFieldAlreadyExistsException("Unable to create field definition by field name:" +
                    name + ". Field definition with that name already exists.");
        }

        FieldDefinition<T> fieldDefinition;

        if (Objects.isNull(modifiers) || modifiers.length == 0) {
            Integer[] defaultPrivateModifier = {ACC_PUBLIC};
            fieldDefinition = new FieldDefinition<>(definition, name, type, defaultPrivateModifier);
        } else {
            fieldDefinition = new FieldDefinition<>(definition, name, type, modifiers);
        }

        proxyFields.put(fieldDefinition.getName(), fieldDefinition);
    }

    public void createFieldDefinition(String name, Object value, Type type, Integer... modifiers) {
        FieldDefinition<T> fieldDefinition = new FieldDefinition<>(definition, name, value, type, modifiers);

        proxyFields.put(fieldDefinition.getName(), fieldDefinition);
    }
}
