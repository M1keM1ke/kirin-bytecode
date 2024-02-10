package ru.mike.kirinbytecode.asm.definition;

import lombok.Getter;
import lombok.Setter;
import org.objectweb.asm.Type;
import ru.mike.kirinbytecode.asm.definition.proxy.ProxyClassDefinition;
import ru.mike.kirinbytecode.asm.generator.node.method.DefaultMethodGenerator;
import ru.mike.kirinbytecode.asm.mapper.ParameterMapper;

import java.lang.reflect.Method;

@Getter
@Setter
public class InterceptedMethodDefinition<T> extends MethodDefinition<T> {
    private AfterMethodDefinition afterMethodDefinition;
    private BeforeMethodDefinition beforeMethodDefinition;

    public InterceptedMethodDefinition(ProxyClassDefinition<T> definition, Method method) {
        this.name = method.getName();
        this.returnType = method.getReturnType();
        this.modifiers = method.getModifiers();
        this.methodDescriptor = Type.getMethodDescriptor(method);
        this.parameterDefinitions = ParameterMapper.toParameterDefinitions(method.getParameters());
        this.parameterCount = method.getParameterCount();
        this.methodGenerator = new DefaultMethodGenerator<>(definition);
    }
}
