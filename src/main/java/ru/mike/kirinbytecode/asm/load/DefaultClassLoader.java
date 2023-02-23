package ru.mike.kirinbytecode.asm.load;

public class DefaultClassLoader extends ClassLoader {

    public Class<?> defineClass(String name, byte[] b) {
        return defineClass(name, b, 0, b.length);
    }
}
