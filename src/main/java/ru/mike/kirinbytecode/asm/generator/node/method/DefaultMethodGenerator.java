package ru.mike.kirinbytecode.asm.generator.node.method;

import lombok.Getter;
import org.objectweb.asm.tree.MethodNode;
import ru.mike.kirinbytecode.asm.builder.InterceptorImplementation;
import ru.mike.kirinbytecode.asm.definition.MethodDefinition;
import ru.mike.kirinbytecode.asm.definition.proxy.ProxyClassDefinition;
import ru.mike.kirinbytecode.asm.exception.notfound.InterceptorImplementationNotFoundException;
import ru.mike.kirinbytecode.asm.generator.Generator;
import ru.mike.kirinbytecode.asm.generator.node.method.interceptor.InterceptorNodeGeneratorHandler;

import java.util.ServiceLoader;
import java.util.stream.StreamSupport;

@Getter
public class DefaultMethodGenerator<T> implements Generator {
    private ProxyClassDefinition<T> definition;
    private MethodNode mn;
    private MethodDefinition<T> methodDefinition;

    public DefaultMethodGenerator(ProxyClassDefinition<T> definition) {
        this.definition = definition;
    }

    public void setMethodDefinition(MethodDefinition<T> methodDefinition) {
        this.methodDefinition = methodDefinition;
    }

    @Override
    @SuppressWarnings({"all"})
    public void generateNode() {
        InterceptorImplementation implementation = methodDefinition.getImplementation();

        ServiceLoader<InterceptorNodeGeneratorHandler> loader = ServiceLoader.load(InterceptorNodeGeneratorHandler.class);

        mn = StreamSupport.stream(loader.spliterator(), false)
                .filter(ingh -> ingh.isSuitableHandler(implementation))
                .findFirst()
                .orElseThrow(() -> new InterceptorImplementationNotFoundException(
                        "Unable to interceptor by implementation:" + implementation + ". Type not supported.")
                )
                .generateMethodNode(definition, methodDefinition)
        ;
    }
}
