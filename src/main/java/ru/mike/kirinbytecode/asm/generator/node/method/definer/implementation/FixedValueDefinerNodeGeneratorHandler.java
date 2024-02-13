package ru.mike.kirinbytecode.asm.generator.node.method.definer.implementation;

import com.google.auto.service.AutoService;
import org.objectweb.asm.tree.MethodNode;
import ru.mike.kirinbytecode.asm.builder.InterceptorImplementation;
import ru.mike.kirinbytecode.asm.definition.DefinedMethodDefinition;
import ru.mike.kirinbytecode.asm.definition.MethodDefinition;
import ru.mike.kirinbytecode.asm.definition.proxy.ProxyClassDefinition;
import ru.mike.kirinbytecode.asm.generator.node.method.NodeGeneratorHandler;
import ru.mike.kirinbytecode.asm.generator.node.method.annotation.MethodAnnotationGenerator;
import ru.mike.kirinbytecode.asm.generator.node.method.parameter.DefaultMethodParameterGenerator;
import ru.mike.kirinbytecode.asm.generator.node.method.parameter.MethodParameterGenerator;
import ru.mike.kirinbytecode.asm.generator.node.method.type.original.OriginalReturnTypeChainBuilder;
import ru.mike.kirinbytecode.asm.matcher.FixedValue;

import java.util.Objects;

import static org.objectweb.asm.Opcodes.ASM9;
import static ru.mike.kirinbytecode.asm.exception.notfound.InterceptorImplementationNotFoundException.checkImplementationTypeOrThrow;

@AutoService(NodeGeneratorHandler.class)
public class FixedValueDefinerNodeGeneratorHandler<T> implements NodeGeneratorHandler<T> {
    private MethodParameterGenerator<T> methodParameterGenerator;
    private MethodAnnotationGenerator methodAnnotationGenerator;

    public FixedValueDefinerNodeGeneratorHandler() {
        this.methodParameterGenerator = new DefaultMethodParameterGenerator<>();
        this.methodAnnotationGenerator = new MethodAnnotationGenerator();
    }

    @Override
    public boolean isSuitableHandler(MethodDefinition<T> methodDefinition) {
        InterceptorImplementation implementation = methodDefinition.getImplementation();
        return Objects.nonNull(implementation) &&
                implementation instanceof FixedValue &&
                methodDefinition instanceof DefinedMethodDefinition;
    }

    @Override
    public MethodNode generateMethodNode(ProxyClassDefinition<T> definition, MethodDefinition<T> methodDefinition) {
        DefinedMethodDefinition<T> definedMethodDefinition = (DefinedMethodDefinition<T>) methodDefinition;

        checkImplementationTypeOrThrow(FixedValue.class, definedMethodDefinition.getImplementation());

        MethodNode mn = new MethodNode(ASM9, definedMethodDefinition.getModifiers(), definedMethodDefinition.getName(), definedMethodDefinition.getMethodDescriptor(), null, null);

//      проставляем аннотации методу
        methodAnnotationGenerator.visitMethodAnnotations(definedMethodDefinition, mn);
//      проставляем параметры методу
        methodParameterGenerator.visitMethodParameters(definedMethodDefinition, mn);
//      для всех параметров проставляем аннотации
        methodParameterGenerator.visitParametersAnnotations(definedMethodDefinition.getParameterDefinitions(), mn);

//      определяем тип возвращаемого значения проксируемого метода и в зависимости от него генерируем прокси метод
        OriginalReturnTypeChainBuilder.buildChain().generate(mn, definition, definedMethodDefinition);

        return mn;
    }
}
