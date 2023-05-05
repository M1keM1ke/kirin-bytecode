package ru.mike.kirinbytecode.matcher;

import org.junit.jupiter.api.Test;
import ru.mike.kirinbytecode.asm.KirinBytecode;
import ru.mike.kirinbytecode.asm.matcher.FixedValue;
import ru.mike.kirinbytecode.util.dummy.DummyClassA;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static ru.mike.kirinbytecode.asm.util.ElementMatchersUtil.nameEndsWith;
import static ru.mike.kirinbytecode.asm.util.ElementMatchersUtil.nameEndsWithIgnoreCase;
import static ru.mike.kirinbytecode.asm.util.ElementMatchersUtil.nameStartsWith;
import static ru.mike.kirinbytecode.asm.util.ElementMatchersUtil.nameStartsWithIgnoreCase;
import static ru.mike.kirinbytecode.asm.util.ElementMatchersUtil.named;
import static ru.mike.kirinbytecode.asm.util.ElementMatchersUtil.namedIgnoreCase;
import static ru.mike.kirinbytecode.util.TestConstants.DUMMY_METHOD_CHARACTER_PROXY_RETURN_VALUE;
import static ru.mike.kirinbytecode.util.TestConstants.DUMMY_METHOD_DEFAULT_PROXY_RETURN_VALUE;
import static ru.mike.kirinbytecode.util.TestConstants.DUMMY_METHOD_NAME_ENDS_WITH;
import static ru.mike.kirinbytecode.util.TestConstants.DUMMY_METHOD_NAME_ENDS_WITH_IGNORE_CASE;
import static ru.mike.kirinbytecode.util.TestConstants.DUMMY_METHOD_NAME_STARTS_WITH;
import static ru.mike.kirinbytecode.util.TestConstants.DUMMY_METHOD_NAME_STARTS_WITH_IGNORE_CASE;
import static ru.mike.kirinbytecode.util.TestConstants.DummyMethod1.DUMMY_METHOD_1_NAME;
import static ru.mike.kirinbytecode.util.TestConstants.DummyMethod1.DUMMY_METHOD_1_NAME_EQUALS_IGNORE_CASE;
import static ru.mike.kirinbytecode.util.TestConstants.DummyMethod1.DUMMY_METHOD_1_PROXY_RETURN_VALUE;

public class NameMatcherTest {

    @Test
    public void givenDummyClassA_whenProxyingMethodByFullyEqualsNameMatcher_thenOk() throws InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        String proxyMethodReturnValue = DUMMY_METHOD_1_PROXY_RETURN_VALUE;
        Predicate<Method> methodsFilterPredicate = f -> f.getName().equals(DUMMY_METHOD_1_NAME);

        DummyClassA proxy = new KirinBytecode()
                    .subclass(DummyClassA.class)
                    .method(named(DUMMY_METHOD_1_NAME))
                    .intercept(FixedValue.value(proxyMethodReturnValue))
                .and()
                    .make()
                    .load()
                    .newInstance();


        Method[] declaredMethods = proxy.getClass().getDeclaredMethods();

        assertEquals(1, declaredMethods.length);
        assertEquals(DUMMY_METHOD_1_NAME, declaredMethods[0].getName());
        assertEquals(proxyMethodReturnValue, proxy.dummyMethod1());

        assertProxyingMethodsByMatcher(methodsFilterPredicate, proxy, proxyMethodReturnValue);
    }

    @Test
    public void givenDummyClassA_whenProxyingMethodByFullyEqualsIgnoreCaseNameMatcher_thenOk() throws InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        String proxyMethodReturnValue = DUMMY_METHOD_1_PROXY_RETURN_VALUE;
        Predicate<Method> methodsFilterPredicate = f -> f.getName().equalsIgnoreCase(DUMMY_METHOD_1_NAME_EQUALS_IGNORE_CASE);

        DummyClassA proxy = new KirinBytecode()
                    .subclass(DummyClassA.class)
                    .method(namedIgnoreCase(DUMMY_METHOD_1_NAME_EQUALS_IGNORE_CASE))
                    .intercept(FixedValue.value(proxyMethodReturnValue))
                .and()
                    .make()
                    .load()
                    .newInstance();


        Method[] declaredMethods = proxy.getClass().getDeclaredMethods();

        assertEquals(1, declaredMethods.length);
        assertEquals(DUMMY_METHOD_1_NAME, declaredMethods[0].getName());
        assertEquals(proxyMethodReturnValue, proxy.dummyMethod1());

        assertProxyingMethodsByMatcher(methodsFilterPredicate, proxy, proxyMethodReturnValue);
    }

    @Test
    public void givenDummyClassA_whenProxyingMethodByStartsWithNameMatcher_thenOk() throws InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        String proxyMethodReturnValue = DUMMY_METHOD_DEFAULT_PROXY_RETURN_VALUE;

        Predicate<Method> methodsFilterPredicate = f -> f.getName().startsWith(DUMMY_METHOD_NAME_STARTS_WITH);

        DummyClassA proxy = new KirinBytecode()
                    .subclass(DummyClassA.class)
                    .method(nameStartsWith(DUMMY_METHOD_NAME_STARTS_WITH))
                    .intercept(FixedValue.value(proxyMethodReturnValue))
                .and()
                    .make()
                    .load()
                    .newInstance();

        assertProxyingMethodsByMatcher(methodsFilterPredicate, proxy, proxyMethodReturnValue);
    }

    @Test
    public void givenDummyClassA_whenProxyingMethodByStartsWithIgnoreCaseNameMatcher_thenOk() throws InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        String proxyMethodReturnValue = DUMMY_METHOD_DEFAULT_PROXY_RETURN_VALUE;
        Predicate<Method> methodsFilterPredicate = f -> f.getName().toLowerCase().startsWith(DUMMY_METHOD_NAME_STARTS_WITH_IGNORE_CASE.toLowerCase());

        DummyClassA proxy = new KirinBytecode()
                    .subclass(DummyClassA.class)
                    .method(nameStartsWithIgnoreCase(DUMMY_METHOD_NAME_STARTS_WITH_IGNORE_CASE))
                    .intercept(FixedValue.value(proxyMethodReturnValue))
                .and()
                    .make()
                    .load()
                    .newInstance();

        assertProxyingMethodsByMatcher(methodsFilterPredicate, proxy, proxyMethodReturnValue);
    }

    @Test
    public void givenDummyClassA_whenProxyingMethodByEndsWithNameMatcher_thenOk() throws InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        Character proxyMethodReturnValue = DUMMY_METHOD_CHARACTER_PROXY_RETURN_VALUE;
        Predicate<Method> methodsFilterPredicate = f -> f.getName().endsWith(DUMMY_METHOD_NAME_ENDS_WITH);

        DummyClassA proxy = new KirinBytecode()
                    .subclass(DummyClassA.class)
                    .method(nameEndsWith(DUMMY_METHOD_NAME_ENDS_WITH))
                    .intercept(FixedValue.value(proxyMethodReturnValue))
                .and()
                    .make()
                    .load()
                    .newInstance();

        assertProxyingMethodsByMatcher(methodsFilterPredicate, proxy, proxyMethodReturnValue);
    }

    @Test
    public void givenDummyClassA_whenProxyingMethodByEndsWithIgnoreCaseNameMatcher_thenOk() throws InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        Character proxyMethodReturnValue = DUMMY_METHOD_CHARACTER_PROXY_RETURN_VALUE;
        Predicate<Method> methodsFilterPredicate = f -> f.getName().toLowerCase().endsWith(DUMMY_METHOD_NAME_ENDS_WITH_IGNORE_CASE.toLowerCase());

        DummyClassA proxy = new KirinBytecode()
                .subclass(DummyClassA.class)
                .method(nameEndsWithIgnoreCase(DUMMY_METHOD_NAME_ENDS_WITH_IGNORE_CASE))
                .intercept(FixedValue.value(proxyMethodReturnValue))
                .and()
                .make()
                .load()
                .newInstance();


        assertProxyingMethodsByMatcher(methodsFilterPredicate, proxy, proxyMethodReturnValue);
    }

    private void assertProxyingMethodsByMatcher(Predicate<Method> methodsFilterPredicate, DummyClassA proxy, Object proxyingMethodsReturnValue) throws IllegalAccessException, InvocationTargetException {
        List<Method> dummyClassAMethods = Arrays.stream(DummyClassA.class.getDeclaredMethods())
                .filter(methodsFilterPredicate)
                .collect(Collectors.toList());

        Method[] proxyDeclaredMethods = proxy.getClass().getDeclaredMethods();

        assertEquals(dummyClassAMethods.size(), proxyDeclaredMethods.length);

        for (Method method : proxyDeclaredMethods) {
            assertEquals(proxyingMethodsReturnValue, method.invoke(proxy));
        }
    }
}
