package ru.mike.kirinbytecode.asm.exception;

public class FieldNotFoundException extends RuntimeException {

    public FieldNotFoundException(String message) {
        super(message);
    }
}
