package ru.mike.kirinbytecode.asm.generator.node.method.interceptor.implementation;

import com.google.auto.service.AutoService;
import org.objectweb.asm.tree.MethodNode;
import ru.mike.kirinbytecode.asm.builder.InterceptorImplementation;
import ru.mike.kirinbytecode.asm.definition.InterceptedMethodDefinition;
import ru.mike.kirinbytecode.asm.definition.MethodDefinition;
import ru.mike.kirinbytecode.asm.definition.proxy.ProxyClassDefinition;
import ru.mike.kirinbytecode.asm.generator.node.method.NodeGeneratorHandler;
import ru.mike.kirinbytecode.asm.generator.node.method.interceptor.SuperMethodCallProperties;
import ru.mike.kirinbytecode.asm.matcher.SuperValue;

import java.util.Objects;

import static org.objectweb.asm.Opcodes.ALOAD;
import static org.objectweb.asm.Opcodes.INVOKESPECIAL;
import static ru.mike.kirinbytecode.asm.exception.incorrect.IncorrectSuperMethodParametersException.throwParamsNonNull;
import static ru.mike.kirinbytecode.asm.util.AsmUtil.STOREbyClass;

@AutoService(NodeGeneratorHandler.class)
public class SuperValueWithoutParamsInterceptorNodeGeneratorHandler<T> extends AbstractSuperValueInterceptorGeneratorHandler<T> {

    @Override
    public boolean isSuitableHandler(MethodDefinition<T> methodDefinition) {
        InterceptorImplementation implementation = methodDefinition.getImplementation();
        return Objects.nonNull(implementation) &&
                implementation instanceof SuperValue &&
                methodDefinition instanceof InterceptedMethodDefinition &&
                Objects.isNull(((SuperValue) implementation).getParams())
        ;
    }

    @Override
    public SuperMethodCallProperties generateSuperMethodCall(
            ProxyClassDefinition<T> definition, InterceptedMethodDefinition<T> methodDefinition, MethodNode mn
    ) {
        mn.visitVarInsn(ALOAD, 0);

        int paramNumber = 1;

        mn.visitMethodInsn(
                INVOKESPECIAL,
                definition.getOriginalClazz().getName().replaceAll("\\.", "/"),
                methodDefinition.getName(),
                methodDefinition.getMethodDescriptor(),
                false
        );

        mn.visitVarInsn(STOREbyClass(methodDefinition.getReturnType()), paramNumber);

        return SuperMethodCallProperties.builder()
                .lastLOADOpcodeNumber(paramNumber)
                .build();
    }

    @Override
    public void checkSuperMethodParamsOrThrow(InterceptedMethodDefinition<T> methodDefinition, InterceptorImplementation implementation) {
        SuperValue superValueImp = (SuperValue) implementation;
        Object[] superValueImpParams = superValueImp.getParams();

        if (Objects.nonNull(superValueImpParams)) {
            throwParamsNonNull(superValueImpParams);
        }
    }
}
