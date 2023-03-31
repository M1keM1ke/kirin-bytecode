package ru.mike.kirinbytecode.builder;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import ru.mike.kirinbytecode.asm.KirinBytecode;
import ru.mike.kirinbytecode.asm.matcher.FixedValue;
import ru.mike.kirinbytecode.asm.matcher.SuperValue;
import ru.mike.kirinbytecode.asm.util.ElementMatchersUtil;
import ru.mike.kirinbytecode.util.DummyClassA;

import java.lang.reflect.InvocationTargetException;
import java.util.UUID;

import static ru.mike.kirinbytecode.util.TestConstants.DummyMethod1.DUMMY_METHOD_1_NAME;

public class ProxyInterceptorsBuilderTest {

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
                .newInstance(null, null);

        Assertions.assertEquals(returnValue, proxy.dummyMethod1());
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
                .newInstance(null, null);

        Assertions.assertEquals(newReturnValue, proxy.dummyMethod1());
    }

    @Test
    void givenDummyClassA_whenSuperValue_thenOk() throws InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        DummyClassA dummyClassA = new DummyClassA();

        DummyClassA proxy = new KirinBytecode()
                .subclass(DummyClassA.class)
                .method(ElementMatchersUtil.named(DUMMY_METHOD_1_NAME))
                .intercept(SuperValue.value())
                .and()
                .make()
                .load()
                .newInstance(null, null);

        Assertions.assertEquals(dummyClassA.dummyMethod1(), proxy.dummyMethod1());
    }

    @Test
    void givenDummyClassA_whenFewSuperValue_thenOk() throws InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        DummyClassA dummyClassA = new DummyClassA();

        DummyClassA proxy = new KirinBytecode()
                .subclass(DummyClassA.class)
                .method(ElementMatchersUtil.named(DUMMY_METHOD_1_NAME))
                .intercept(SuperValue.value())
                .intercept(SuperValue.value())
                .and()
                .make()
                .load()
                .newInstance(null, null);

        Assertions.assertEquals(dummyClassA.dummyMethod1(), proxy.dummyMethod1());
    }
}
