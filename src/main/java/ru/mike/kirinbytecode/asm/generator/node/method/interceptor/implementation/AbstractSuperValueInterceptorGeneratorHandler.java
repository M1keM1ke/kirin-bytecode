package ru.mike.kirinbytecode.asm.generator.node.method.interceptor.implementation;

import org.objectweb.asm.tree.MethodNode;
import ru.mike.kirinbytecode.asm.builder.InterceptorImplementation;
import ru.mike.kirinbytecode.asm.definition.InterceptedMethodDefinition;
import ru.mike.kirinbytecode.asm.definition.MethodDefinition;
import ru.mike.kirinbytecode.asm.definition.proxy.ProxyClassDefinition;
import ru.mike.kirinbytecode.asm.generator.node.method.interceptor.InterceptorSuperNodeGeneratorHandler;
import ru.mike.kirinbytecode.asm.generator.node.method.interceptor.SuperMethodCallProperties;
import ru.mike.kirinbytecode.asm.matcher.SuperValue;

import java.lang.reflect.Modifier;

import static org.objectweb.asm.Opcodes.ASM9;
import static ru.mike.kirinbytecode.asm.exception.incorrect.IncorrectModifierException.throwIncorrectFinal;
import static ru.mike.kirinbytecode.asm.exception.incorrect.IncorrectModifierException.throwIncorrectPrivate;
import static ru.mike.kirinbytecode.asm.exception.notfound.InterceptorImplementationNotFoundException.checkImplementationTypeOrThrow;
import static ru.mike.kirinbytecode.asm.util.AsmUtil.LOADbyClass;
import static ru.mike.kirinbytecode.asm.util.AsmUtil.RETURNbyClass;

public abstract class AbstractSuperValueInterceptorGeneratorHandler<T> implements InterceptorSuperNodeGeneratorHandler<T> {

    @Override
    public MethodNode generateMethodNode(ProxyClassDefinition<T> definition, MethodDefinition<T> methodDefinition) {
        InterceptedMethodDefinition<T> interceptedMethodDefinition = (InterceptedMethodDefinition<T>) methodDefinition;
        InterceptorImplementation implementation = methodDefinition.getImplementation();

        String interceptedMethodName = interceptedMethodDefinition.getName();
        int modifiers = interceptedMethodDefinition.getModifiers();

        checkImplementationTypeOrThrow(SuperValue.class, implementation);
        checkModifiersCorrectOrThrow(definition, interceptedMethodName, modifiers);
        checkSuperMethodParamsOrThrow(interceptedMethodDefinition, implementation);

        MethodNode mn = new MethodNode(ASM9, modifiers, interceptedMethodName,
                methodDefinition.getMethodDescriptor(), null, null);

        return generateMethodNode(definition, interceptedMethodDefinition, mn);
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

    private MethodNode generateMethodNode(
            ProxyClassDefinition<T> definition,
            InterceptedMethodDefinition<T> interceptedMethodDefinition,
            MethodNode mn
    ) {
        generateBeforeMethodDefinitionCall(definition, interceptedMethodDefinition.getBeforeMethodDefinition(), mn);
        SuperMethodCallProperties superMethodCallProperties = generateSuperMethodCall(definition, interceptedMethodDefinition, mn);
        generateAfterMethodDefinitionCall(definition, interceptedMethodDefinition.getAfterMethodDefinition(), mn);

        Class<?> interceptedMethodReturnType = interceptedMethodDefinition.getReturnType();
        mn.visitVarInsn(
                LOADbyClass(interceptedMethodReturnType),
                superMethodCallProperties.getLastLOADOpcodeNumber()
        );
        mn.visitInsn(RETURNbyClass(interceptedMethodReturnType));

        return mn;
    }
}