package ru.mike.kirinbytecode.asm.generator.node.method.interceptor.supervalue;

import com.google.auto.service.AutoService;
import org.objectweb.asm.Type;
import org.objectweb.asm.tree.MethodNode;
import ru.mike.kirinbytecode.asm.builder.InterceptorImplementation;
import ru.mike.kirinbytecode.asm.definition.MethodDefinition;
import ru.mike.kirinbytecode.asm.definition.proxy.ProxyClassDefinition;
import ru.mike.kirinbytecode.asm.generator.node.method.interceptor.InterceptorNodeGeneratorHandler;
import ru.mike.kirinbytecode.asm.generator.node.method.interceptor.SuperMethodCallProperties;
import ru.mike.kirinbytecode.asm.matcher.SuperValue;

import java.util.Objects;

import static org.objectweb.asm.Opcodes.ALOAD;
import static org.objectweb.asm.Opcodes.ASTORE;
import static org.objectweb.asm.Opcodes.INVOKESPECIAL;
import static ru.mike.kirinbytecode.asm.exception.incorrect.IncorrectSuperMethodParametersException.throwParamsNonNull;

@AutoService(InterceptorNodeGeneratorHandler.class)
public class SuperValueWithoutParamsInterceptorNodeGeneratorHandler<T> extends AbstractSuperValueInterceptorGeneratorHandler<T> {

    @Override
    public boolean isSuitableHandler(InterceptorImplementation implementation) {
        if (!(implementation instanceof SuperValue)) {
            return false;
        }

        SuperValue superValueImp = (SuperValue) implementation;

        return Objects.isNull(superValueImp.getParams());
    }

    @Override
    public SuperMethodCallProperties generateSuperMethodCall(
            ProxyClassDefinition<T> definition, MethodDefinition<T> methodDefinition, MethodNode mn
    ) {
        mn.visitVarInsn(ALOAD, 0);
        mn.visitMethodInsn(
                INVOKESPECIAL,
                definition.getOriginalClazz().getName().replaceAll("\\.", "/"),
                methodDefinition.getMethod().getName(),
                Type.getMethodDescriptor(methodDefinition.getMethod()),
                false
        );

        int paramNumber = 1;
        mn.visitVarInsn(ASTORE, paramNumber);

        return SuperMethodCallProperties.builder()
                .lastLOADOpcodeNumber(paramNumber)
                .build();
    }

    @Override
    public void checkSuperMethodParamsOrThrow(MethodDefinition<T> methodDefinition, InterceptorImplementation implementation) {
        SuperValue superValueImp = (SuperValue) implementation;
        Object[] superValueImpParams = superValueImp.getParams();

        if (Objects.nonNull(superValueImpParams)) {
            throwParamsNonNull(superValueImpParams);
        }
    }
}
