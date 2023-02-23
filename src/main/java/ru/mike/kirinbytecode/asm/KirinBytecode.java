package ru.mike.kirinbytecode.asm;

import ru.mike.kirinbytecode.asm.builder.Builder;
import ru.mike.kirinbytecode.asm.builder.SubclassDynamicTypeBuilder;
import ru.mike.kirinbytecode.asm.definition.proxy.ProxyClassDefinition;

public class KirinBytecode {
    private ProxyClassDefinition definition;

    public KirinBytecode() {
        this.definition = new ProxyClassDefinition<>();
    }

    public <T> Builder<T> subclass(Class<T> clazz) {
        definition.setOriginalClazz(clazz);
        return new SubclassDynamicTypeBuilder<T>(definition);
    }
}
