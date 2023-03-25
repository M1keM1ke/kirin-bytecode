package ru.mike.kirinbytecode.util;

import static ru.mike.kirinbytecode.util.TestConstants.DummyMethod1.DUMMY_METHOD_1_RETURN_VALUE;
import static ru.mike.kirinbytecode.util.TestConstants.DummyMethod2.DUMMY_METHOD_2_RETURN_VALUE;
import static ru.mike.kirinbytecode.util.TestConstants.DummyMethod3.DUMMY_METHOD_3_RETURN_VALUE;

public class DummyClassA {

    public String dummyMethod1() {
        return DUMMY_METHOD_1_RETURN_VALUE;
    }

    public String dummyMethod2() {
        return DUMMY_METHOD_2_RETURN_VALUE;
    }

    public String dummy3Method() {
        return DUMMY_METHOD_3_RETURN_VALUE;
    }
}
