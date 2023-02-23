package ru.mike.kirinbytecode.asm.matcher.name;

import ru.mike.kirinbytecode.asm.matcher.MatcherMode;
import ru.mike.kirinbytecode.asm.matcher.NameMatcher;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class FullyEqualsNameMatcher<T> extends NameMatcher<T> {

    /**
     * Дефолтный конструктор, необходим для работы создания инстанса через рефлексию в
     * {@link ru.mike.kirinbytecode.asm.matcher.name.NameMatchersUtil}
     */
    public FullyEqualsNameMatcher() {}

    public FullyEqualsNameMatcher(String value, MatcherMode mode) {
        super(value, mode);
    }

    @Override
    public <R> boolean isCurrentNameMatcher(NameMatcher<R> nameMatcher) {
        return nameMatcher instanceof FullyEqualsNameMatcher;
    }

    @Override
    public List<Method> findSuitableMethods(List<Method> methods) {
        List<Method> suitableMethods = new ArrayList<>();

        for (Method method : methods) {
            String methodName = method.getName();

            if (Objects.equals(methodName, getValue())) {
                suitableMethods.add(method);
            }
        }

        return suitableMethods;
    }


}
