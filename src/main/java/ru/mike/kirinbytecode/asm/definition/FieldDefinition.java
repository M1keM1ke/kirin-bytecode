package ru.mike.kirinbytecode.asm.definition;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import ru.mike.kirinbytecode.asm.definition.proxy.ProxyClassDefinition;
import ru.mike.kirinbytecode.asm.generator.FieldGenerator;
import ru.mike.kirinbytecode.asm.generator.node.DefaultFieldGenerator;

import java.lang.reflect.Type;

@Getter
@Setter
@AllArgsConstructor
public class FieldDefinition<T> implements Definition {
    private String name;
    private Type type;
    private Integer[] modifiers;
    private Object value;
    private ProxyClassDefinition<T> definition;
    private FieldGenerator<T> fieldGenerator;


    public FieldDefinition(ProxyClassDefinition<T> definition, String name, Type type, Integer[] modifiers) {
        this.definition = definition;
        fieldGenerator = new DefaultFieldGenerator<>(definition, name);
        this.name = name;
        this.type = type;
        this.modifiers = modifiers;
    }

    public FieldDefinition(ProxyClassDefinition<T> definition, String name, Object value, Type type, Integer[] modifiers) {
        this(definition, name, type, modifiers);
        this.value = value;
    }

    public int getSumOfModifiers() {
        int sum = 0;

        for (int i = 0; i < modifiers.length; i++) {
            sum += modifiers[i];
        }

        return sum;
    }
}
