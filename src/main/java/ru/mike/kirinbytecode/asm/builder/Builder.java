package ru.mike.kirinbytecode.asm.builder;

import ru.mike.kirinbytecode.asm.builder.field.FieldDefinitionBuilder;
import ru.mike.kirinbytecode.asm.builder.method.AnnotationDefinition;
import ru.mike.kirinbytecode.asm.builder.method.MethodDefinitionBuilder;
import ru.mike.kirinbytecode.asm.builder.method.MethodInterceptionBuilder;
import ru.mike.kirinbytecode.asm.builder.method.MethodInterceptionStagesBuilder;
import ru.mike.kirinbytecode.asm.matcher.NameMatcher;

import javax.annotation.Nullable;
import java.lang.reflect.Type;

public interface Builder<T> {

    /**
     * Выбор метода из родительского класса, который нужно проксировать
     *
     * @param nameMatcher матчер, с помощью которого определяется нужный метод
     * @return {@link MethodInterceptionBuilder <T>} для проксирования метода
     */
    MethodInterceptionStagesBuilder<T> method(NameMatcher<T> nameMatcher);

    /**
     * Описывает создание нового метода, а не проксирование метода родительского класса
     *
     * @param name имя метода
     * @param returnType возвращаемое значение
     * @param modifiers список модификаторов из {@link org.objectweb.asm.Opcodes}
     */
    MethodDefinitionBuilder<T> defineMethod(String name, Class<?> returnType, int modifiers);

    /**
     * Описывает поле, которое создастся в прокси классе. Полю можно присвоить значение
     * (см. {@link FieldDefinitionBuilder#value(Object)}.
     * Значение описываемого поля должно совпадать с его значением в {@link FieldDefinitionBuilder#value(Object)}.
     *
     * @param name имя поля
     * @param type тип поля
     * @param modifiers список модификаторов из {@link org.objectweb.asm.Opcodes}, необязательный параметр.
     * Если нет модификаторов, то по умолчанию присваивается модификатор {@link org.objectweb.asm.Opcodes#ACC_PUBLIC}
     * @return {@link FieldDefinitionBuilder<T>} для описания значения
     */
    FieldDefinitionBuilder<T> defineField(String name, Type type, @Nullable Integer...modifiers);

    /**
     * Генерирует прокси класс на основе настроек, собранных на более ранних этапах в
     * {@link ru.mike.kirinbytecode.asm.definition.proxy.ProxyClassDefinition}
     *
     * @return сгенерированный, но не загруженный класслоадером класс
     */
    UnloadedType<T> make();

    Builder<T> implement(Type type);

    /**
     * Устанавливает имя прокси класса, создавая генератор имени прокси
     * {@link ru.mike.kirinbytecode.asm.generator.name.ConstantProxyClassNameGenerator}
     *
     * @param name имя прокси класса
     * @return {@link Builder<T>}
     */
    Builder<T> name(String name);

    Builder<T> annotateType(AnnotationDefinition annotationDefinition);
}
