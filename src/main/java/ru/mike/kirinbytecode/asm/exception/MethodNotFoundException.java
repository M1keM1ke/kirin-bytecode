package ru.mike.kirinbytecode.asm.exception;

public class MethodNotFoundException extends RuntimeException {

    public MethodNotFoundException(String message) {
        super(message);
    }
}
