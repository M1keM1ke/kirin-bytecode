package ru.mike.kirinbytecode.asm.generator.node.method.interceptor.supervalue;

import org.objectweb.asm.tree.MethodNode;
import ru.mike.kirinbytecode.asm.builder.InterceptorImplementation;
import ru.mike.kirinbytecode.asm.definition.MethodDefinition;
import ru.mike.kirinbytecode.asm.definition.proxy.ProxyClassDefinition;
import ru.mike.kirinbytecode.asm.generator.node.method.interceptor.InterceptorSuperNodeGeneratorHandler;
import ru.mike.kirinbytecode.asm.generator.node.method.interceptor.SuperMethodCallProperties;
import ru.mike.kirinbytecode.asm.matcher.SuperValue;

import java.lang.reflect.Method;
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
        InterceptorImplementation implementation = methodDefinition.getImplementation();
        Method interceptedMethod = methodDefinition.getMethod();

        String interceptedMethodName = interceptedMethod.getName();
        int modifiers = interceptedMethod.getModifiers();

        checkImplementationTypeOrThrow(SuperValue.class, implementation);
        checkModifiersCorrectOrThrow(definition, interceptedMethodName, modifiers);
        checkSuperMethodParamsOrThrow(methodDefinition, implementation);

        MethodNode mn = new MethodNode(ASM9, modifiers, interceptedMethodName,
                methodDefinition.getMethodDescriptor(), null, null);

        return generateMethodNode(definition, methodDefinition, mn);
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
            MethodDefinition<T> methodDefinition,
            MethodNode mn
    ) {
        Method interceptedMethod = methodDefinition.getMethod();

        generateBeforeMethodDefinitionCall(definition, methodDefinition.getBeforeMethodDefinition(), mn);
        SuperMethodCallProperties superMethodCallProperties = generateSuperMethodCall(definition, methodDefinition, mn);
        generateAfterMethodDefinitionCall(definition, methodDefinition.getAfterMethodDefinition(), mn);

        Class<?> interceptedMethodReturnType = interceptedMethod.getReturnType();
        mn.visitVarInsn(
                LOADbyClass(interceptedMethodReturnType),
                superMethodCallProperties.getLastLOADOpcodeNumber()
        );
        mn.visitInsn(RETURNbyClass(interceptedMethodReturnType));

        return mn;
    }
}