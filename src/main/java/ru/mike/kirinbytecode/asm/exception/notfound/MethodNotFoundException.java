package ru.mike.kirinbytecode.asm.exception.notfound;

public class MethodNotFoundException extends RuntimeException {

    public MethodNotFoundException(String message) {
        super(message);
    }

    public static MethodNotFoundException byName(String methodName) {
        return new MethodNotFoundException("Unable to find method with name:" + methodName);
    }
}
