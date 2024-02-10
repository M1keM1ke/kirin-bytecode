package ru.mike.kirinbytecode.asm.builder.field;

import ru.mike.kirinbytecode.asm.builder.SubclassDynamicTypeBuilder;
import ru.mike.kirinbytecode.asm.builder.other.Junction;

import javax.annotation.Nullable;
import java.lang.reflect.Type;

public interface FieldDefinitionBuilder<T> extends Junction<T> {

    /**
     * Присваивает значение полю, созданному в
     * {@link ru.mike.kirinbytecode.asm.builder.Builder#defineField(String, Type, Integer...)}.
     * Тип значения в {@link FieldDefinitionBuilder#value(Object)} должен совпадать с типом поля,
     * созданного в {@link ru.mike.kirinbytecode.asm.builder.Builder#defineField(String, Type, Integer...)}
     *
     * @param value значение поля
     * @return {@link SubclassDynamicTypeBuilder<T>}
     */
    FieldDefinitionBuilder<T> value(@Nullable Object value);


}