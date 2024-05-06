package ru.mike.kirinbytecode.asm.generator.node.method.definer.implementation;

import com.google.auto.service.AutoService;
import lombok.extern.slf4j.Slf4j;
import org.objectweb.asm.tree.MethodNode;
import ru.mike.kirinbytecode.asm.builder.InterceptorImplementation;
import ru.mike.kirinbytecode.asm.definition.DefinedMethodDefinition;
import ru.mike.kirinbytecode.asm.definition.MethodDefinition;
import ru.mike.kirinbytecode.asm.definition.proxy.ProxyClassDefinition;
import ru.mike.kirinbytecode.asm.exception.IncomparableTypeException;
import ru.mike.kirinbytecode.asm.generator.name.FieldNameGenerator;
import ru.mike.kirinbytecode.asm.generator.node.method.NodeGeneratorHandler;
import ru.mike.kirinbytecode.asm.generator.node.method.annotation.MethodAnnotationGenerator;
import ru.mike.kirinbytecode.asm.generator.node.method.parameter.DefaultMethodParameterGenerator;
import ru.mike.kirinbytecode.asm.generator.node.method.parameter.MethodParameterGenerator;
import ru.mike.kirinbytecode.asm.matcher.CustomValue;
import ru.mike.kirinbytecode.asm.util.BytecodeGenHelper;

import java.util.Objects;
import java.util.function.Supplier;

import static org.objectweb.asm.Opcodes.*;
import static ru.mike.kirinbytecode.asm.exception.notfound.InterceptorImplementationNotFoundException.checkImplementationTypeOrThrow;
import static ru.mike.kirinbytecode.asm.util.AsmUtil.RETURNbyClass;
import static ru.mike.kirinbytecode.asm.util.AsmUtil.invokeVirtualWrapperValue;
import static sun.invoke.util.Wrapper.asPrimitiveType;
import static sun.invoke.util.Wrapper.isPrimitiveType;

@AutoService(NodeGeneratorHandler.class)
@Slf4j
public class CustomValueDefinerNodeGeneratorHandler<T> implements NodeGeneratorHandler<T> {
    private MethodParameterGenerator<T> methodParameterGenerator;
    private MethodAnnotationGenerator methodAnnotationGenerator;

    public CustomValueDefinerNodeGeneratorHandler() {
        this.methodParameterGenerator = new DefaultMethodParameterGenerator<>();
        this.methodAnnotationGenerator = new MethodAnnotationGenerator();
    }

    @Override
    public boolean isSuitableHandler(MethodDefinition<T> methodDefinition) {
        InterceptorImplementation implementation = methodDefinition.getImplementation();
        return Objects.nonNull(implementation) &&
                implementation instanceof CustomValue &&
                methodDefinition instanceof DefinedMethodDefinition;
    }

    @Override
    public MethodNode generateMethodNode(ProxyClassDefinition<T> definition, MethodDefinition<T> methodDefinition) {
        DefinedMethodDefinition<T> definedMethodDefinition = (DefinedMethodDefinition<T>) methodDefinition;
        InterceptorImplementation implementation = definedMethodDefinition.getImplementation();

        checkImplementationTypeOrThrow(CustomValue.class, implementation);

        String interceptedMethodName = definedMethodDefinition.getName();
        int modifiers = definedMethodDefinition.getModifiers();

        CustomValue<T> customValue = (CustomValue) implementation;
        Supplier<T> supp = customValue.getSupp();
        Class<?> suppGenericType = customValue.getReturnType();

//      если тип супплаера в конструкторе CustomValue примитив - то кидаем исключение
        if (suppGenericType.isPrimitive()) {
            IncomparableTypeException.throwDefault(suppGenericType, customValue.getClass());
        }

        Class<?> originalReturnType = definedMethodDefinition.getReturnType();

        String methodDescriptor = definedMethodDefinition.getMethodDescriptor();

        MethodNode mn = new MethodNode(ASM9, modifiers, interceptedMethodName, methodDescriptor, null, null);

        String fieldGeneratedName = new FieldNameGenerator().getGeneratedName(null);
        BytecodeGenHelper.generateSupplierCall(definition, mn, fieldGeneratedName, customValue.getReturnType());

        mn.visitVarInsn(ALOAD, 3);

//      если возвращаемый тип в оригинальном методе - примитив, то нужно вызвать value метод обертки
        if (isPrimitiveType(originalReturnType)) {
            invokeVirtualWrapperValue(mn, suppGenericType);
        }

//      asPrimitiveType вернет изначальный тип, если он окажется не примитивным, то есть в итоге вернется ARETURN
        mn.visitInsn(RETURNbyClass(asPrimitiveType(suppGenericType)));

//      создаем и добавляем FieldDefinition в мапу, чтобы в LoadedType.fillEmptyFields()
//      заполнить сгенерированное поле
        definition.getProxyClassFieldsDefinition().createFieldDefinition(
                fieldGeneratedName,
                supp,
                Supplier.class,
                ACC_PUBLIC
        );

//      проставляем аннотации методу
        methodAnnotationGenerator.visitMethodAnnotations(definedMethodDefinition, mn);
        //      проставляем параметры методу
        methodParameterGenerator.visitMethodParameters(definedMethodDefinition, mn);
//      для всех параметров проставляем аннотации
        methodParameterGenerator.visitParametersAnnotations(definedMethodDefinition.getParameterDefinitions(), mn);

        return mn;
    }
}
