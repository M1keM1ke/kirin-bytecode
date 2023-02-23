package ru.mike.kirinbytecode.asm.builder;

import ru.mike.kirinbytecode.asm.definition.FieldDefinition;
import ru.mike.kirinbytecode.asm.definition.proxy.ProxyClassDefinition;
import ru.mike.kirinbytecode.asm.exception.FieldNotFoundException;

import javax.annotation.Nullable;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.Objects;

public class LoadedType<T> {
    private ProxyClassDefinition<T> definition;

    public LoadedType(ProxyClassDefinition<T> definition) {
        this.definition = definition;
    }

    public T newInstance(@Nullable Class<?>[] parameterTypes, Object[] initArgs) throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        Class<? extends T> generatedClazz = definition.getGeneratedClazz();

        T instance = generatedClazz.getDeclaredConstructor(parameterTypes).newInstance(initArgs);

        for (Field field : instance.getClass().getDeclaredFields()) {
            String fieldName = field.getName();
            FieldDefinition<T> fieldDefinition = definition.getProxyClassFieldsDefinition().getProxyFields().get(fieldName);

            if (Objects.isNull(fieldDefinition)) {
                throw new FieldNotFoundException("Unable to find field by name:" + fieldName);
            }

            field.set(instance, fieldDefinition.getValue());
        }

        return instance;
    }

    public Class<? extends T> getLoaded() {
        return definition.getGeneratedClazz();
    }
}
