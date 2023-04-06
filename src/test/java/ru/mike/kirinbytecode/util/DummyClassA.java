package ru.mike.kirinbytecode.util;

import java.util.Random;

import static ru.mike.kirinbytecode.util.TestConstants.DummyMethod1.DUMMY_METHOD_1_RETURN_VALUE;
import static ru.mike.kirinbytecode.util.TestConstants.DummyMethod2.DUMMY_METHOD_2_RETURN_VALUE;
import static ru.mike.kirinbytecode.util.TestConstants.DummyMethod3.DUMMY_METHOD_3_RETURN_VALUE;

public class DummyClassA {
    public Random random = new Random();

    public String dummyMethod1() {
        return DUMMY_METHOD_1_RETURN_VALUE;
    }

    public String dummyMethod2() {
        return DUMMY_METHOD_2_RETURN_VALUE;
    }

    public Character dummy3Method() {
        return DUMMY_METHOD_3_RETURN_VALUE;
    }

    public Integer dummyWrapperMethodRandomInteger() {
        return random.nextInt();
    }

    public Short dummyWrapperMethodRandomShort() {
        return (short) random.nextInt(Short.MAX_VALUE + 1);
    }

    public Double dummyWrapperMethodRandomDouble() {
        return random.nextDouble();
    }

    public Long dummyWrapperMethodRandomLong() {
        return random.nextLong();
    }

    public Boolean dummyWrapperMethodRandomBoolean() {
        return random.nextBoolean();
    }

    public Character dummyWrapperMethodRandomCharacter() {
        return Character.forDigit(random.nextInt(100), 10);
    }

    public Object dummyWrapperMethodObject() {
        return new Object();
    }

    public int dummyPrimitiveMethodRandomInteger() {
        return random.nextInt();
    }

    public short dummyPrimitiveMethodRandomShort() {
        return (short) random.nextInt(Short.MAX_VALUE + 1);
    }

    public double dummyPrimitiveMethodRandomDouble() {
        return random.nextDouble();
    }

    public long dummyPrimitiveMethodRandomLong() {
        return random.nextLong();
    }

    public boolean dummyPrimitiveMethodRandomBoolean() {
        return random.nextBoolean();
    }

    public char dummyPrimitiveMethodRandomCharacter() {
        return Character.forDigit(random.nextInt(100), 10);
    }
}
