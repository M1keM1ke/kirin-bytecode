package ru.mike.kirinbytecode.asm.exception.notfound;

import ru.mike.kirinbytecode.asm.builder.InterceptorImplementation;
import ru.mike.kirinbytecode.asm.matcher.SuperValue;

public class InterceptorImplementationNotFoundException extends RuntimeException {

    public InterceptorImplementationNotFoundException(String message) {
        super(message);
    }

    public static void checkImplementationTypeOrThrow(
            Class<? extends InterceptorImplementation> expected,
            InterceptorImplementation current
    ) {
        if (!(expected.isInstance(current))) {
            throwByImplementation(SuperValue.class, current);
        }
    }

    public static void throwByImplementation(
            Class<? extends InterceptorImplementation> expected,
            InterceptorImplementation current
    ) {
        throw new InterceptorImplementationNotFoundException("No suitable InterceptorImplementation, expected "
                + expected.getName() + ", got " + current.getClass().getName());

    }

}
