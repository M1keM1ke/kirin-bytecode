package ru.mike.kirinbytecode.builder.interceptor.type;

import org.junit.jupiter.api.Test;
import ru.mike.kirinbytecode.asm.KirinBytecode;
import ru.mike.kirinbytecode.asm.exception.incorrect.IncorrectModifierException;
import ru.mike.kirinbytecode.asm.matcher.SuperValue;
import ru.mike.kirinbytecode.asm.util.ElementMatchersUtil;
import ru.mike.kirinbytecode.util.dummy.DummyClassA;
import ru.mike.kirinbytecode.util.dummy.DummyClassModifiers;

import java.lang.reflect.InvocationTargetException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static ru.mike.kirinbytecode.asm.util.ElementMatchersUtil.named;
import static ru.mike.kirinbytecode.util.TestConstants.DUMMY_PRIVATE_VOID_METHOD_NAME;
import static ru.mike.kirinbytecode.util.TestConstants.DUMMY_PUBLIC_FINAL_VOID_METHOD_NAME;
import static ru.mike.kirinbytecode.util.TestConstants.DummyMethod1.DUMMY_METHOD_1_NAME;

public class SuperValueWithoutParamsInterceptorTest {

    @Test
    void givenDummyClassA_whenSuperValue_thenOk() throws InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        DummyClassA dummyClassA = new DummyClassA();

        DummyClassA proxy = new KirinBytecode()
                .subclass(DummyClassA.class)
                .method(ElementMatchersUtil.named(DUMMY_METHOD_1_NAME))
                .intercept(SuperValue.callSuper())
                .and()
                .make()
                .load()
                .newInstance();

        assertEquals(dummyClassA.dummyMethod1(), proxy.dummyMethod1());
    }

    @Test
    void givenDummyClassA_whenFewSuperValue_thenOk() throws InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        DummyClassA dummyClassA = new DummyClassA();

        DummyClassA proxy = new KirinBytecode()
                .subclass(DummyClassA.class)
                .method(ElementMatchersUtil.named(DUMMY_METHOD_1_NAME))
                .intercept(SuperValue.callSuper())
                .intercept(SuperValue.callSuper())
                .and()
                .make()
                .load()
                .newInstance();

        assertEquals(dummyClassA.dummyMethod1(), proxy.dummyMethod1());
    }

    @Test
    public void givenDummyClassModifiers_whenProxyingPrivateMethodBySuperValue_thenThrow() {
        assertThrows(
                IncorrectModifierException.class,
                () -> new KirinBytecode()
                        .subclass(DummyClassModifiers.class)
                        .method(named(DUMMY_PRIVATE_VOID_METHOD_NAME))
                        .intercept(SuperValue.callSuper())
                        .and()
                        .make()
                        .load()
                        .newInstance());
    }

    @Test
    public void givenDummyClassModifiers_whenProxyingFinalMethodBySuperValue_thenThrow() {
        assertThrows(
                IncorrectModifierException.class,
                () -> new KirinBytecode()
                        .subclass(DummyClassModifiers.class)
                        .method(named(DUMMY_PUBLIC_FINAL_VOID_METHOD_NAME))
                        .intercept(SuperValue.callSuper())
                        .and()
                        .make()
                        .load()
                        .newInstance());
    }
}
