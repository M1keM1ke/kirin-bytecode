package ru.mike.kirinbytecode.asm.generator.node.method.interceptor.implementation;

import com.google.auto.service.AutoService;
import org.objectweb.asm.tree.MethodNode;
import ru.mike.kirinbytecode.asm.builder.InterceptorImplementation;
import ru.mike.kirinbytecode.asm.definition.InterceptedMethodDefinition;
import ru.mike.kirinbytecode.asm.definition.MethodDefinition;
import ru.mike.kirinbytecode.asm.definition.parameter.ParameterDefinition;
import ru.mike.kirinbytecode.asm.definition.proxy.ProxyClassDefinition;
import ru.mike.kirinbytecode.asm.generator.node.method.NodeGeneratorHandler;
import ru.mike.kirinbytecode.asm.generator.node.method.interceptor.SuperMethodCallProperties;
import ru.mike.kirinbytecode.asm.matcher.SuperValue;
import ru.mike.kirinbytecode.asm.util.AsmUtil;

import java.util.List;
import java.util.Objects;

import static jdk.dynalink.linker.support.TypeUtilities.isWrapperType;
import static org.objectweb.asm.Opcodes.ALOAD;
import static org.objectweb.asm.Opcodes.INVOKESPECIAL;
import static ru.mike.kirinbytecode.asm.exception.incorrect.IncorrectSuperMethodParametersException.throwParamsCountNotEquals;
import static ru.mike.kirinbytecode.asm.exception.incorrect.IncorrectSuperMethodParametersException.throwParamsIsNull;
import static ru.mike.kirinbytecode.asm.exception.incorrect.IncorrectSuperMethodParametersException.throwParamsTypesNotEquals;
import static ru.mike.kirinbytecode.asm.util.AsmUtil.LOADbyClass;
import static sun.invoke.util.Wrapper.asPrimitiveType;

@AutoService(NodeGeneratorHandler.class)
public class SuperValueWithParamsInterceptorNodeGeneratorHandler<T> extends AbstractSuperValueInterceptorGeneratorHandler<T> {

    @Override
    public boolean isSuitableHandler(MethodDefinition<T> methodDefinition) {
        InterceptorImplementation implementation = methodDefinition.getImplementation();
        return Objects.nonNull(implementation) &&
                implementation instanceof SuperValue &&
                methodDefinition instanceof InterceptedMethodDefinition &&
                Objects.nonNull(((SuperValue) implementation).getParams())
        ;
    }

    @Override
    public SuperMethodCallProperties generateSuperMethodCall(ProxyClassDefinition<T> definition, InterceptedMethodDefinition<T> methodDefinition, MethodNode mn) {
        mn.visitVarInsn(ALOAD, 0);

        int paramNumber = generateLOADOpcodesForMethodParams(methodDefinition, mn);

//      вызываем супер-метод в прокси методе
        mn.visitMethodInsn(
                INVOKESPECIAL,
                definition.getOriginalClazz().getName().replaceAll("\\.", "/"),
                methodDefinition.getName(),
                methodDefinition.getMethodDescriptor(),
                false
        );

//      считываем из стека значение переменной, сохраненной после вызова супер-метода при помощи INVOKESPECIAL
        mn.visitVarInsn(AsmUtil.STOREbyClass(methodDefinition.getReturnType()), paramNumber);

        return SuperMethodCallProperties.builder()
                .lastLOADOpcodeNumber(paramNumber)
                .build();
    }

    @Override
    public void checkSuperMethodParamsOrThrow(InterceptedMethodDefinition<T> methodDefinition, InterceptorImplementation implementation) {
        SuperValue superValueImp = (SuperValue) implementation;

        Object[] transmittedParams = superValueImp.getParams();

        int transmittedParamsCount = transmittedParams.length;
        int originalParamsCount = methodDefinition.getParameterCount();

        checkTransmittedParamsNonNullOrThrow(transmittedParams, methodDefinition);
        checkTransmittedParamsCountEqualsOrThrow(transmittedParamsCount, originalParamsCount);
        checkTransmittedParamsTypesOrThrow(transmittedParams, methodDefinition);
    }

    /**
     * Генерирует последовательно загрузку параметров проксируемого метода на стек при помощи вызова опкодов <br>
     *  {@link org.objectweb.asm.Opcodes#ILOAD} <br> {@link org.objectweb.asm.Opcodes#DLOAD} <br>
     *  {@link org.objectweb.asm.Opcodes#LLOAD} <br> {@link org.objectweb.asm.Opcodes#ALOAD} <br>
     *  Опкод выбирается в зависимости от типа параметра. Также высчитывается индекс при сохранении на стек,
     *  которой указывается после опкода. Для типов long и double смещение == 2, для остальных == 1. Например,
     *  для метода public void someMethod(String str, int i, long l, short sh, double d) загрузка будет такая: <br>
     *  ALOAD 1 <br>
     *  ILOAD 2 <br>
     *  LLOAD 3 <br>
     *  ILOAD 5 <br>
     *  DLOAD 6 <br>
     *
     * @param interceptedMethod проксируемый метод
     * @param mn нода, для которой происходит генерация загрузки параметров прокси метода
     * @return последний индекс + 1, который был указан в последнем опкоде.
     * Например, для примера выше индекс будет равен 7
     */
    private int generateLOADOpcodesForMethodParams(InterceptedMethodDefinition<T> interceptedMethod, MethodNode mn) {
        List<ParameterDefinition> methodParams = interceptedMethod.getParameterDefinitions();
        int parameterCount = interceptedMethod.getParameterCount();
        int paramNumber = 1;

        for (int i = 0; i < parameterCount; i++) {
            if (paramNumber == 1) {
                Class<?> methodParamType = methodParams.get(0).getType();
                mn.visitVarInsn(LOADbyClass(methodParamType), paramNumber);
                paramNumber++;
                continue;
            }

            if (paramNumber == parameterCount) {
                Class<?> lastMethodParamType = methodParams.get(methodParams.size() - 1).getType();

                if ((Objects.equals(long.class, lastMethodParamType) ||
                        Objects.equals(double.class, lastMethodParamType))) {
                    paramNumber++;
                }

                mn.visitVarInsn(LOADbyClass(lastMethodParamType), paramNumber);
                paramNumber++;
                continue;
            }

            Class<?> previousMethodParamType = methodParams.get(i - 1).getType();

//          для long и double смещение == 2, так как они двухбайтовые
            if ((Objects.equals(long.class, previousMethodParamType) ||
                    Objects.equals(double.class, previousMethodParamType))) {
                paramNumber++;
            }

            mn.visitVarInsn(LOADbyClass(previousMethodParamType), paramNumber);

            paramNumber++;
        }

        return paramNumber;
    }

    private void checkTransmittedParamsNonNullOrThrow(Object[] transmittedParams, InterceptedMethodDefinition<T> interceptedMethod) {
//      проверяем, что переданные параметры не null, так как супер-метод точно должен быть с параметрами
        if (Objects.isNull(transmittedParams)) {
            throwParamsIsNull(interceptedMethod);
        }
    }

    private void checkTransmittedParamsCountEqualsOrThrow(int transmittedParamsCount, int originalParamsCount) {
//      проверяем, что длина передаваемых параметров == длине параметров супер метода
        if (transmittedParamsCount != originalParamsCount) {
            throwParamsCountNotEquals(transmittedParamsCount, originalParamsCount);
        }
    }

    /**
     * Сравнивает по порядку тип каждого переданного параметра с типом каждого параметра супер-метода, например,
     * в супер-методе 3 параметра: String, long, Object. Нам необходимо проверить, что пользователь передал
     * значения таких же типов и в том же порядке, например: "str", 10l, new Object()
     *  @param transmittedParams массив параметров, переданных пользователем
     * @param interceptedMethod проксируемый метод из оригинального класса
     */
    private void checkTransmittedParamsTypesOrThrow(Object[] transmittedParams, InterceptedMethodDefinition<T> interceptedMethod) {
        List<ParameterDefinition> originalParams = interceptedMethod.getParameterDefinitions();
        int transmittedParamsCount = transmittedParams.length;

//      сравниваем по порядку тип каждого переданного параметра с типом каждого параметра супер-метода
        for (int i = 0; i < transmittedParamsCount; i++) {
            Object transmittedParam = transmittedParams[i];
            Class<?> transmittedParamType = transmittedParam.getClass();
            ParameterDefinition originalParam = originalParams.get(i);
            Class<?> originalParamType = originalParam.getType();

//          если возвращаемый тип в оригинальном методе класса - примитивный тип, а тип значения для
//          прокси метода - обертка, то необходимо скастить обертку к примитиву, иначе будет ReturnTypeCastException
            if (originalParamType.isPrimitive() && isWrapperType(transmittedParamType)) {
                transmittedParamType = asPrimitiveType(transmittedParam.getClass());
            }

//          пользователь может передать реализацию в качестве значения параметра,
//          например, ArrayList, в то время как в супер-методе параметром является List
            if (!originalParamType.isAssignableFrom(transmittedParamType)) {
                throwParamsTypesNotEquals(interceptedMethod.getName(), i, transmittedParam, originalParamType);
            }
        }
    }
}
