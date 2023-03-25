package ru.mike.kirinbytecode.asm.definition;

import lombok.Getter;
import lombok.Setter;
import org.objectweb.asm.Type;
import ru.mike.kirinbytecode.asm.builder.InterceptorImplementation;
import ru.mike.kirinbytecode.asm.definition.proxy.ProxyClassDefinition;
import ru.mike.kirinbytecode.asm.generator.node.DefaultMethodGenerator;

import java.lang.reflect.Method;

@Getter
@Setter
public class MethodDefinition<T> implements Definition {
    private Method method;
    private String methodDescriptor;
    private DefaultMethodGenerator<T> methodGenerator;
    private InterceptorImplementation implementation;
    private AfterMethodDefinition afterMethodDefinition;
    private BeforeMethodDefinition beforeMethodDefinition;

    public MethodDefinition(ProxyClassDefinition<T> definition, Method method) {
        this.method = method;
        this.methodDescriptor = Type.getMethodDescriptor(method);
        methodGenerator = new DefaultMethodGenerator<T>(definition);
    }
}
