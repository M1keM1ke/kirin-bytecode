package ru.mike.kirinbytecode.asm.util;

import lombok.SneakyThrows;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;
import org.objectweb.asm.tree.MethodNode;

import javax.annotation.Nullable;
import java.util.Objects;

import static jdk.dynalink.linker.support.TypeUtilities.isWrapperType;
import static org.objectweb.asm.Opcodes.*;

public class AsmUtil {

    public static String getMethodDescriptor(Class<?> returnType) {
        return getMethodDescriptor(returnType, null);
    }

    public static String getMethodDescriptor(Class<?> returnType, @Nullable Class<?>...parameters) {
        StringBuilder sb = new StringBuilder();
        sb.append("(");

        if (Objects.nonNull(parameters)) {
            for (int i = 0; i < parameters.length; i++) {
                sb.append(Type.getDescriptor(parameters[i]));
            }
        }

        sb.append(")").append(Type.getDescriptor(returnType));

        return sb.toString();
    }

    public static int RETURNbyClass(Class<?> clazz) {
        return opcodeByClass(clazz, IRETURN, FRETURN, DRETURN, LRETURN, ARETURN);
    }

    public static int LOADbyClass(Class<?> clazz) {
        return opcodeByClass(clazz, ILOAD, FLOAD, DLOAD, LLOAD, ALOAD);
    }

    public static int STOREbyClass(Class<?> clazz) {
        return opcodeByClass(clazz, ISTORE, FSTORE, DSTORE, LSTORE, ASTORE);
    }

    public static void invokeVirtualWrapperValue(MethodNode mn, Class<?> wrapperClazz) {
        if (!isWrapperType(wrapperClazz)) {
            throw new RuntimeException(wrapperClazz + " is not wrapper type!");
        }

        String methodName = getWrapperValueMethodName(wrapperClazz);
        mn.visitMethodInsn(
                Opcodes.INVOKEVIRTUAL,
                wrapperClazz.getName().replaceAll("\\.", "/"),
                methodName,
                getMethodDescriptor(wrapperClazz, methodName, null),
                false
        );
    }

    @SneakyThrows
    public static String getMethodDescriptor(Class<?> clazz, String methodName, Class<?>...parameterTypes) {
        return Type.getMethodDescriptor(clazz.getDeclaredMethod(methodName, parameterTypes));
    }

    public static String getWrapperValueMethodName(Class<?> wrapperClass) {
        if (Integer.class.isAssignableFrom(wrapperClass)) {
            return "intValue";
        }

        if (Short.class.isAssignableFrom(wrapperClass)) {
            return "shortValue";
        }

        if (Byte.class.isAssignableFrom(wrapperClass)) {
            return "byteValue";
        }

        if (Character.class.isAssignableFrom(wrapperClass)) {
            return "charValue";
        }

        if (Long.class.isAssignableFrom(wrapperClass)) {
            return "longValue";
        }

        if (Float.class.isAssignableFrom(wrapperClass)) {
            return "floatValue";
        }

        if (Double.class.isAssignableFrom(wrapperClass)) {
            return "doubleValue";
        }

        throw new RuntimeException("Unable to find wrapper class: " + wrapperClass);
    }

    private static int opcodeByClass(Class<?> clazz, int iOpcode, int fOpcode, int dOpcode, int lOpcode, int aOpcode) {
        if (Objects.equals(int.class, clazz) ||
                Objects.equals(short.class, clazz) ||
                Objects.equals(boolean.class, clazz) ||
                Objects.equals(char.class, clazz) ||
                Objects.equals(byte.class, clazz)
        ) {
            return iOpcode;
        }

        if (Objects.equals(float.class, clazz)) {
            return fOpcode;
        }

        if (Objects.equals(double.class, clazz)) {
            return dOpcode;
        }

        if (Objects.equals(long.class, clazz)) {
            return lOpcode;
        }

        return aOpcode;
    }
}
