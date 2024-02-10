package ru.mike.kirinbytecode.asm.matcher;

import lombok.Getter;
import ru.mike.kirinbytecode.asm.builder.InterceptorImplementation;

import java.util.function.Supplier;

@Getter
public class CustomValue<T> implements InterceptorImplementation {
    private Supplier<T> supp;
    private Class<T> returnType;

    public CustomValue(Class<T> returnType, Supplier<T> supp) {
        this.supp = supp;
        this.returnType = returnType;
    }

    /**
     * Принимает {@link Supplier}, код которого будет встроен между фазами
     * {@link ru.mike.kirinbytecode.asm.definition.BeforeMethodDefinition} и
     * {@link ru.mike.kirinbytecode.asm.definition.AfterMethodDefinition},
     * если используется {@link ru.mike.kirinbytecode.asm.builder.SubclassDynamicTypeBuilder#method(NameMatcher)}.
     * Тип возвращаемого значения метода в оригинальном классе должен совпадать с
     * типом {@link T}, иначе возникнет исключение
     *
     * @param supp
     * @param <T>
     * @return
     */
    public static <T> InterceptorImplementation value(Class<T> returnType, Supplier<T> supp) {
        return new CustomValue<>(returnType, supp);
    }
}
