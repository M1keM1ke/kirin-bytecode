package ru.mike.kirinbytecode.asm.generator.node.method.interceptor.implementation;

import com.google.auto.service.AutoService;
import org.objectweb.asm.tree.MethodNode;
import ru.mike.kirinbytecode.asm.builder.InterceptorImplementation;
import ru.mike.kirinbytecode.asm.definition.InterceptedMethodDefinition;
import ru.mike.kirinbytecode.asm.definition.MethodDefinition;
import ru.mike.kirinbytecode.asm.definition.proxy.ProxyClassDefinition;
import ru.mike.kirinbytecode.asm.generator.name.FieldNameGenerator;
import ru.mike.kirinbytecode.asm.generator.node.method.NodeGeneratorHandler;
import ru.mike.kirinbytecode.asm.generator.node.method.annotation.MethodAnnotationGenerator;
import ru.mike.kirinbytecode.asm.generator.node.method.interceptor.StagesNodeGeneratorHandler;
import ru.mike.kirinbytecode.asm.matcher.CustomValue;

import java.lang.reflect.Modifier;
import java.util.Objects;
import java.util.function.Supplier;

import static jdk.dynalink.linker.support.TypeUtilities.isWrapperType;
import static org.objectweb.asm.Opcodes.ACC_PUBLIC;
import static org.objectweb.asm.Opcodes.ALOAD;
import static org.objectweb.asm.Opcodes.ARETURN;
import static org.objectweb.asm.Opcodes.ASM9;
import static ru.mike.kirinbytecode.asm.exception.incorrect.IncorrectModifierException.throwIncorrectFinal;
import static ru.mike.kirinbytecode.asm.exception.incorrect.IncorrectModifierException.throwIncorrectPrivate;
import static ru.mike.kirinbytecode.asm.exception.notfound.InterceptorImplementationNotFoundException.checkImplementationTypeOrThrow;
import static ru.mike.kirinbytecode.asm.util.BytecodeGenHelper.generateSupplierCall;
import static sun.invoke.util.Wrapper.asPrimitiveType;

@AutoService(NodeGeneratorHandler.class)
public class CustomValueInterceptorNodeGeneratorHandler<T> implements StagesNodeGeneratorHandler<T> {
    private MethodAnnotationGenerator methodAnnotationGenerator;

    public CustomValueInterceptorNodeGeneratorHandler() {
        this.methodAnnotationGenerator = new MethodAnnotationGenerator();
    }

    @Override
    public boolean isSuitableHandler(MethodDefinition<T> methodDefinition) {
        InterceptorImplementation implementation = methodDefinition.getImplementation();
        return Objects.nonNull(implementation) &&
                implementation instanceof CustomValue &&
                methodDefinition instanceof InterceptedMethodDefinition;
    }

    @Override
    public MethodNode generateMethodNode(ProxyClassDefinition<T> definition, MethodDefinition<T> methodDefinition) {
        InterceptedMethodDefinition<T> interceptedMethodDefinition = (InterceptedMethodDefinition<T>) methodDefinition;
        InterceptorImplementation implementation = methodDefinition.getImplementation();

        checkImplementationTypeOrThrow(CustomValue.class, implementation);

        String interceptedMethodName = interceptedMethodDefinition.getName();
        int modifiers = interceptedMethodDefinition.getModifiers();

        CustomValue<T> customValue = (CustomValue) implementation;
        Supplier<T> supp = customValue.getSupp();
        Class<?> suppGenericType = customValue.getReturnType();
        Class<?> originalReturnType = interceptedMethodDefinition.getReturnType();

//      если возвращаемый тип в оригинальном методе класса - примитивный тип, а тип значения для
//      прокси метода - обертка, то необходимо скастить обертку к примитиву, иначе будет ReturnTypeCastException
        if (originalReturnType.isPrimitive() && isWrapperType(suppGenericType)) {
            suppGenericType = asPrimitiveType(suppGenericType);
        }

        if (!originalReturnType.isAssignableFrom(suppGenericType)) {
            throw new RuntimeException();
        }

        checkModifiersCorrectOrThrow(definition, interceptedMethodName, modifiers);


        String methodDescriptor = interceptedMethodDefinition.getMethodDescriptor();

        MethodNode mn = new MethodNode(ASM9, modifiers, interceptedMethodName, methodDescriptor, null, null);
        generateBeforeMethodDefinitionCall(definition, interceptedMethodDefinition.getBeforeMethodDefinition(), mn);

        String fieldGeneratedName = new FieldNameGenerator().getGeneratedName(null);

        generateSupplierCall(definition, mn, fieldGeneratedName);
        generateAfterMethodDefinitionCall(definition, interceptedMethodDefinition.getAfterMethodDefinition(), mn);

        mn.visitVarInsn(ALOAD, 3);
        mn.visitInsn(ARETURN);

//      создаем и добавляем FieldDefinition в мапу, чтобы в LoadedType.fillEmptyFields()
//      заполнить сгенерированное поле
        definition.getProxyClassFieldsDefinition().createFieldDefinition(
                fieldGeneratedName,
                supp,
                Supplier.class,
                ACC_PUBLIC
        );

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
