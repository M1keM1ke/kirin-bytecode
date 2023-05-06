package ru.mike.kirinbytecode.asm.matcher.name;

import ru.mike.kirinbytecode.asm.matcher.NameMatcher;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class StartsWithNameMatcher<T> extends NameMatcher<T> {

    /**
     * Дефолтный конструктор, необходим для работы создания инстанса через рефлексию в
     * {@link ru.mike.kirinbytecode.asm.matcher.name.NameMatchersUtil}
     */
    public StartsWithNameMatcher() {}

    public StartsWithNameMatcher(String value) {
        super(value);
    }

    @Override
    public <R> boolean isCurrentNameMatcher(NameMatcher<R> nameMatcher) {
        return nameMatcher instanceof StartsWithNameMatcher;
    }

    @Override
    public List<Method> findSuitableMethods(List<Method> methods) {
        List<Method> suitableMethods = new ArrayList<>();

        for (Method method : methods) {
            String methodName = method.getName();

            if (methodName.startsWith(getValue())) {
                suitableMethods.add(method);
            }
        }

        return suitableMethods;
    }
}
