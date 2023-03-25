package ru.mike.kirinbytecode.asm.generator.node;

import lombok.Getter;
import org.objectweb.asm.tree.MethodNode;
import ru.mike.kirinbytecode.asm.builder.InterceptorImplementation;
import ru.mike.kirinbytecode.asm.definition.AfterMethodDefinition;
import ru.mike.kirinbytecode.asm.definition.BeforeMethodDefinition;
import ru.mike.kirinbytecode.asm.definition.MethodDefinition;
import ru.mike.kirinbytecode.asm.definition.StageMethodDefinition;
import ru.mike.kirinbytecode.asm.definition.proxy.ProxyClassDefinition;
import ru.mike.kirinbytecode.asm.exception.InterceptorImplementationNotFoundException;
import ru.mike.kirinbytecode.asm.exception.ReturnTypeCastException;
import ru.mike.kirinbytecode.asm.generator.Generator;
import ru.mike.kirinbytecode.asm.matcher.FixedValue;

import java.lang.reflect.Method;
import java.util.Objects;

import static org.objectweb.asm.Opcodes.ACC_PUBLIC;
import static org.objectweb.asm.Opcodes.ALOAD;
import static org.objectweb.asm.Opcodes.ARETURN;
import static org.objectweb.asm.Opcodes.ASM9;
import static org.objectweb.asm.Opcodes.GETFIELD;
import static org.objectweb.asm.Opcodes.INVOKEINTERFACE;
import static ru.mike.kirinbytecode.asm.definition.BeforeMethodDefinition.RUNNABLE_BEFORE_FIELD_DESCRIPTOR;

@Getter
public class DefaultMethodGenerator<T> implements Generator {
    private ProxyClassDefinition<T> definition;
    private MethodNode mn;
    private MethodDefinition<T> methodDefinition;

    public DefaultMethodGenerator(ProxyClassDefinition<T> definition) {
        this.definition = definition;
    }

    public void setMethodDefinition(MethodDefinition<T> methodDefinition) {
        this.methodDefinition = methodDefinition;
    }

    @Override
    public void generateNode() {
        InterceptorImplementation implementation = methodDefinition.getImplementation();

        //TODO: реализовать chain of responsibility
        if (!(implementation instanceof FixedValue)) {
            throw new InterceptorImplementationNotFoundException("Unable to interceptor by implementation:"
                    + implementation + ". Type not supported.");
        }

        FixedValue fixedValueImp = (FixedValue) implementation;

        String interceptedReturnValue = fixedValueImp.getValue();
        Method interceptedMethod = methodDefinition.getMethod();

        Class<?> originalReturnType = interceptedMethod.getReturnType();
        Class<? extends String> interceptedReturnType = interceptedReturnValue.getClass();

        if (!originalReturnType.equals(interceptedReturnType)) {
            throw new ReturnTypeCastException("Unable to intercept method:" + interceptedMethod +
                    ". Return types are not equals. Expected:'" + originalReturnType + "', but got " + interceptedReturnType);
        }

        String methodDescriptor = methodDefinition.getMethodDescriptor();
        String methodName = interceptedMethod.getName();
//      TODO: реализовать modifiers
        int modifiers = interceptedMethod.getModifiers();

        mn = new MethodNode(ASM9, ACC_PUBLIC, methodName, methodDescriptor, null, null);

        generateBeforeMethodDefinitionCall();
        generateAfterMethodDefinitionCall();

        mn.visitLdcInsn(interceptedReturnValue);
        mn.visitInsn(ARETURN);

    }

    private void generateBeforeMethodDefinitionCall() {
        BeforeMethodDefinition beforeMethodDefinition = methodDefinition.getBeforeMethodDefinition();
        generateStageMethodDefinitionCall(beforeMethodDefinition);
    }

    private void generateAfterMethodDefinitionCall() {
        AfterMethodDefinition afterMethodDefinition = methodDefinition.getAfterMethodDefinition();
        generateStageMethodDefinitionCall(afterMethodDefinition);
    }

    private void generateStageMethodDefinitionCall(StageMethodDefinition stageMethodDefinition) {
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
                RUNNABLE_BEFORE_FIELD_DESCRIPTOR
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
