package ru.mike.kirinbytecode.builder.interceptor;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import ru.mike.kirinbytecode.asm.KirinBytecode;
import ru.mike.kirinbytecode.asm.builder.InterceptorImplementation;
import ru.mike.kirinbytecode.asm.exception.notfound.InterceptorImplementationNotFoundException;
import ru.mike.kirinbytecode.asm.util.ElementMatchersUtil;
import ru.mike.kirinbytecode.util.dummy.DummyClassA;

import static ru.mike.kirinbytecode.util.TestConstants.DummyMethod1.DUMMY_METHOD_1_NAME;

public class CommonInterceptorTest {

    @Test
    public void givenDummyClassA_whenRandomInterceptorImplementation_thenThrow() {
        Assertions.assertThrows(
                InterceptorImplementationNotFoundException.class,
                () -> new KirinBytecode()
                        .subclass(DummyClassA.class)
                        .method(ElementMatchersUtil.named(DUMMY_METHOD_1_NAME))
                        .intercept(new InterceptorImplementation() {})
                        .and()
                        .make()
                        .load()
                        .newInstance()
        );
    }
}
