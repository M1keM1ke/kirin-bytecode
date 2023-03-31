package ru.mike.kirinbytecode.asm.generator.node.method.interceptor;

import com.google.auto.service.AutoService;
import org.objectweb.asm.tree.MethodNode;
import ru.mike.kirinbytecode.asm.builder.InterceptorImplementation;
import ru.mike.kirinbytecode.asm.definition.MethodDefinition;
import ru.mike.kirinbytecode.asm.definition.proxy.ProxyClassDefinition;
import ru.mike.kirinbytecode.asm.exception.ReturnTypeCastException;
import ru.mike.kirinbytecode.asm.matcher.FixedValue;

import java.lang.reflect.Method;

import static org.objectweb.asm.Opcodes.ACC_PUBLIC;
import static org.objectweb.asm.Opcodes.ARETURN;
import static org.objectweb.asm.Opcodes.ASM9;

@AutoService(InterceptorNodeGeneratorHandler.class)
public class FixedValueInterceptorNodeGeneratorHandler<T> implements InterceptorNodeGeneratorHandler<T> {

    @Override
    public boolean isSuitableHandler(InterceptorImplementation implementation) {
        return implementation instanceof FixedValue;
    }

    @Override
    public MethodNode generateMethodNode(ProxyClassDefinition<T> definition, MethodDefinition<T> methodDefinition) {
        FixedValue fixedValueImp = (FixedValue) methodDefinition.getImplementation();

        String interceptedReturnValue = fixedValueImp.getValue();
        Method interceptedMethod = methodDefinition.getMethod();

        Class<?> originalReturnType = interceptedMethod.getReturnType();
        Class<? extends String> interceptedReturnType = interceptedReturnValue.getClass();

        if (!originalReturnType.equals(interceptedReturnType)) {
            throw new ReturnTypeCastException("Unable to intercept method:" + interceptedMethod +
                    ". Return types are not equals. Expected:'" + originalReturnType + "', but got " + interceptedReturnType);
        }

        String methodDescriptor = methodDefinition.getMethodDescriptor();
        String methodName = interceptedMethod.getName();
//      TODO: реализовать modifiers
        int modifiers = interceptedMethod.getModifiers();

        MethodNode mn = new MethodNode(ASM9, ACC_PUBLIC, methodName, methodDescriptor, null, null);

        generateBeforeMethodDefinitionCall(definition, methodDefinition.getBeforeMethodDefinition(), mn);
        generateAfterMethodDefinitionCall(definition, methodDefinition.getAfterMethodDefinition(), mn);

        mn.visitLdcInsn(interceptedReturnValue);
        mn.visitInsn(ARETURN);

        return mn;
    }
}
