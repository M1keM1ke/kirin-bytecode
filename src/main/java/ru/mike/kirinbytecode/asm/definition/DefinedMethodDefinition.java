package ru.mike.kirinbytecode.asm.definition;

import lombok.Getter;
import lombok.Setter;
import ru.mike.kirinbytecode.asm.definition.proxy.ProxyClassDefinition;
import ru.mike.kirinbytecode.asm.generator.node.method.DefaultMethodGenerator;
import ru.mike.kirinbytecode.asm.util.AsmUtil;


@Getter
@Setter
public class DefinedMethodDefinition<T> extends MethodDefinition<T> {

    public DefinedMethodDefinition(
            ProxyClassDefinition<T> definition, String name, Class<?> returnType, int modifiers
    ) {
        this.name = name;
        this.returnType = returnType;
        this.modifiers = modifiers;
        //дескриптор может поменяться в будущем, если пользователь добавит к методу параметры
        this.methodDescriptor = AsmUtil.getMethodDescriptor(returnType);
        this.methodGenerator = new DefaultMethodGenerator<>(definition);
    }
}
