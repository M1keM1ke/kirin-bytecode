package ru.mike.kirinbytecode.asm.generator.node.method.interceptor.implementation;

import com.google.auto.service.AutoService;
import lombok.extern.log4j.Log4j2;
import org.objectweb.asm.tree.MethodNode;
import ru.mike.kirinbytecode.asm.builder.InterceptorImplementation;
import ru.mike.kirinbytecode.asm.definition.InterceptedMethodDefinition;
import ru.mike.kirinbytecode.asm.definition.MethodDefinition;
import ru.mike.kirinbytecode.asm.definition.proxy.ProxyClassDefinition;
import ru.mike.kirinbytecode.asm.exception.ReturnTypeCastException;
import ru.mike.kirinbytecode.asm.generator.node.method.NodeGeneratorHandler;
import ru.mike.kirinbytecode.asm.generator.node.method.annotation.MethodAnnotationGenerator;
import ru.mike.kirinbytecode.asm.generator.node.method.interceptor.StagesNodeGeneratorHandler;
import ru.mike.kirinbytecode.asm.generator.node.method.type.original.OriginalReturnTypeChainBuilder;
import ru.mike.kirinbytecode.asm.matcher.FixedValue;

import java.lang.reflect.Modifier;
import java.util.Objects;

import static jdk.dynalink.linker.support.TypeUtilities.isWrapperType;
import static org.objectweb.asm.Opcodes.ASM9;
import static ru.mike.kirinbytecode.asm.exception.incorrect.IncorrectModifierException.throwIncorrectFinal;
import static ru.mike.kirinbytecode.asm.exception.incorrect.IncorrectModifierException.throwIncorrectPrivate;
import static ru.mike.kirinbytecode.asm.exception.notfound.InterceptorImplementationNotFoundException.checkImplementationTypeOrThrow;
import static sun.invoke.util.Wrapper.asPrimitiveType;

@Log4j2
@AutoService(NodeGeneratorHandler.class)
public class FixedValueInterceptorNodeGeneratorHandler<T> implements StagesNodeGeneratorHandler<T> {
    private MethodAnnotationGenerator methodAnnotationGenerator;

    public FixedValueInterceptorNodeGeneratorHandler() {
        this.methodAnnotationGenerator = new MethodAnnotationGenerator();
    }

    @Override
    public boolean isSuitableHandler(MethodDefinition<T> methodDefinition) {
        InterceptorImplementation implementation = methodDefinition.getImplementation();
        return Objects.nonNull(implementation) &&
                implementation instanceof FixedValue &&
                methodDefinition instanceof InterceptedMethodDefinition;
    }

    @Override
    public MethodNode generateMethodNode(ProxyClassDefinition<T> definition, MethodDefinition<T> methodDefinition) {
        InterceptedMethodDefinition<T> interceptedMethodDefinition = (InterceptedMethodDefinition<T>) methodDefinition;
        InterceptorImplementation implementation = methodDefinition.getImplementation();

        checkImplementationTypeOrThrow(FixedValue.class, implementation);

        FixedValue fixedValueImp = (FixedValue) implementation;
        Object interceptedReturnValue = fixedValueImp.getValue();
        String interceptedMethodName = interceptedMethodDefinition.getName();
        int modifiers = interceptedMethodDefinition.getModifiers();

        checkModifiersCorrectOrThrow(definition, interceptedMethodName, modifiers);

        Class<?> originalReturnType = interceptedMethodDefinition.getReturnType();
        Class<?> interceptedReturnType = interceptedReturnValue.getClass();

//      если возвращаемый тип в оригинальном методе класса - примитивный тип, а тип значения для
//      прокси метода - обертка, то необходимо скастить обертку к примитиву, иначе будет ReturnTypeCastException
        if (originalReturnType.isPrimitive() && isWrapperType(interceptedReturnType)) {
            interceptedReturnType = asPrimitiveType(interceptedReturnType);
        }

        if (!Objects.equals(originalReturnType, interceptedReturnType)) {
            throw new ReturnTypeCastException("Unable to intercept method:" + interceptedMethodDefinition +
                    ". Return types are not equals. Expected:'" + originalReturnType + "', but got " + interceptedReturnType);
        }

        String methodDescriptor = interceptedMethodDefinition.getMethodDescriptor();

        MethodNode mn = new MethodNode(ASM9, modifiers, interceptedMethodName, methodDescriptor, null, null);

        generateBeforeMethodDefinitionCall(definition, interceptedMethodDefinition.getBeforeMethodDefinition(), mn);
        generateAfterMethodDefinitionCall(definition, interceptedMethodDefinition.getAfterMethodDefinition(), mn);

//      определяем тип возвращаемого значения проксируемого метода и в зависимости от него генерируем прокси метод
        OriginalReturnTypeChainBuilder.buildChain().generate(mn, definition, interceptedMethodDefinition);

//      проставляем аннотации методу
        methodAnnotationGenerator.visitMethodAnnotations(interceptedMethodDefinition, mn);

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
