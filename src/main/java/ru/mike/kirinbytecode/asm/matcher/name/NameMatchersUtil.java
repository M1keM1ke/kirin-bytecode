package ru.mike.kirinbytecode.asm.matcher.name;

import org.reflections.Reflections;
import ru.mike.kirinbytecode.asm.exception.NameMatcherInstantiationException;
import ru.mike.kirinbytecode.asm.matcher.NameMatcher;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class NameMatchersUtil {
    public static String LIBRARY_PREFIX = "ru.mike.kirinbytecode";

    public static <T> List<NameMatcher<T>> getNameMatchersOrThrow(String value) {
        try {
            return getNameMatchers(value);
        } catch (NoSuchMethodException | InvocationTargetException | InstantiationException | IllegalAccessException e) {
            throw new NameMatcherInstantiationException(e.getMessage());
        }
    }

    /**
     * Создает через рефлексию инстансы всех классов, которые имплементят {@link NameMatcher}
     * и возвращает их в виде списка. Для сбора имплементящих классов используется {@link Reflections}.
     *
     * @param value имя, по которому будет производиться поиск
     * @return список всех созданных инстансов матчеров, которые имплементят {@link NameMatcher}
     */
    @SuppressWarnings(value = {"rawtypes", "unchecked"})
    public static <T> List<NameMatcher<T>> getNameMatchers(
            String value
    ) throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        Reflections reflections = new Reflections(LIBRARY_PREFIX);
        Set<Class<? extends NameMatcher>> subTypes = reflections.getSubTypesOf(NameMatcher.class);

        List<NameMatcher<T>> nameMatchers = new ArrayList<>();

        for (Class<? extends NameMatcher> clazz : subTypes) {
            nameMatchers.add(clazz.getDeclaredConstructor(String.class).newInstance(value));
        }

        return nameMatchers;
    }
}
