package ru.mike.kirinbytecode.asm.util;

import ru.mike.kirinbytecode.asm.matcher.NameMatcher;
import ru.mike.kirinbytecode.asm.matcher.name.ContainsIgnoreCaseNameMatcher;
import ru.mike.kirinbytecode.asm.matcher.name.ContainsNameMatcher;
import ru.mike.kirinbytecode.asm.matcher.name.EndsWithIgnoreCaseNameMatcher;
import ru.mike.kirinbytecode.asm.matcher.name.EndsWithNameMatcher;
import ru.mike.kirinbytecode.asm.matcher.name.FullyEqualsIgnoreCaseNameMatcher;
import ru.mike.kirinbytecode.asm.matcher.name.FullyEqualsNameMatcher;
import ru.mike.kirinbytecode.asm.matcher.name.StartsWithIgnoreCaseNameMatcher;
import ru.mike.kirinbytecode.asm.matcher.name.StartsWithNameMatcher;

public class ElementMatchersUtil {

    public static <T> NameMatcher<T> named(String name) {
        return new FullyEqualsNameMatcher<T>(name);
    }

    public static <T> NameMatcher<T> namedIgnoreCase(String name) {
        return new FullyEqualsIgnoreCaseNameMatcher<>(name);
    }

    public static <T> NameMatcher<T> nameStartsWith(String name) {
        return new StartsWithNameMatcher<>(name);
    }

    public static <T> NameMatcher<T> nameStartsWithIgnoreCase(String name) {
        return new StartsWithIgnoreCaseNameMatcher<>(name);
    }

    public static <T> NameMatcher<T> nameEndsWith(String name) {
        return new EndsWithNameMatcher<>(name);
    }

    public static <T> NameMatcher<T> nameEndsWithIgnoreCase(String name) {
        return new EndsWithIgnoreCaseNameMatcher<>(name);
    }

    public static <T> NameMatcher<T> nameContains(String name) {
        return new ContainsNameMatcher<>(name);
    }

    public static <T> NameMatcher<T> nameContainsIgnoreCase(String name) {
        return new ContainsIgnoreCaseNameMatcher<>(name);
    }
}
