package ru.mike.kirinbytecode.asm.generator.node.method.definer.implementation;

import com.google.auto.service.AutoService;
import org.objectweb.asm.tree.MethodNode;
import ru.mike.kirinbytecode.asm.builder.InterceptorImplementation;
import ru.mike.kirinbytecode.asm.definition.DefinedMethodDefinition;
import ru.mike.kirinbytecode.asm.definition.MethodDefinition;
import ru.mike.kirinbytecode.asm.definition.proxy.ProxyClassDefinition;
import ru.mike.kirinbytecode.asm.generator.name.FieldNameGenerator;
import ru.mike.kirinbytecode.asm.generator.node.annotation.DefaultAnnotationGenerator;
import ru.mike.kirinbytecode.asm.generator.node.method.NodeGeneratorHandler;
import ru.mike.kirinbytecode.asm.generator.node.method.parameter.DefaultMethodParameterGenerator;
import ru.mike.kirinbytecode.asm.generator.node.method.parameter.MethodParameterGenerator;
import ru.mike.kirinbytecode.asm.matcher.CustomValue;
import ru.mike.kirinbytecode.asm.util.BytecodeGenHelper;

import java.util.Objects;
import java.util.function.Supplier;

import static org.objectweb.asm.Opcodes.ACC_PUBLIC;
import static org.objectweb.asm.Opcodes.ALOAD;
import static org.objectweb.asm.Opcodes.ARETURN;
import static org.objectweb.asm.Opcodes.ASM9;
import static ru.mike.kirinbytecode.asm.exception.notfound.InterceptorImplementationNotFoundException.checkImplementationTypeOrThrow;

@AutoService(NodeGeneratorHandler.class)
public class CustomValueDefinerNodeGeneratorHandler<T> implements NodeGeneratorHandler<T> {
    private MethodParameterGenerator<T> methodParameterGenerator;
    private DefaultAnnotationGenerator defaultAnnotationGenerator;

    public CustomValueDefinerNodeGeneratorHandler() {
        this.methodParameterGenerator = new DefaultMethodParameterGenerator<>();
        this.defaultAnnotationGenerator = new DefaultAnnotationGenerator();
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
        InterceptorImplementation implementation = methodDefinition.getImplementation();

        checkImplementationTypeOrThrow(CustomValue.class, implementation);

        String interceptedMethodName = definedMethodDefinition.getName();
        int modifiers = definedMethodDefinition.getModifiers();

        CustomValue<T> customValue = (CustomValue) implementation;
        Supplier<T> supp = customValue.getSupp();


        String methodDescriptor = definedMethodDefinition.getMethodDescriptor();

        MethodNode mn = new MethodNode(ASM9, modifiers, interceptedMethodName, methodDescriptor, null, null);

        String fieldGeneratedName = new FieldNameGenerator().getGeneratedName(null);
        BytecodeGenHelper.generateSupplierCall(definition, mn, fieldGeneratedName);

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


        //      проставляем параметры методу
        methodParameterGenerator.visitMethodParameters(definedMethodDefinition, mn);
//      для всех параметров проставляем аннотации
        defaultAnnotationGenerator.visitParametersAnnotations(methodDefinition.getParameterDefinitions(), mn);

        return mn;
    }
}
