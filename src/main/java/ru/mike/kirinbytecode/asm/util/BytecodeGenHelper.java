package ru.mike.kirinbytecode.asm.util;

import org.objectweb.asm.tree.MethodNode;
import ru.mike.kirinbytecode.asm.definition.proxy.ProxyClassDefinition;

import static org.objectweb.asm.Opcodes.ALOAD;
import static org.objectweb.asm.Opcodes.ASTORE;
import static org.objectweb.asm.Opcodes.CHECKCAST;
import static org.objectweb.asm.Opcodes.GETFIELD;
import static org.objectweb.asm.Opcodes.INVOKEINTERFACE;

public class BytecodeGenHelper {

    public static void generateSupplierCall(ProxyClassDefinition definition, MethodNode mn, String fieldGeneratedName) {
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
    }
}
