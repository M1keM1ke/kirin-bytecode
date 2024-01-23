package ru.mike.kirinbytecode.asm.generator.node.method.interceptor.customvalue;

import com.google.auto.service.AutoService;
import org.objectweb.asm.tree.MethodNode;
import ru.mike.kirinbytecode.asm.builder.InterceptorImplementation;
import ru.mike.kirinbytecode.asm.definition.MethodDefinition;
import ru.mike.kirinbytecode.asm.definition.proxy.ProxyClassDefinition;
import ru.mike.kirinbytecode.asm.generator.name.FieldNameGenerator;
import ru.mike.kirinbytecode.asm.generator.node.method.interceptor.InterceptorNodeGeneratorHandler;
import ru.mike.kirinbytecode.asm.matcher.CustomValue;

import java.lang.reflect.Method;
import java.util.function.Supplier;

import static jdk.dynalink.linker.support.TypeUtilities.isWrapperType;
import static org.objectweb.asm.Opcodes.ACC_PUBLIC;
import static org.objectweb.asm.Opcodes.ALOAD;
import static org.objectweb.asm.Opcodes.ARETURN;
import static org.objectweb.asm.Opcodes.ASM9;
import static org.objectweb.asm.Opcodes.ASTORE;
import static org.objectweb.asm.Opcodes.CHECKCAST;
import static org.objectweb.asm.Opcodes.GETFIELD;
import static org.objectweb.asm.Opcodes.INVOKEINTERFACE;
import static ru.mike.kirinbytecode.asm.exception.notfound.InterceptorImplementationNotFoundException.checkImplementationTypeOrThrow;
import static sun.invoke.util.Wrapper.asPrimitiveType;

@AutoService(InterceptorNodeGeneratorHandler.class)
public class CustomValueInterceptorNodeGeneratorHandler<T> implements InterceptorNodeGeneratorHandler<T> {
    @Override
    public boolean isSuitableHandler(InterceptorImplementation implementation) {
        return implementation instanceof CustomValue;
    }

    @Override
    public MethodNode generateMethodNode(ProxyClassDefinition<T> definition, MethodDefinition<T> methodDefinition) {
        InterceptorImplementation implementation = methodDefinition.getImplementation();

        checkImplementationTypeOrThrow(CustomValue.class, implementation);



        Method interceptedMethod = methodDefinition.getMethod();
        String interceptedMethodName = interceptedMethod.getName();
        int modifiers = interceptedMethod.getModifiers();

        CustomValue<T> customValue = (CustomValue) implementation;
        Supplier<T> supp = customValue.getSupp();

        Class<?> suppGenericType = customValue.getReturnType();

        Class<?> originalReturnType = interceptedMethod.getReturnType();

//      если возвращаемый тип в оригинальном методе класса - примитивный тип, а тип значения для
//      прокси метода - обертка, то необходимо скастить обертку к примитиву, иначе будет ReturnTypeCastException
        if (originalReturnType.isPrimitive() && isWrapperType(suppGenericType)) {
            suppGenericType = asPrimitiveType(suppGenericType);
        }

        if (!originalReturnType.isAssignableFrom(suppGenericType)) {
            throw new RuntimeException();
        }

        checkModifiersCorrectOrThrow(definition, interceptedMethodName, modifiers);


        String methodDescriptor = methodDefinition.getMethodDescriptor();

        MethodNode mn = new MethodNode(ASM9, modifiers, interceptedMethodName, methodDescriptor, null, null);

        generateBeforeMethodDefinitionCall(definition, methodDefinition.getBeforeMethodDefinition(), mn);

        String fieldGeneratedName = new FieldNameGenerator().getGeneratedName(null);

        mn.visitVarInsn(ALOAD, 0);
        mn.visitFieldInsn(
                GETFIELD,
                definition.getProxyPackageName().replaceAll("\\.", "/") + "/" + definition.getNameGenerator().getGeneratedName(definition.getOriginalClazz().getSimpleName()),
                fieldGeneratedName,
                "Ljava/util/function/Supplier;"
        );
        mn.visitMethodInsn(INVOKEINTERFACE,
                "java/util/function/Supplier",
                "get",
                "()Ljava/lang/Object;",
                true
        );
        mn.visitTypeInsn(CHECKCAST, "java/lang/String");
        mn.visitVarInsn(ASTORE, 3);


//      создаем и добавляем FieldDefinition в мапу, чтобы в LoadedType.fillEmptyFields()
//      заполнить сгенерированное поле
        definition.getProxyClassFieldsDefinition().createFieldDefinition(
                fieldGeneratedName,
                supp,
                Supplier.class,
                ACC_PUBLIC
        );

        generateAfterMethodDefinitionCall(definition, methodDefinition.getAfterMethodDefinition(), mn);

        mn.visitVarInsn(ALOAD, 3);
        mn.visitInsn(ARETURN);

        return mn;
    }

    @Override
    public void checkModifiersCorrectOrThrow(ProxyClassDefinition<T> definition, String interceptedMethodName, int modifiers) {

    }
}
