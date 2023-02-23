package ru.mike.kirinbytecode.asm.matcher;

import ru.mike.kirinbytecode.asm.builder.InterceptorImplementation;

public class FixedValue implements InterceptorImplementation {
    private String value;

    public FixedValue(String value) {
        this.value = value;
    }

    public static InterceptorImplementation value(String value) {
        return new FixedValue(value);
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
