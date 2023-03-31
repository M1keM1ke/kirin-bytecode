package ru.mike.kirinbytecode.asm.generator.node.method.interceptor;

import com.google.auto.service.AutoService;
import org.objectweb.asm.tree.MethodNode;
import ru.mike.kirinbytecode.asm.builder.InterceptorImplementation;
import ru.mike.kirinbytecode.asm.definition.MethodDefinition;
import ru.mike.kirinbytecode.asm.definition.proxy.ProxyClassDefinition;
import ru.mike.kirinbytecode.asm.matcher.SuperValue;

import java.lang.reflect.Method;

import static org.objectweb.asm.Opcodes.ACC_PUBLIC;
import static org.objectweb.asm.Opcodes.ALOAD;
import static org.objectweb.asm.Opcodes.ARETURN;
import static org.objectweb.asm.Opcodes.ASM9;

@AutoService(InterceptorNodeGeneratorHandler.class)
public class SuperValueInterceptorNodeGeneratorHandler<T> implements InterceptorNodeGeneratorHandler<T> {
    @Override
    public boolean isSuitableHandler(InterceptorImplementation implementation) {
        return implementation instanceof SuperValue;
    }

    @Override
    public MethodNode generateMethodNode(ProxyClassDefinition<T> definition, MethodDefinition<T> methodDefinition) {
        SuperValue superValue = (SuperValue) methodDefinition.getImplementation();
        boolean isSuper = superValue.isSuper();

        Method interceptedMethod = methodDefinition.getMethod();
        String methodDescriptor = methodDefinition.getMethodDescriptor();
        String methodName = interceptedMethod.getName();

//      TODO: реализовать modifiers
        int modifiers = interceptedMethod.getModifiers();

        MethodNode mn = new MethodNode(ASM9, ACC_PUBLIC, methodName, methodDescriptor, null, null);

        generateBeforeMethodDefinitionCall(definition, methodDefinition.getBeforeMethodDefinition(), mn);

        generateSuperMethodCall(definition, methodDefinition, mn);

        generateAfterMethodDefinitionCall(definition, methodDefinition.getAfterMethodDefinition(), mn);

        mn.visitVarInsn(ALOAD, 1);
        mn.visitInsn(ARETURN);

        return mn;
    }
}
