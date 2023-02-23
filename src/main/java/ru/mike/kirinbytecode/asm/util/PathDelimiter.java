package ru.mike.kirinbytecode.asm.util;

import lombok.Getter;

@Getter
public enum PathDelimiter {
    DOT("\\."),
    RIGHT_SLASH("/");

    private String delimiter;

    PathDelimiter(String delimiter) {
        this.delimiter = delimiter;
    }
}
