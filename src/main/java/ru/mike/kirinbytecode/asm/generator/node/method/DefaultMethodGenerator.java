package ru.mike.kirinbytecode.asm.generator.node.method;

import org.objectweb.asm.tree.MethodNode;
import ru.mike.kirinbytecode.asm.definition.MethodDefinition;
import ru.mike.kirinbytecode.asm.definition.proxy.ProxyClassDefinition;
import ru.mike.kirinbytecode.asm.exception.notfound.InterceptorImplementationNotFoundException;
import ru.mike.kirinbytecode.asm.generator.MethodGenerator;

import java.util.ServiceLoader;
import java.util.stream.StreamSupport;

public class DefaultMethodGenerator<T> implements MethodGenerator<T> {
    private ProxyClassDefinition<T> proxyClassDefinition;
    private MethodNode mn;
    private MethodDefinition<T> methodDefinition;

    public DefaultMethodGenerator(ProxyClassDefinition<T> definition) {
        this.proxyClassDefinition = definition;
    }


    @Override
    @SuppressWarnings({"all"})
    public void generateNode() {
        ServiceLoader<NodeGeneratorHandler> loader = ServiceLoader.load(NodeGeneratorHandler.class);

        mn = StreamSupport.stream(loader.spliterator(), false)
                .filter(ingh -> ingh.isSuitableHandler(methodDefinition))
                .findFirst()
                .orElseThrow(() -> new InterceptorImplementationNotFoundException(
                        "Unable to interceptor by implementation:" + methodDefinition.getImplementation() + ". Type not supported.")
                )
                .generateMethodNode(proxyClassDefinition, methodDefinition)
        ;
    }

    @Override
    public void setMethodDefinition(MethodDefinition<T> methodDefinition) {
        this.methodDefinition = methodDefinition;
    }

    @Override
    public MethodNode getMn() {
        return mn;
    }
}
