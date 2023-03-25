package ru.mike.kirinbytecode.builder;

import org.junit.jupiter.api.Test;
import org.objectweb.asm.Opcodes;
import ru.mike.kirinbytecode.asm.KirinBytecode;
import ru.mike.kirinbytecode.asm.exception.ProxyFieldAlreadyExistsException;
import ru.mike.kirinbytecode.util.DummyClassA;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.Map;
import java.util.Random;
import java.util.UUID;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class ProxyFieldsBuilderTest implements Opcodes {

    @Test
    public void givenDummyClassA_whenCreateProxyWithField_thenOk() throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        Class<Integer> generatedFieldType = Integer.class;
        String generatedFieldName = UUID.randomUUID().toString();
        Integer generatedFieldValue = new Random().nextInt();
        int generatedFieldModifiers = ACC_PUBLIC;

        DummyClassA proxy = new KirinBytecode()
                .subclass(DummyClassA.class)
                .defineField(generatedFieldName, generatedFieldType, generatedFieldModifiers)
                .value(generatedFieldValue)
                .and()
                .make()
                .load()
                .newInstance(null, null);

        Field[] proxyFields = proxy.getClass().getDeclaredFields();

        assertEquals(1, proxyFields.length);

        Field proxyField = proxyFields[0];
        proxyField.setAccessible(true);

        assertEquals(generatedFieldType, proxyField.getType());
        assertEquals(generatedFieldName, proxyField.getName());
        assertEquals(generatedFieldValue, proxyField.get(proxy));
        assertEquals(generatedFieldModifiers, proxyField.getModifiers());
    }

    @Test
    public void givenDummyClassA_whenCreateProxyWithFewDifferentFields_thenOk() throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
//      данные для первого поля
        Class<Integer> generatedFieldType1 = Integer.class;
        String generatedFieldName1 = UUID.randomUUID().toString();
        Integer generatedFieldValue1 = new Random().nextInt();
        int generatedFieldModifiers1 = ACC_PUBLIC;

//      данные для второго поля
        Class<String> generatedFieldType2 = String.class;
        String generatedFieldName2 = UUID.randomUUID().toString();
        String generatedFieldValue2 = UUID.randomUUID().toString();
        int generatedFieldModifiers2 = ACC_PUBLIC;

        DummyClassA proxy = new KirinBytecode()
                .subclass(DummyClassA.class)
                .defineField(generatedFieldName1, generatedFieldType1, generatedFieldModifiers1)
                .value(generatedFieldValue1)
                .and()
                .defineField(generatedFieldName2, generatedFieldType2, generatedFieldModifiers2)
                .value(generatedFieldValue2)
                .and()
                .make()
                .load()
                .newInstance(null, null);

        Map<String, Field> proxyFields = Arrays
                .stream(proxy.getClass().getDeclaredFields())
                .collect(Collectors.toMap(Field::getName, e -> e));

        assertEquals(2, proxyFields.size());

        Field proxyField1 = proxyFields.get(generatedFieldName1);
        Field proxyField2 = proxyFields.get(generatedFieldName2);

//      проверки для первого поля
        assertEquals(generatedFieldType1, proxyField1.getType());
        assertEquals(generatedFieldName1, proxyField1.getName());
        assertEquals(generatedFieldValue1, proxyField1.get(proxy));
        assertEquals(generatedFieldModifiers1, proxyField1.getModifiers());

//      проверки для первого поля
        assertEquals(generatedFieldType2, proxyField2.getType());
        assertEquals(generatedFieldName2, proxyField2.getName());
        assertEquals(generatedFieldValue2, proxyField2.get(proxy));
        assertEquals(generatedFieldModifiers2, proxyField2.getModifiers());
    }

    @Test
    public void givenDummyClassA_whenCreateProxyWithFewEqualsFields_thenThrow() {
        Class<Integer> generatedFieldType = Integer.class;
        String generatedFieldName = UUID.randomUUID().toString();
        Integer generatedFieldValue = new Random().nextInt();
        int generatedFieldModifiers = ACC_PUBLIC;

        assertThrows(
                ProxyFieldAlreadyExistsException.class,
                () -> new KirinBytecode()
                            .subclass(DummyClassA.class)
                            .defineField(generatedFieldName, generatedFieldType, generatedFieldModifiers)
                            .value(generatedFieldValue)
                        .and()
                            .defineField(generatedFieldName, generatedFieldType, generatedFieldModifiers)
                            .value(generatedFieldValue)
                        .and()
                            .make()
                            .load()
                            .newInstance(null, null)
        );
    }

    @Test
    public void givenDummyClassA_whenCreateProxyWithFieldWithoutValue_thenOk() throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        Class<Integer> generatedFieldType = Integer.class;
        String generatedFieldName = UUID.randomUUID().toString();
        int generatedFieldModifiers = ACC_PUBLIC;

        DummyClassA proxy = new KirinBytecode()
                .subclass(DummyClassA.class)
                .defineField(generatedFieldName, generatedFieldType, generatedFieldModifiers)
                .and()
                .make()
                .load()
                .newInstance(null, null);

        Field[] proxyFields = proxy.getClass().getDeclaredFields();

        assertEquals(1, proxyFields.length);

        Field proxyField = proxyFields[0];
        proxyField.setAccessible(true);

        assertEquals(generatedFieldType, proxyField.getType());
        assertEquals(generatedFieldName, proxyField.getName());
        assertNull(proxyField.get(proxy));
        assertEquals(generatedFieldModifiers, proxyField.getModifiers());
    }

    @Test
    public void givenDummyClassA_whenCreateProxyWithFieldWithoutModifiers_thenOk() throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        Class<Integer> generatedFieldType = Integer.class;
        String generatedFieldName = UUID.randomUUID().toString();
        int generatedFieldModifiers = ACC_PUBLIC;
        Integer generatedFieldValue = new Random().nextInt();

        DummyClassA proxy = new KirinBytecode()
                .subclass(DummyClassA.class)
                .defineField(generatedFieldName, generatedFieldType)
                .value(generatedFieldValue)
                .and()
                .make()
                .load()
                .newInstance(null, null);

        Field[] proxyFields = proxy.getClass().getDeclaredFields();

        assertEquals(1, proxyFields.length);

        Field proxyField = proxyFields[0];
        proxyField.setAccessible(true);

        assertEquals(generatedFieldType, proxyField.getType());
        assertEquals(generatedFieldName, proxyField.getName());
        assertEquals(generatedFieldValue, proxyField.get(proxy));
        assertEquals(generatedFieldModifiers, proxyField.getModifiers());
    }
}
