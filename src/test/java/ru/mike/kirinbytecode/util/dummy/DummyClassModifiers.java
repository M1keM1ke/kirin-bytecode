package ru.mike.kirinbytecode.util.dummy;

import java.util.List;

public class DummyClassModifiers {
    private void dummyPrivateVoidMethod() {
    }

    private String dummyPrivateVoidMethodWith1Param(String str) {
        return str;
    }

    public final void dummyPublicFinalVoidMethod() {
    }

    public final String dummyPublicFinalVoidMethodWith1Param(String str) {
        return str;
    }

    public String dummyPublicVoidMethodWith1Param(String str) {
        return str;
    }

    public String dummyPublicVoidMethodWith2Param(String str, Integer i) {
        return str;
    }

    public String dummyPublicVoidMethodWith3Param(String str, Integer i, List<Object> objects) {
        return str;
    }
}
