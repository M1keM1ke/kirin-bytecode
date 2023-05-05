package ru.mike.kirinbytecode.builder.interceptor.type;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;
import ru.mike.kirinbytecode.asm.KirinBytecode;
import ru.mike.kirinbytecode.asm.exception.ReturnTypeCastException;
import ru.mike.kirinbytecode.asm.exception.incorrect.IncorrectModifierException;
import ru.mike.kirinbytecode.asm.matcher.FixedValue;
import ru.mike.kirinbytecode.asm.util.ElementMatchersUtil;
import ru.mike.kirinbytecode.util.dummy.DummyClassA;
import ru.mike.kirinbytecode.util.dummy.DummyClassModifiers;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Objects;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static ru.mike.kirinbytecode.asm.util.ElementMatchersUtil.named;
import static ru.mike.kirinbytecode.util.TestConstants.DUMMY_PUBLIC_FINAL_VOID_METHOD_NAME;
import static ru.mike.kirinbytecode.util.TestConstants.DummyMethod1.DUMMY_METHOD_1_NAME;
import static ru.mike.kirinbytecode.util.dummy.DummyUtils.getRandomDummyPrimitiveMethod;
import static ru.mike.kirinbytecode.util.dummy.DummyUtils.getRandomDummyWrapperMethod;

public class FixedValueInterceptorTest {

    @Test
    void givenDummyClassA_whenFixedValue_thenOk() throws InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        String returnValue = UUID.randomUUID().toString();

        DummyClassA proxy = new KirinBytecode()
                .subclass(DummyClassA.class)
                .method(ElementMatchersUtil.named(DUMMY_METHOD_1_NAME))
                .intercept(FixedValue.value(returnValue))
                .and()
                .make()
                .load()
                .newInstance();

        assertEquals(returnValue, proxy.dummyMethod1());
    }

    @Test
    void givenDummyClassA_whenFewFixedValue_thenLastFixedValue() throws InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        String returnValue = UUID.randomUUID().toString();
        String newReturnValue = UUID.randomUUID().toString();

        DummyClassA proxy = new KirinBytecode()
                .subclass(DummyClassA.class)
                .method(ElementMatchersUtil.named(DUMMY_METHOD_1_NAME))
                .intercept(FixedValue.value(returnValue))
                .intercept(FixedValue.value(newReturnValue))
                .and()
                .make()
                .load()
                .newInstance();

        assertEquals(newReturnValue, proxy.dummyMethod1());
    }
    
    @Test
    void givenDummyClassA_whenDifferentOriginalAndFixedValue_thenThrow() {
        Assertions.assertThrows(
                ReturnTypeCastException.class,
                () -> new KirinBytecode()
                        .subclass(DummyClassA.class)
                        .method(ElementMatchersUtil.named(DUMMY_METHOD_1_NAME))
                        .intercept(FixedValue.value(new Object()))
                        .and()
                        .make()
                        .load()
                        .newInstance(null, null)
        );
    }

    @RepeatedTest(5)
    void givenDummyClassA_whenFixedValueIsWrapper_thenOk() throws InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
//      получаем случайный метод с возвращаемым типом-оберткой
        Method randomMethod = getRandomDummyWrapperMethod();
        Object returnedValue = randomMethod.invoke(new DummyClassA());
        String randomMethodName = randomMethod.getName();

        DummyClassA proxy = new KirinBytecode()
                .subclass(DummyClassA.class)
                .method(ElementMatchersUtil.named(randomMethodName))
                .intercept(FixedValue.value(returnedValue))
                .and()
                .make()
                .load()
                .newInstance();

        Method[] proxyMethods = proxy.getClass().getDeclaredMethods();
//      находим метод, который перехватывали
        Method interceptedMethod = Arrays.stream(proxyMethods)
                .filter(m -> Objects.equals(randomMethodName, m.getName()))
                .findFirst()
                .orElseThrow();

        assertEquals(1, proxyMethods.length);
        assertEquals(returnedValue, interceptedMethod.invoke(proxy));
    }

    @Test
    void givenDummyClassA_whenFixedValueIsObject_thenOk() throws InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        Object returnedValue = new Object();

        DummyClassA proxy = new KirinBytecode()
                .subclass(DummyClassA.class)
                .method(ElementMatchersUtil.named("dummyWrapperMethodObject"))
                .intercept(FixedValue.value(returnedValue))
                .and()
                .make()
                .load()
                .newInstance();

        Method[] proxyMethods = proxy.getClass().getDeclaredMethods();

        assertEquals(1, proxyMethods.length);
        assertEquals(returnedValue, proxy.dummyWrapperMethodObject());
    }

    @RepeatedTest(5)
    void givenDummyClassA_whenReturnMethodTypeIsPrimitive_thenOk() throws InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
//      получаем случайный метод с возвращаемым типом-примитивом
        Method randomMethod = getRandomDummyPrimitiveMethod();
        Object returnedValue = randomMethod.invoke(new DummyClassA());
        String randomMethodName = randomMethod.getName();

        DummyClassA proxy = new KirinBytecode()
                .subclass(DummyClassA.class)
                .method(ElementMatchersUtil.named(randomMethodName))
                .intercept(FixedValue.value(returnedValue))
                .and()
                .make()
                .load()
                .newInstance();

        Method[] proxyMethods = proxy.getClass().getDeclaredMethods();
//      находим метод, который перехватывали
        Method interceptedMethod = Arrays.stream(proxyMethods)
                .filter(m -> Objects.equals(randomMethodName, m.getName()))
                .findFirst()
                .orElseThrow();

        assertEquals(1, proxyMethods.length);
        assertEquals(returnedValue, interceptedMethod.invoke(proxy));
    }

    @Test
    public void givenDummyClassModifiers_whenProxyingFinalMethodByFixedValue_thenThrow() {
        assertThrows(
                IncorrectModifierException.class,
                () -> new KirinBytecode()
                        .subclass(DummyClassModifiers.class)
                        .method(named(DUMMY_PUBLIC_FINAL_VOID_METHOD_NAME))
                        .intercept(FixedValue.value())
                        .and()
                        .make()
                        .load()
                        .newInstance());
    }
}
