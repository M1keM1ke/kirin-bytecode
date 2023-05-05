package ru.mike.kirinbytecode.asm.matcher;

import lombok.Getter;
import ru.mike.kirinbytecode.asm.builder.InterceptorImplementation;
import ru.mike.kirinbytecode.asm.exception.incorrect.IncorrectSuperMethodParametersException;

import javax.annotation.Nonnull;

@Getter
public class SuperValue implements InterceptorImplementation {
    private Object[] params;

    public SuperValue() {
    }

    public SuperValue(Object...params) {
        this.params = params;
    }

    public static InterceptorImplementation callSuper() {
        return new SuperValue();
    }

    /**
     * При генерации прокси метода вызывает супер-метод с параметрами, переданными в данном методе.
     * Важно соблюдать типы и порядок передаваемых параметров, иначе при генерации возникнет исключение
     * {@link IncorrectSuperMethodParametersException}
     *
     * @param params параметры, необходимые для вызова супер-метода прокси метода
     * @return {@link SuperValue}
     */
    public static InterceptorImplementation callSuper(@Nonnull Object...params) {
        return new SuperValue(params);
    }

}
