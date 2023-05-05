package ru.mike.kirinbytecode.asm.exception.notfound;

public class FieldNotFoundException extends RuntimeException {

    public FieldNotFoundException(String message) {
        super(message);
    }
}
