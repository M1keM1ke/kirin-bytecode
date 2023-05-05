package ru.mike.kirinbytecode.builder.interceptor.type;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import ru.mike.kirinbytecode.asm.KirinBytecode;
import ru.mike.kirinbytecode.asm.exception.incorrect.IncorrectModifierException;
import ru.mike.kirinbytecode.asm.exception.incorrect.IncorrectSuperMethodParametersException;
import ru.mike.kirinbytecode.asm.matcher.SuperValue;
import ru.mike.kirinbytecode.util.dummy.DummyClassModifiers;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static ru.mike.kirinbytecode.asm.util.ElementMatchersUtil.named;
import static ru.mike.kirinbytecode.util.TestConstants.DUMMY_PRIVATE_VOID_METHOD_WITH_1_PARAM_NAME;
import static ru.mike.kirinbytecode.util.TestConstants.DUMMY_PUBLIC_FINAL_VOID_METHOD_WITH_1_PARAM_NAME;
import static ru.mike.kirinbytecode.util.TestConstants.DUMMY_PUBLIC_VOID_METHOD_WITH_1_PARAM_NAME;
import static ru.mike.kirinbytecode.util.TestConstants.DUMMY_PUBLIC_VOID_METHOD_WITH_2_PARAM_NAME;
import static ru.mike.kirinbytecode.util.TestConstants.DUMMY_PUBLIC_VOID_METHOD_WITH_3_PARAM_NAME;

public class SuperValueWithParamsInterceptorTest {
    public Random random = new Random();
    
    @Test
    public void givenDummyClassModifiers_whenSuperValueWith1Param_thenOk() throws InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        DummyClassModifiers dummyClassModifiers = new DummyClassModifiers();
        String randomStr = UUID.randomUUID().toString();

        DummyClassModifiers proxy = new KirinBytecode()
                .subclass(DummyClassModifiers.class)
                .method(named(DUMMY_PUBLIC_VOID_METHOD_WITH_1_PARAM_NAME))
//                передаем 1 параметр
                .intercept(SuperValue.callSuper(randomStr))
                .and()
                .make()
                .load()
                .newInstance();

        Assertions.assertEquals(
                dummyClassModifiers.dummyPublicVoidMethodWith1Param(randomStr),
                proxy.dummyPublicVoidMethodWith1Param(randomStr)
        );
    }

    @Test
    public void givenDummyClassModifiers_whenSuperValueWith2Param_thenOk() throws InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        DummyClassModifiers dummyClassModifiers = new DummyClassModifiers();
        String randomStr = UUID.randomUUID().toString();
        int randomInt = random.nextInt(100);

        DummyClassModifiers proxy = new KirinBytecode()
                .subclass(DummyClassModifiers.class)
                .method(named(DUMMY_PUBLIC_VOID_METHOD_WITH_2_PARAM_NAME))
//                передаем 2 параметра
                .intercept(SuperValue.callSuper(randomStr, randomInt))
                .and()
                .make()
                .load()
                .newInstance();

        Assertions.assertEquals(
                dummyClassModifiers.dummyPublicVoidMethodWith2Param(randomStr, randomInt),
                proxy.dummyPublicVoidMethodWith2Param(randomStr, randomInt)
        );
    }

    @Test
    public void givenDummyClassModifiers_whenSuperValueWith3Param_thenOk() throws InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        DummyClassModifiers dummyClassModifiers = new DummyClassModifiers();
        String randomStr = UUID.randomUUID().toString();
        int randomInt = random.nextInt(100);
        List<Object> objects = new ArrayList<>();

        DummyClassModifiers proxy = new KirinBytecode()
                .subclass(DummyClassModifiers.class)
                .method(named(DUMMY_PUBLIC_VOID_METHOD_WITH_3_PARAM_NAME))
//                передаем 3 параметра
                .intercept(SuperValue.callSuper(randomStr, randomInt, objects))
                .and()
                .make()
                .load()
                .newInstance();

        Assertions.assertEquals(
                dummyClassModifiers.dummyPublicVoidMethodWith3Param(randomStr, randomInt, objects),
                proxy.dummyPublicVoidMethodWith3Param(randomStr, randomInt, objects)
        );
    }

    @Test
    void givenDummyClassModifiers_whenFewSuperValueWithParams_thenOk() throws InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        DummyClassModifiers dummyClassModifiers = new DummyClassModifiers();
        String randomStr = UUID.randomUUID().toString();
        int randomInt = random.nextInt(100);

        DummyClassModifiers proxy = new KirinBytecode()
                .subclass(DummyClassModifiers.class)
                .method(named(DUMMY_PUBLIC_VOID_METHOD_WITH_2_PARAM_NAME))
//                передаем 2 параметра
                .intercept(SuperValue.callSuper(randomStr, randomInt))
                .intercept(SuperValue.callSuper(randomStr, randomInt))
                .and()
                .make()
                .load()
                .newInstance();

        assertEquals(
                dummyClassModifiers.dummyPublicVoidMethodWith2Param(randomStr, randomInt),
                proxy.dummyPublicVoidMethodWith2Param(randomStr, randomInt)
        );
    }

    @Test
    public void givenDummyClassModifiers_whenProxyingPrivateMethodBySuperValueWithParams_thenThrow() {
        assertThrows(
                IncorrectModifierException.class,
                () -> new KirinBytecode()
                        .subclass(DummyClassModifiers.class)
                        .method(named(DUMMY_PRIVATE_VOID_METHOD_WITH_1_PARAM_NAME))
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
                        .method(named(DUMMY_PUBLIC_FINAL_VOID_METHOD_WITH_1_PARAM_NAME))
                        .intercept(SuperValue.callSuper())
                        .and()
                        .make()
                        .load()
                        .newInstance());
    }

    @Test
    public void givenDummyClassModifiers_whenTransmittedParamWithAnotherType_thenThrow() {
        int randomInt = random.nextInt(100);

        Assertions.assertThrows(
                IncorrectSuperMethodParametersException.class,
                () -> new KirinBytecode()
                        .subclass(DummyClassModifiers.class)
                        .method(named(DUMMY_PUBLIC_VOID_METHOD_WITH_1_PARAM_NAME))
//              передали другой тип параметра, не как в супер-методе
                        .intercept(SuperValue.callSuper(randomInt))
                        .and()
                        .make()
                        .load()
                        .newInstance()
        );
    }

    @Test
    public void givenDummyClassModifiers_whenChangedParamsOrder_thenThrow() {
        String randomStr = UUID.randomUUID().toString();
        int randomInt = random.nextInt(100);

        Assertions.assertThrows(
                IncorrectSuperMethodParametersException.class,
                () -> new KirinBytecode()
                        .subclass(DummyClassModifiers.class)
                        .method(named(DUMMY_PUBLIC_VOID_METHOD_WITH_2_PARAM_NAME))
//              поменяли местами параметры, теперь они не как в исходном методе
                        .intercept(SuperValue.callSuper(randomInt, randomStr))
                        .and()
                        .make()
                        .load()
                        .newInstance()
        );
    }

    @Test
    public void givenDummyClassModifiers_whenTransmittedAnotherParamsCount_thenThrow() {
        String randomStr = UUID.randomUUID().toString();
        int randomInt = random.nextInt(100);
        List<Object> objects = new ArrayList<>();

        Assertions.assertThrows(
                IncorrectSuperMethodParametersException.class,
                () -> new KirinBytecode()
                        .subclass(DummyClassModifiers.class)
                        .method(named(DUMMY_PUBLIC_VOID_METHOD_WITH_2_PARAM_NAME))
//              изменили количество параметров на 3, а в супер-методе их 2
                        .intercept(SuperValue.callSuper(randomInt, randomStr, objects))
                        .and()
                        .make()
                        .load()
                        .newInstance()
        );
    }
}
