package ru.mike.kirinbytecode.asm.generator.node.method.interceptor;

import org.objectweb.asm.tree.MethodNode;
import ru.mike.kirinbytecode.asm.builder.InterceptorImplementation;
import ru.mike.kirinbytecode.asm.definition.MethodDefinition;
import ru.mike.kirinbytecode.asm.definition.proxy.ProxyClassDefinition;


public interface InterceptorSuperNodeGeneratorHandler<T> extends InterceptorNodeGeneratorHandler<T> {

    /**
     * Генерирует {@link MethodNode} с вызовом супер-метода оригинального класса
     *
     * @param definition описание, содержащее оригинальный класс
     * @param methodDefinition описание, содержащее супер-метод оригинального класса {@link MethodDefinition#getMethod()}
     * @param mn пустая нода, в которой производится вызов супер-метода
     */
    void generateSuperMethodCall(ProxyClassDefinition<T> definition, MethodDefinition<T> methodDefinition, MethodNode mn);

    /**
     * Проверяет параметры супер-метода, сравнивая их с передаваемыми (если параметры вообще есть)
     *
     * @param methodDefinition описание, содержащее супер-метод оригинального класса {@link MethodDefinition#getMethod()}
     * @param implementation реализация {@link InterceptorImplementation}
     */
    void checkSuperMethodParamsOrThrow(MethodDefinition<T> methodDefinition, InterceptorImplementation implementation);
}
