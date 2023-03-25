package ru.mike.kirinbytecode.builder;

import org.junit.jupiter.api.Test;
import ru.mike.kirinbytecode.asm.KirinBytecode;
import ru.mike.kirinbytecode.util.DummyClassA;

import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static ru.mike.kirinbytecode.asm.definition.proxy.ProxyClassDefinition.DEFAULT_PROXY_PACKAGE;

public class BaseProxyBuilderTest {

    @Test
    public void givenDummyClassA_whenCreateEmptyProxy_thenOk() throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        DummyClassA proxy = new KirinBytecode()
                .subclass(DummyClassA.class)
                .make()
                .load()
                .newInstance(null, null);

        assertNotNull(proxy.getClass().getSuperclass());
        assertTrue(DummyClassA.class.isAssignableFrom(proxy.getClass().getSuperclass()));
    }

    @Test
    public void givenJdkObject_whenCreateEmptyProxy_thenPackageOk() throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        Object proxy = new KirinBytecode()
                .subclass(Object.class)
                .make()
                .load()
                .newInstance(null, null);

        assertNotNull(proxy.getClass().getSuperclass());
        assertEquals(DEFAULT_PROXY_PACKAGE, proxy.getClass().getPackageName());
    }

    @Test
    public void givenDummyClassA_whenCreateProxyWithInterface_thenOk() throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        DummyClassA proxy = new KirinBytecode()
                .subclass(DummyClassA.class)
                .implement(Serializable.class)
                .make()
                .load()
                .newInstance(null, null);

        Class<?>[] interfaces = proxy.getClass().getInterfaces();

        assertNotNull(interfaces);
        assertEquals(1, interfaces.length);
        assertEquals(Serializable.class.getName(), interfaces[0].getName());
    }

    @Test
    public void givenDummyClassA_whenCreateProxyWithFewDifferentInterfaces_thenOk() throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        DummyClassA proxy = new KirinBytecode()
                .subclass(DummyClassA.class)
                .implement(Serializable.class)
                .implement(Cloneable.class)
                .make()
                .load()
                .newInstance(null, null);

        Class<?>[] interfaces = proxy.getClass().getInterfaces();

        assertNotNull(interfaces);
        assertEquals(2, interfaces.length);
        assertTrue(Arrays.asList(interfaces).contains(Serializable.class));
        assertTrue(Arrays.asList(interfaces).contains(Cloneable.class));
    }

    @Test
    public void givenDummyClassA_whenCreateProxyWithFewEqualsInterfaces_thenOk() throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        DummyClassA proxy = new KirinBytecode()
                .subclass(DummyClassA.class)
                .implement(Serializable.class)
                .implement(Serializable.class)
                .make()
                .load()
                .newInstance(null, null);

        Class<?>[] interfaces = proxy.getClass().getInterfaces();

        assertNotNull(interfaces);
        assertEquals(1, interfaces.length);
        assertEquals(Serializable.class.getName(), interfaces[0].getName());
    }

    @Test
    public void givenDummyClassA_whenCreateProxyWithName_thenOk() throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        String proxyName = UUID.randomUUID().toString();

        DummyClassA proxy = new KirinBytecode()
                .subclass(DummyClassA.class)
                .name(proxyName)
                .make()
                .load()
                .newInstance(null, null);

        assertEquals(proxyName, proxy.getClass().getSimpleName());
    }

    @Test
    public void givenDummyClassA_whenCreateProxyWithFewDifferentNames_thenGetFirstName() throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        String proxyName1 = UUID.randomUUID().toString();
        String proxyName2 = UUID.randomUUID().toString();

        DummyClassA proxy = new KirinBytecode()
                .subclass(DummyClassA.class)
                .name(proxyName1)
                .name(proxyName2)
                .make()
                .load()
                .newInstance(null, null);

        assertEquals(proxyName1, proxy.getClass().getSimpleName());
    }

    @Test
    public void givenDummyClassA_whenCreateProxyWithFewEqualsNames_thenOk() throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        String proxyName = UUID.randomUUID().toString();

        DummyClassA proxy = new KirinBytecode()
                .subclass(DummyClassA.class)
                .name(proxyName)
                .name(proxyName)
                .make()
                .load()
                .newInstance(null, null);

        assertEquals(proxyName, proxy.getClass().getSimpleName());
    }
}
