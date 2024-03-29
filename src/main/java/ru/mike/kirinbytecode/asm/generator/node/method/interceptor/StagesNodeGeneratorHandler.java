package ru.mike.kirinbytecode.asm.generator.node.method.interceptor;

import org.objectweb.asm.tree.MethodNode;
import ru.mike.kirinbytecode.asm.definition.AfterMethodDefinition;
import ru.mike.kirinbytecode.asm.definition.BeforeMethodDefinition;
import ru.mike.kirinbytecode.asm.definition.StageMethodDefinition;
import ru.mike.kirinbytecode.asm.definition.proxy.ProxyClassDefinition;

import java.util.Objects;

import static org.objectweb.asm.Opcodes.ACC_PUBLIC;
import static org.objectweb.asm.Opcodes.ALOAD;
import static org.objectweb.asm.Opcodes.GETFIELD;
import static org.objectweb.asm.Opcodes.INVOKEINTERFACE;
import static ru.mike.kirinbytecode.asm.definition.StageMethodDefinition.RUNNABLE_FIELD_DESCRIPTOR;

public interface StagesNodeGeneratorHandler<T> extends InterceptorNodeGeneratorHandler<T> {

    default void generateBeforeMethodDefinitionCall(ProxyClassDefinition<T> definition, BeforeMethodDefinition beforeMethodDefinition, MethodNode mn) {
        generateStageMethodDefinitionCall(definition, beforeMethodDefinition, mn);
    }

    default void generateAfterMethodDefinitionCall(ProxyClassDefinition<T> definition, AfterMethodDefinition afterMethodDefinition, MethodNode mn) {
        generateStageMethodDefinitionCall(definition, afterMethodDefinition, mn);
    }

    private void generateStageMethodDefinitionCall(ProxyClassDefinition<T> definition, StageMethodDefinition stageMethodDefinition, MethodNode mn) {
        if (Objects.isNull(stageMethodDefinition)) {
            return;
        }

        String fieldGeneratedName = stageMethodDefinition
                .getFieldNameGenerator()
                .getGeneratedName(null);

        mn.visitVarInsn(ALOAD, 0);

        mn.visitFieldInsn(
                GETFIELD,
                definition.getProxyPackageName().replaceAll("\\.", "/") + "/" + definition.getNameGenerator().getGeneratedName(definition.getOriginalClazz().getSimpleName()),
                fieldGeneratedName,
                RUNNABLE_FIELD_DESCRIPTOR
        );

        mn.visitMethodInsn(INVOKEINTERFACE, "java/lang/Runnable", "run", "()V", true);

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
