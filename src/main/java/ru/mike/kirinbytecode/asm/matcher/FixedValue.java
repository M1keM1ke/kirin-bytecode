package ru.mike.kirinbytecode.asm.matcher;

import lombok.Getter;
import lombok.Setter;
import ru.mike.kirinbytecode.asm.builder.InterceptorImplementation;

@Getter
@Setter
public class FixedValue implements InterceptorImplementation {
    private String value;

    public FixedValue(String value) {
        this.value = value;
    }

    public static InterceptorImplementation value(String value) {
        return new FixedValue(value);
    }
}
