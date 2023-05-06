package ru.mike.kirinbytecode.asm.matcher.name;

import ru.mike.kirinbytecode.asm.matcher.NameMatcher;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class ContainsIgnoreCaseNameMatcher<T> extends NameMatcher<T> {

    /**
     * Дефолтный конструктор, необходим для работы создания инстанса через рефлексию в
     * {@link ru.mike.kirinbytecode.asm.matcher.name.NameMatchersUtil}
     */
    public ContainsIgnoreCaseNameMatcher() {}

    public ContainsIgnoreCaseNameMatcher(String value) {
        super(value);
    }

    @Override
    public <R> boolean isCurrentNameMatcher(NameMatcher<R> nameMatcher) {
        return nameMatcher instanceof ContainsIgnoreCaseNameMatcher;
    }

    @Override
    public List<Method> findSuitableMethods(List<Method> methods) {
        List<Method> suitableMethods = new ArrayList<>();

        for (Method method : methods) {
            String methodName = method.getName();

            if (methodName.toLowerCase().contains(getValue().toLowerCase())) {
                suitableMethods.add(method);
            }
        }

        return suitableMethods;
    }
}
