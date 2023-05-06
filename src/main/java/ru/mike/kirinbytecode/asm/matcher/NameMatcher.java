package ru.mike.kirinbytecode.asm.matcher;

import java.lang.reflect.Method;
import java.util.List;

public abstract class NameMatcher<T> {
    private String value;

    /**
     * Дефолтный конструктор, необходим для работы создания инстанса через рефлексию в
     * {@link ru.mike.kirinbytecode.asm.matcher.name.NameMatchersUtil}
     */
    public NameMatcher() {}

    public NameMatcher(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public abstract <R> boolean isCurrentNameMatcher(NameMatcher<R> nameMatcher);

    public abstract List<Method> findSuitableMethods(List<Method> methods);


}
