package ru.mike.kirinbytecode.asm.builder;


import lombok.Getter;
import ru.mike.kirinbytecode.asm.definition.proxy.ProxyClassDefinition;
import ru.mike.kirinbytecode.asm.load.DefaultClassLoader;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class UnloadedType<T> {
    public static final String DEFAULT_FOLDER = "kirin-output";

    private ProxyClassDefinition<T> definition;
    @Getter
    byte[] bytecodeClazz;

    public UnloadedType(ProxyClassDefinition<T> definition) {
        this.definition = definition;
        this.bytecodeClazz = definition.getBytecodeClazz();
    }

    public UnloadedType<T> saveToFile() {
        new File(DEFAULT_FOLDER).mkdir();
        saveToFile(DEFAULT_FOLDER);

        return this;
    }

    public UnloadedType<T> saveToFile(String path) {
        File file = new File(path + "/" + definition.getGeneratedProxyClassName() + ".class");

        try(FileOutputStream fileOutputStream = new FileOutputStream(file)) {
            fileOutputStream.write(this.bytecodeClazz);
            fileOutputStream.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return this;
    }

    @SuppressWarnings({"unchecked"})
    public LoadedType<T> load() {
        return load(new DefaultClassLoader());
    }

    public LoadedType<T> load(DefaultClassLoader classLoader) {
        String generatedClassName = definition.getProxyPackageName().replaceAll("/", ".") +
                "." + definition.getNameGenerator().getGeneratedName(definition.getOriginalClazz().getSimpleName());

        classLoader.defineClass(generatedClassName, bytecodeClazz);

        try {
            Class<? extends T> loadedClass = (Class<? extends T> ) classLoader.loadClass(generatedClassName);
            definition.setGeneratedClazz(loadedClass);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        return new LoadedType<>(definition);
    }


}
