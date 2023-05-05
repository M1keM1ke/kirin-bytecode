package ru.mike.kirinbytecode.asm.generator.node.method.interceptor.fixedvalue;

import com.google.auto.service.AutoService;
import lombok.extern.log4j.Log4j2;
import org.objectweb.asm.tree.MethodNode;
import ru.mike.kirinbytecode.asm.builder.InterceptorImplementation;
import ru.mike.kirinbytecode.asm.definition.MethodDefinition;
import ru.mike.kirinbytecode.asm.definition.proxy.ProxyClassDefinition;
import ru.mike.kirinbytecode.asm.exception.ReturnTypeCastException;
import ru.mike.kirinbytecode.asm.generator.node.method.interceptor.InterceptorNodeGeneratorHandler;
import ru.mike.kirinbytecode.asm.generator.node.method.type.original.OriginalReturnTypeChainBuilder;
import ru.mike.kirinbytecode.asm.matcher.FixedValue;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Objects;

import static jdk.dynalink.linker.support.TypeUtilities.isWrapperType;
import static org.objectweb.asm.Opcodes.ASM9;
import static ru.mike.kirinbytecode.asm.exception.incorrect.IncorrectModifierException.throwIncorrectFinal;
import static ru.mike.kirinbytecode.asm.exception.notfound.InterceptorImplementationNotFoundException.checkImplementationTypeOrThrow;
import static sun.invoke.util.Wrapper.asPrimitiveType;

@Log4j2
@AutoService(InterceptorNodeGeneratorHandler.class)
public class FixedValueInterceptorNodeGeneratorHandler<T> implements InterceptorNodeGeneratorHandler<T> {

    @Override
    public boolean isSuitableHandler(InterceptorImplementation implementation) {
        return implementation instanceof FixedValue;
    }

    @Override
    public MethodNode generateMethodNode(ProxyClassDefinition<T> definition, MethodDefinition<T> methodDefinition) {
        InterceptorImplementation implementation = methodDefinition.getImplementation();

        checkImplementationTypeOrThrow(FixedValue.class, implementation);

        FixedValue fixedValueImp = (FixedValue) implementation;
        Object interceptedReturnValue = fixedValueImp.getValue();
        Method interceptedMethod = methodDefinition.getMethod();
        String interceptedMethodName = interceptedMethod.getName();
        int modifiers = interceptedMethod.getModifiers();

        checkModifiersCorrectOrThrow(definition, interceptedMethodName, modifiers);

        Class<?> originalReturnType = interceptedMethod.getReturnType();
        Class<?> interceptedReturnType = interceptedReturnValue.getClass();

//      если возвращаемый тип в оригинальном методе класса - примитивный тип, а тип значения для
//      прокси метода - обертка, то необходимо скастить обертку к примитиву, иначе будет ReturnTypeCastException
        if (originalReturnType.isPrimitive() && isWrapperType(interceptedReturnType)) {
            interceptedReturnType = asPrimitiveType(interceptedReturnType);
        }

        if (!Objects.equals(originalReturnType, interceptedReturnType)) {
            throw new ReturnTypeCastException("Unable to intercept method:" + interceptedMethod +
                    ". Return types are not equals. Expected:'" + originalReturnType + "', but got " + interceptedReturnType);
        }

        String methodDescriptor = methodDefinition.getMethodDescriptor();

        MethodNode mn = new MethodNode(ASM9, modifiers, interceptedMethodName, methodDescriptor, null, null);

        generateBeforeMethodDefinitionCall(definition, methodDefinition.getBeforeMethodDefinition(), mn);
        generateAfterMethodDefinitionCall(definition, methodDefinition.getAfterMethodDefinition(), mn);

//      определяем тип возвращаемого значения проксируемого метода и в зависимости от него генерируем прокси метод
        OriginalReturnTypeChainBuilder.buildChain().generate(mn, definition, methodDefinition);

        return mn;
    }

    @Override
    public void checkModifiersCorrectOrThrow(ProxyClassDefinition<T> definition, String interceptedMethodName, int modifiers) {
//      если в супер классе метод, который мы хотим переопределить в прокси классе - приватный, то кидаем предупреждение,
//      что в прокси классе данный метод не будет переопределен, а будет являться просто новым методом с таким же именем,
//      как в супер классе
        if (Modifier.isPrivate(modifiers)) {
            log.warn("Super method '{}' in class '{}' has private modifier. " +
                    "Generated proxy method with same name can't be overridden. " +
                    "It's will be just new method with same name '{}'. " +
                    "Change method modifier in super class or remove proxying this method.",
                    interceptedMethodName, definition.getOriginalClazz().getName(), interceptedMethodName
            );
        }

//      если в супер классе метод, который мы хотим переопределить в прокси классе - финальный, то кидаем исключение,
//      так как в такой метод не может быть переопределен
        if (Modifier.isFinal(modifiers)) {
            throwIncorrectFinal(definition.getOriginalClazz().getName(), interceptedMethodName);
        }
    }
}
