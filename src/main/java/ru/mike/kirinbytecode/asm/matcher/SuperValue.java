package ru.mike.kirinbytecode.asm.matcher;

import lombok.Getter;
import ru.mike.kirinbytecode.asm.builder.InterceptorImplementation;

@Getter
public class SuperValue implements InterceptorImplementation {
    private boolean isSuper;

    public SuperValue() {
        this.isSuper = true;
    }

    public static InterceptorImplementation value() {
        return new SuperValue();
    }
}
