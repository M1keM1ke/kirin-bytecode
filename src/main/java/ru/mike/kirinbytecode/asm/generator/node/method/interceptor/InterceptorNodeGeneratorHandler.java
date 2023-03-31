package ru.mike.kirinbytecode.asm.generator.node.method.interceptor;

import org.objectweb.asm.Type;
import org.objectweb.asm.tree.MethodNode;
import ru.mike.kirinbytecode.asm.builder.InterceptorImplementation;
import ru.mike.kirinbytecode.asm.definition.AfterMethodDefinition;
import ru.mike.kirinbytecode.asm.definition.BeforeMethodDefinition;
import ru.mike.kirinbytecode.asm.definition.MethodDefinition;
import ru.mike.kirinbytecode.asm.definition.StageMethodDefinition;
import ru.mike.kirinbytecode.asm.definition.proxy.ProxyClassDefinition;

import java.util.Objects;

import static org.objectweb.asm.Opcodes.ACC_PUBLIC;
import static org.objectweb.asm.Opcodes.ALOAD;
import static org.objectweb.asm.Opcodes.ASTORE;
import static org.objectweb.asm.Opcodes.GETFIELD;
import static org.objectweb.asm.Opcodes.INVOKEINTERFACE;
import static org.objectweb.asm.Opcodes.INVOKESPECIAL;
import static ru.mike.kirinbytecode.asm.definition.BeforeMethodDefinition.RUNNABLE_BEFORE_FIELD_DESCRIPTOR;

public interface InterceptorNodeGeneratorHandler<T> {

    boolean isSuitableHandler(InterceptorImplementation implementation);

    MethodNode generateMethodNode(ProxyClassDefinition<T> definition, MethodDefinition<T> methodDefinition);

    default void generateSuperMethodCall(ProxyClassDefinition<T> definition, MethodDefinition<T> methodDefinition, MethodNode mn) {
        mn.visitVarInsn(ALOAD, 0);
        mn.visitMethodInsn(
                INVOKESPECIAL,
                definition.getOriginalClazz().getName().replaceAll("\\.", "/"),
                methodDefinition.getMethod().getName(),
                Type.getMethodDescriptor(methodDefinition.getMethod()),
                false
        );
        mn.visitVarInsn(ASTORE, 1);
    }

    default void generateBeforeMethodDefinitionCall(ProxyClassDefinition<T> definition, BeforeMethodDefinition beforeMethodDefinition, MethodNode methodNode) {
        generateStageMethodDefinitionCall(definition, beforeMethodDefinition, methodNode);
    }

    default void generateAfterMethodDefinitionCall(ProxyClassDefinition<T> definition, AfterMethodDefinition afterMethodDefinition, MethodNode methodNode) {
        generateStageMethodDefinitionCall(definition, afterMethodDefinition, methodNode);
    }

    private void generateStageMethodDefinitionCall(ProxyClassDefinition<T> definition, StageMethodDefinition stageMethodDefinition, MethodNode methodNode) {
        if (Objects.isNull(stageMethodDefinition)) {
            return;
        }

        String fieldGeneratedName = stageMethodDefinition
                .getFieldNameGenerator()
                .getGeneratedName(null);

        methodNode.visitVarInsn(ALOAD, 0);

        methodNode.visitFieldInsn(
                GETFIELD,
                definition.getProxyPackageName().replaceAll("\\.", "/") + "/" + definition.getNameGenerator().getGeneratedName(definition.getOriginalClazz().getSimpleName()),
                fieldGeneratedName,
                RUNNABLE_BEFORE_FIELD_DESCRIPTOR
        );

        methodNode.visitMethodInsn(INVOKEINTERFACE, "java/lang/Runnable", "run", "()V", true);

//      создаем и добавляем FieldDefinition в мапу, чтобы в LoadedType.fillEmptyFields()
//      заполнить сгенерированное поле значением stageMethodDefinition.getRunnableStage()
        definition.getProxyClassFieldsDefinition().createFieldDefinition(
                fieldGeneratedName,
                stageMethodDefinition.getRunnableStage(),
                Runnable.class,
                ACC_PUBLIC
        );
    }
}
