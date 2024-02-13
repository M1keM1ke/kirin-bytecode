package ru.mike.kirinbytecode.asm.generator.node;

import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.InsnNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;
import org.objectweb.asm.tree.VarInsnNode;
import ru.mike.kirinbytecode.asm.definition.proxy.ProxyClassDefinition;
import ru.mike.kirinbytecode.asm.generator.Generator;
import ru.mike.kirinbytecode.asm.util.TransformerUtil;

public class DefaultClassGenerator<T> implements Generator, Opcodes {
    private ProxyClassDefinition<T> definition;
    private ClassNode cn;

    public DefaultClassGenerator(ProxyClassDefinition<T> definition) {
        this.definition = definition;
    }

    public ClassNode getCn() {
        return cn;
    }

    @Override
    public void generateNode() {
        Class<T> clazz = definition.getOriginalClazz();
        String className = definition.getNameGenerator().getGeneratedName(clazz.getSimpleName());
        definition.setGeneratedProxyClassName(className);

        fillClassNode(clazz, className);
    }

    private void fillClassNode(Class<T> clazz, String className) {
        cn = new ClassNode(ASM9);
        cn.version = V1_8;
        cn.access = ACC_PUBLIC;
        cn.name = definition.getProxyPackageName().replaceAll("\\.", "/") + "/" + className;
        cn.superName = TransformerUtil.transformClassNameToAsmClassName(clazz);

        MethodNode defaultConstructor = createDefaultConstructor(clazz);

        cn.methods.add(defaultConstructor);
    }

    private MethodNode createDefaultConstructor(Class<T> clazz) {
        MethodNode defaultConstructor = new MethodNode(ASM9, ACC_PUBLIC, "<init>", "()V", null, null);
        defaultConstructor.instructions.add(new VarInsnNode(ALOAD, 0));
        defaultConstructor.instructions.add(new MethodInsnNode(INVOKESPECIAL, TransformerUtil.transformClassNameToAsmClassName(clazz), "<init>", "()V", false));

        defaultConstructor.instructions.add(new InsnNode(RETURN));
        return defaultConstructor;
    }
}
