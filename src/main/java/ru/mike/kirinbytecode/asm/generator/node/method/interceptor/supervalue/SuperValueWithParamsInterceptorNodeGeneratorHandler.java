package ru.mike.kirinbytecode.asm.generator.node.method.interceptor.supervalue;

import com.google.auto.service.AutoService;
import org.objectweb.asm.Type;
import org.objectweb.asm.tree.MethodNode;
import ru.mike.kirinbytecode.asm.builder.InterceptorImplementation;
import ru.mike.kirinbytecode.asm.definition.MethodDefinition;
import ru.mike.kirinbytecode.asm.definition.proxy.ProxyClassDefinition;
import ru.mike.kirinbytecode.asm.generator.node.method.interceptor.InterceptorNodeGeneratorHandler;
import ru.mike.kirinbytecode.asm.matcher.SuperValue;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.Objects;

import static org.objectweb.asm.Opcodes.ALOAD;
import static org.objectweb.asm.Opcodes.ASTORE;
import static org.objectweb.asm.Opcodes.INVOKESPECIAL;
import static ru.mike.kirinbytecode.asm.exception.incorrect.IncorrectSuperMethodParametersException.throwParamsCountNotEquals;
import static ru.mike.kirinbytecode.asm.exception.incorrect.IncorrectSuperMethodParametersException.throwParamsIsNull;
import static ru.mike.kirinbytecode.asm.exception.incorrect.IncorrectSuperMethodParametersException.throwParamsTypesNotEquals;

@AutoService(InterceptorNodeGeneratorHandler.class)
public class SuperValueWithParamsInterceptorNodeGeneratorHandler<T> extends AbstractSuperValueInterceptorGeneratorHandler<T> {

    @Override
    public boolean isSuitableHandler(InterceptorImplementation implementation) {
        if (!(implementation instanceof SuperValue)) {
            return false;
        }

        SuperValue superValueImp = (SuperValue) implementation;

        return Objects.nonNull(superValueImp.getParams());
    }

    @Override
    public void generateSuperMethodCall(ProxyClassDefinition<T> definition, MethodDefinition<T> methodDefinition, MethodNode mn) {
        mn.visitVarInsn(ALOAD, 0);

        int parameterCount = methodDefinition.getMethod().getParameterCount();

        for (int i = 0; i < parameterCount; i++) {
            mn.visitVarInsn(ALOAD, i + 1);
        }

        mn.visitMethodInsn(
                INVOKESPECIAL,
                definition.getOriginalClazz().getName().replaceAll("\\.", "/"),
                methodDefinition.getMethod().getName(),
                Type.getMethodDescriptor(methodDefinition.getMethod()),
                false
        );
        mn.visitVarInsn(ASTORE, 1);
    }

    @Override
    public void checkSuperMethodParamsOrThrow(MethodDefinition<T> methodDefinition, InterceptorImplementation implementation) {
        SuperValue superValueImp = (SuperValue) implementation;
        Object[] transmittedParams = superValueImp.getParams();

        Method interceptedMethod = methodDefinition.getMethod();

//      проверяем, что переданные параметры не null, так как супер-метод точно должен быть с параметрами
        if (Objects.isNull(transmittedParams)) {
            throwParamsIsNull(interceptedMethod);
        }

        int transmittedParamsCount = transmittedParams.length;
        int originalParamsCount = interceptedMethod.getParameterCount();

//      проверяем, что длина передаваемых параметров == длине параметров супер метода
        if (transmittedParamsCount != originalParamsCount) {
            throwParamsCountNotEquals(transmittedParamsCount, originalParamsCount);
        }

        Parameter[] originalParams = interceptedMethod.getParameters();

//      сравниваем по порядку тип каждого переданного параметра с типом каждого параметра супер-метода
        for (int i = 0; i < transmittedParamsCount; i++) {
            Object transmittedParam = transmittedParams[i];
            Parameter originalParam = originalParams[i];
            Class<?> originalParamType = originalParam.getType();

            if (!originalParamType.isInstance(transmittedParam)) {
                throwParamsTypesNotEquals(i, transmittedParam, originalParamType);
            }
        }
    }
}
