package ru.mike.kirinbytecode.asm.builder.other;

import ru.mike.kirinbytecode.asm.builder.Builder;

public interface Junction<T> {

    Builder<T> and();
}
