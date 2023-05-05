package ru.mike.kirinbytecode.asm.exception.notfound;

public class MethodNotFoundException extends RuntimeException {

    public MethodNotFoundException(String message) {
        super(message);
    }
}
