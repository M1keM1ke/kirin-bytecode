package ru.mike.kirinbytecode.asm.generator;

import org.objectweb.asm.tree.FieldNode;

public interface FieldGenerator<T> extends Generator<T> {

    FieldNode getFn();
}
