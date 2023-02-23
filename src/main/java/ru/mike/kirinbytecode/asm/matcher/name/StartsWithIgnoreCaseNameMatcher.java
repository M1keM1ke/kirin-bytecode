package ru.mike.kirinbytecode.asm.matcher.name;

import ru.mike.kirinbytecode.asm.matcher.MatcherMode;
import ru.mike.kirinbytecode.asm.matcher.NameMatcher;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class StartsWithIgnoreCaseNameMatcher<T> extends NameMatcher<T> {

    /**
     * Дефолтный конструктор, необходим для работы создания инстанса через рефлексию в
     * {@link ru.mike.kirinbytecode.asm.matcher.name.NameMatchersUtil}
     */
    public StartsWithIgnoreCaseNameMatcher() {}

    public StartsWithIgnoreCaseNameMatcher(String value, MatcherMode mode) {
        super(value, mode);
    }

    @Override
    public <R> boolean isCurrentNameMatcher(NameMatcher<R> nameMatcher) {
        return nameMatcher instanceof StartsWithIgnoreCaseNameMatcher;
    }

    @Override
    public List<Method> findSuitableMethods(List<Method> methods) {
        List<Method> suitableMethods = new ArrayList<>();

        for (Method method : methods) {
            String methodName = method.getName();

            if (methodName.toLowerCase().startsWith(getValue().toLowerCase())) {
                suitableMethods.add(method);
            }
        }

        return suitableMethods;
    }
}
