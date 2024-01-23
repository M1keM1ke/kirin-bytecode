package ru.mike.kirinbytecode.asm.builder;


import lombok.Getter;
import ru.mike.kirinbytecode.asm.definition.proxy.ProxyClassDefinition;
import ru.mike.kirinbytecode.asm.load.DefaultClassLoader;

public class UnloadedType<T> {
    private ProxyClassDefinition<T> definition;
    @Getter
    byte[] bytecodeClazz;

    public UnloadedType(ProxyClassDefinition<T> definition) {
        this.definition = definition;
        this.bytecodeClazz = definition.getBytecodeClazz();
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
