package ru.mike.kirinbytecode.builder.interceptor.stage;

import org.junit.jupiter.api.Test;
import ru.mike.kirinbytecode.asm.KirinBytecode;
import ru.mike.kirinbytecode.asm.matcher.FixedValue;
import ru.mike.kirinbytecode.util.dummy.DummyClassA;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static ru.mike.kirinbytecode.asm.util.ElementMatchersUtil.named;
import static ru.mike.kirinbytecode.util.TestConstants.DummyMethod1.DUMMY_METHOD_1_NAME;
import static ru.mike.kirinbytecode.util.TestConstants.DummyMethod1.DUMMY_METHOD_1_PROXY_RETURN_VALUE;

public class InterceptorAfterStageTest {

    @Test
    public void givenDummyClassA_whenProxyingMethodWithAfter_thenOk() throws InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        List<Integer> numbers = new ArrayList<>();

        DummyClassA proxy = new KirinBytecode()
                .subclass(DummyClassA.class)
                .method(named(DUMMY_METHOD_1_NAME))
                .intercept(FixedValue.value(DUMMY_METHOD_1_PROXY_RETURN_VALUE))
                .after(() -> numbers.add(1))
                .and()
                .make()
                .load()
                .newInstance();

        assertEquals(0, numbers.size());
        assertEquals(1, proxy.getClass().getDeclaredMethods().length);
        assertEquals(1, proxy.getClass().getDeclaredFields().length);

        Field field = proxy.getClass().getDeclaredFields()[0];

        assertEquals(Runnable.class, field.getType());

//      получаем значение сгенерированного поля из proxy
        Runnable generatedFieldValue = (Runnable) field.get(proxy);

//      при запуске выполнится код в after секции, то есть добавится число в список
        generatedFieldValue.run();

        assertEquals(1, numbers.size());

//      при запуске выполнится код в проксируемом методе и в after секции, то есть добавится число в список
        proxy.dummyMethod1();
        assertEquals(2, numbers.size());
    }

    @Test
    public void givenDummyClassA_whenProxyingMethodWithFewAfter_thenExecuteOnce() throws InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        List<Integer> numbers = new ArrayList<>();

        DummyClassA proxy = new KirinBytecode()
                .subclass(DummyClassA.class)
                .method(named(DUMMY_METHOD_1_NAME))
                .intercept(FixedValue.value(DUMMY_METHOD_1_PROXY_RETURN_VALUE))
                .after(() -> numbers.add(1))
                .after(() -> numbers.add(1))
                .and()
                .make()
                .load()
                .newInstance();

        assertEquals(0, numbers.size());
        assertEquals(1, proxy.getClass().getDeclaredMethods().length);
        assertEquals(1, proxy.getClass().getDeclaredFields().length);

        Field field = proxy.getClass().getDeclaredFields()[0];

        assertEquals(Runnable.class, field.getType());

//      получаем значение сгенерированного поля из proxy
        Runnable generatedFieldValue = (Runnable) field.get(proxy);

//      при запуске выполнится код в after секции, то есть добавится число в список
        generatedFieldValue.run();

        assertEquals(1, numbers.size());

//      при запуске выполнится код в проксируемом методе и в after секции, то есть добавится число в список единожды
        proxy.dummyMethod1();
        assertEquals(2, numbers.size());
    }
}
