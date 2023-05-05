package ru.mike.kirinbytecode.asm.generator.node;

import lombok.Getter;
import org.objectweb.asm.Type;
import org.objectweb.asm.tree.FieldNode;
import ru.mike.kirinbytecode.asm.definition.FieldDefinition;
import ru.mike.kirinbytecode.asm.definition.proxy.ProxyClassDefinition;
import ru.mike.kirinbytecode.asm.exception.notfound.FieldNotFoundException;
import ru.mike.kirinbytecode.asm.generator.Generator;

import java.util.Map;

@Getter
public class DefaultFieldGenerator<T> implements Generator {
    private ProxyClassDefinition<T> definition;
    private FieldNode fn;
    private String fieldName;
    private FieldDefinition<T> fieldDefinition;

    public DefaultFieldGenerator(ProxyClassDefinition<T> definition, String fieldName) {
        this.definition = definition;
        this.fieldName = fieldName;
    }

    @Override
    public void generateNode() {
        Map<String, FieldDefinition<T>> proxyFields = definition.getProxyClassFieldsDefinition().getProxyFields();

        if (!proxyFields.containsKey(fieldName)) {
            throw new FieldNotFoundException("Unable to find proxy field by name:" + fieldName);
        }

        this.fieldDefinition = proxyFields.get(fieldName);

        fn = new FieldNode(
                fieldDefinition.getSumOfModifiers(),
                fieldDefinition.getName(), Type.getDescriptor((Class<?>) fieldDefinition.getType()),
                null,
//               value == null потому что заполнение идет после создания инстанста (см. LoadedType.newInstance)
                null
        );
    }
}
