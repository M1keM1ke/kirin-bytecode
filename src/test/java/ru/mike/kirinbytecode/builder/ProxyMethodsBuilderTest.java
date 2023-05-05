package ru.mike.kirinbytecode.builder;

import org.junit.jupiter.api.Test;
import ru.mike.kirinbytecode.asm.KirinBytecode;
import ru.mike.kirinbytecode.asm.exception.notfound.MethodNotFoundException;
import ru.mike.kirinbytecode.asm.matcher.FixedValue;
import ru.mike.kirinbytecode.util.dummy.DummyClassA;

import java.lang.reflect.InvocationTargetException;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static ru.mike.kirinbytecode.asm.util.ElementMatchersUtil.named;
import static ru.mike.kirinbytecode.util.TestConstants.DUMMY_METHOD_DEFAULT_PROXY_RETURN_VALUE;
import static ru.mike.kirinbytecode.util.TestConstants.DummyMethod1.DUMMY_METHOD_1_NAME;
import static ru.mike.kirinbytecode.util.TestConstants.DummyMethod1.DUMMY_METHOD_1_PROXY_RETURN_VALUE;
import static ru.mike.kirinbytecode.util.TestConstants.DummyMethod2.DUMMY_METHOD_2_NAME;
import static ru.mike.kirinbytecode.util.TestConstants.DummyMethod2.DUMMY_METHOD_2_PROXY_NAME_RETURN_VALUE;

public class ProxyMethodsBuilderTest {

    @Test
    public void givenDummyClassA_whenProxyingNonexistentMethod_thenThrow() {
        String randomMethodName = UUID.randomUUID().toString();

        assertThrows(
                MethodNotFoundException.class,
                () -> new KirinBytecode()
                            .subclass(DummyClassA.class)
                            .method(named(randomMethodName))
                            .intercept(FixedValue.value(DUMMY_METHOD_DEFAULT_PROXY_RETURN_VALUE))
                        .and()
                            .make()
                            .load()
                            .newInstance(null, null)
        );
    }

    @Test
    public void givenDummyClassA_whenProxyingMethod_thenOk() throws InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        String proxyMethodReturnValue = DUMMY_METHOD_1_PROXY_RETURN_VALUE;

        DummyClassA proxy = new KirinBytecode()
                    .subclass(DummyClassA.class)
                    .method(named(DUMMY_METHOD_1_NAME))
                    .intercept(FixedValue.value(proxyMethodReturnValue))
                .and()
                    .make()
                    .load()
                    .newInstance();


        assertEquals(1, proxy.getClass().getDeclaredMethods().length);
        assertEquals(proxyMethodReturnValue, proxy.dummyMethod1());
    }

    @Test
    public void givenDummyClassA_whenProxyingFewDifferentMethods_thenOk() throws InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        String proxyMethod1ReturnValue = DUMMY_METHOD_1_PROXY_RETURN_VALUE;
        String proxyMethod2ReturnValue = DUMMY_METHOD_2_PROXY_NAME_RETURN_VALUE;

        DummyClassA proxy = new KirinBytecode()
                    .subclass(DummyClassA.class)
                    .method(named(DUMMY_METHOD_1_NAME))
                    .intercept(FixedValue.value(proxyMethod1ReturnValue))
                .and()
                    .method(named(DUMMY_METHOD_2_NAME))
                    .intercept(FixedValue.value(proxyMethod2ReturnValue))
                .and()
                    .make()
                    .load()
                    .newInstance();


        assertEquals(2, proxy.getClass().getDeclaredMethods().length);
        assertEquals(proxyMethod1ReturnValue, proxy.dummyMethod1());
        assertEquals(proxyMethod2ReturnValue, proxy.dummyMethod2());
    }

    @Test
    public void givenDummyClassA_whenProxyingFewEqualsMethods_thenThrow() {
        String proxyMethodName = DUMMY_METHOD_1_NAME;
        String proxyMethod1ReturnValue = DUMMY_METHOD_1_PROXY_RETURN_VALUE;

        assertThrows(
                ClassFormatError.class,
                () -> new KirinBytecode()
                        .subclass(DummyClassA.class)
                        .method(named(proxyMethodName))
                        .intercept(FixedValue.value(proxyMethod1ReturnValue))
                    .and()
                        .method(named(proxyMethodName))
                        .intercept(FixedValue.value(proxyMethod1ReturnValue))
                    .and()
                        .make()
                        .load()
                        .newInstance(null, null)
        );
    }
}
