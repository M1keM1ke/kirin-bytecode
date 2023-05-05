package ru.mike.kirinbytecode.asm.generator.node.method.interceptor.supervalue;

import org.objectweb.asm.tree.MethodNode;
import ru.mike.kirinbytecode.asm.builder.InterceptorImplementation;
import ru.mike.kirinbytecode.asm.definition.MethodDefinition;
import ru.mike.kirinbytecode.asm.definition.proxy.ProxyClassDefinition;
import ru.mike.kirinbytecode.asm.generator.node.method.interceptor.InterceptorSuperNodeGeneratorHandler;
import ru.mike.kirinbytecode.asm.matcher.SuperValue;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

import static org.objectweb.asm.Opcodes.ALOAD;
import static org.objectweb.asm.Opcodes.ARETURN;
import static org.objectweb.asm.Opcodes.ASM9;
import static ru.mike.kirinbytecode.asm.exception.incorrect.IncorrectModifierException.throwIncorrectFinal;
import static ru.mike.kirinbytecode.asm.exception.incorrect.IncorrectModifierException.throwIncorrectPrivate;
import static ru.mike.kirinbytecode.asm.exception.notfound.InterceptorImplementationNotFoundException.checkImplementationTypeOrThrow;

public abstract class AbstractSuperValueInterceptorGeneratorHandler<T> implements InterceptorSuperNodeGeneratorHandler<T> {

    @Override
    public MethodNode generateMethodNode(ProxyClassDefinition<T> definition, MethodDefinition<T> methodDefinition) {
        InterceptorImplementation implementation = methodDefinition.getImplementation();

        checkImplementationTypeOrThrow(SuperValue.class, implementation);

        Method interceptedMethod = methodDefinition.getMethod();

        String interceptedMethodName = interceptedMethod.getName();
        int modifiers = interceptedMethod.getModifiers();

        checkModifiersCorrectOrThrow(definition, interceptedMethodName, modifiers);

        String methodDescriptor = methodDefinition.getMethodDescriptor();

        checkSuperMethodParamsOrThrow(methodDefinition, implementation);

        MethodNode mn = new MethodNode(ASM9, modifiers, interceptedMethodName, methodDescriptor, null, null);

        generateBeforeMethodDefinitionCall(definition, methodDefinition.getBeforeMethodDefinition(), mn);
        generateSuperMethodCall(definition, methodDefinition, mn);
        generateAfterMethodDefinitionCall(definition, methodDefinition.getAfterMethodDefinition(), mn);

        mn.visitVarInsn(ALOAD, 1);
        mn.visitInsn(ARETURN);

        return mn;
    }

    @Override
    public void checkModifiersCorrectOrThrow(ProxyClassDefinition<T> definition, String interceptedMethodName, int modifiers) {
//      если в супер классе метод, который мы хотим переопределить в прокси классе - приватный, то кидаем исключение,
//      так как в таком прокси методе будет вызван super.method(), что невозможно из-за приватности
        if (Modifier.isPrivate(modifiers)) {
            throwIncorrectPrivate(definition.getOriginalClazz().getName(), interceptedMethodName);
        }

//      если в супер классе метод, который мы хотим переопределить в прокси классе - финальный, то кидаем исключение,
//      так как в такой метод не может быть переопределен
        if (Modifier.isFinal(modifiers)) {
            throwIncorrectFinal(definition.getOriginalClazz().getName(), interceptedMethodName);
        }
    }
}