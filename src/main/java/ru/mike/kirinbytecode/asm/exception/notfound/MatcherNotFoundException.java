package ru.mike.kirinbytecode.asm.exception.notfound;

public class MatcherNotFoundException extends RuntimeException {

    public MatcherNotFoundException(String message) {
        super(message);
    }
}
