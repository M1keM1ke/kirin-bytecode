package ru.mike.kirinbytecode.asm.matcher;

import lombok.Getter;
import lombok.Setter;
import ru.mike.kirinbytecode.asm.builder.InterceptorImplementation;

@Getter
@Setter
public class FixedValue implements InterceptorImplementation {
    private Object value;

    public FixedValue(Object value) {
        this.value = value;
    }

    public FixedValue() {
        this.value = null;
    }

    public static InterceptorImplementation value(Object value) {
        return new FixedValue(value);
    }

    public static InterceptorImplementation value() {
        return new FixedValue();
    }
}
