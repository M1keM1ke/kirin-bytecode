package ru.mike.kirinbytecode.asm.builder.field;

import ru.mike.kirinbytecode.asm.builder.SubclassDynamicTypeBuilder;
import ru.mike.kirinbytecode.asm.definition.FieldDefinition;
import ru.mike.kirinbytecode.asm.definition.proxy.ProxyClassDefinition;
import ru.mike.kirinbytecode.asm.exception.FieldNotFoundException;

import javax.annotation.Nullable;
import java.util.Map;

public class DefaultFieldDefinitionBuilder<T> implements FieldDefinitionBuilder<T> {
    private ProxyClassDefinition<T> definition;
    private String fieldName;
    private SubclassDynamicTypeBuilder<T> subclassDynamicTypeBuilder;

    public DefaultFieldDefinitionBuilder(
            ProxyClassDefinition<T> definition,
            String fieldName,
            SubclassDynamicTypeBuilder<T> subclassDynamicTypeBuilder
    ) {
        this.definition = definition;
        this.fieldName = fieldName;
        this.subclassDynamicTypeBuilder = subclassDynamicTypeBuilder;
    }

    @Override
    public FieldDefinitionBuilder<T> value(@Nullable Object value) {
        Map<String, FieldDefinition<T>> proxyFields = definition.getProxyClassFieldsDefinition().getProxyFields();

        if (!proxyFields.containsKey(fieldName)) {
            throw new FieldNotFoundException("Unable to find proxy field by name:" + fieldName);
        }

        FieldDefinition<T> fieldDefinition = proxyFields.get(fieldName);
        fieldDefinition.setValue(value);

        return this;
    }

    @Override
    public SubclassDynamicTypeBuilder<T> and() {
        return subclassDynamicTypeBuilder;
    }
}
