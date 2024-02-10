package ru.mike.kirinbytecode.asm.generator.node.method.interceptor;

import org.objectweb.asm.tree.MethodNode;
import ru.mike.kirinbytecode.asm.builder.InterceptorImplementation;
import ru.mike.kirinbytecode.asm.definition.InterceptedMethodDefinition;
import ru.mike.kirinbytecode.asm.definition.proxy.ProxyClassDefinition;


public interface InterceptorSuperNodeGeneratorHandler<T> extends StagesNodeGeneratorHandler<T> {

    /**
     * Генерирует {@link MethodNode} с вызовом супер-метода оригинального класса
     *  @param definition описание, содержащее оригинальный класс
     * @param methodDefinition описание, содержащее супер-метод оригинального класса {@link InterceptedMethodDefinition#getMethod()}
     * @param mn пустая нода, в которой производится вызов супер-метода
     * @return настройки, сохраненные при генерации метода
     */
    SuperMethodCallProperties generateSuperMethodCall(ProxyClassDefinition<T> definition, InterceptedMethodDefinition<T> methodDefinition, MethodNode mn);

    /**
     * Проверяет параметры супер-метода, сравнивая их с передаваемыми (если параметры вообще есть)
     *
     * @param methodDefinition описание, содержащее супер-метод оригинального класса {@link InterceptedMethodDefinition#getMethod()}
     * @param implementation реализация {@link InterceptorImplementation}
     */
    void checkSuperMethodParamsOrThrow(InterceptedMethodDefinition<T> methodDefinition, InterceptorImplementation implementation);
}
