package ru.mike.kirinbytecode.asm.util;

import org.objectweb.asm.Type;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Objects;
import java.util.Optional;

import static org.objectweb.asm.Opcodes.ALOAD;
import static org.objectweb.asm.Opcodes.ARETURN;
import static org.objectweb.asm.Opcodes.ASTORE;
import static org.objectweb.asm.Opcodes.DLOAD;
import static org.objectweb.asm.Opcodes.DRETURN;
import static org.objectweb.asm.Opcodes.DSTORE;
import static org.objectweb.asm.Opcodes.ILOAD;
import static org.objectweb.asm.Opcodes.IRETURN;
import static org.objectweb.asm.Opcodes.ISTORE;
import static org.objectweb.asm.Opcodes.LLOAD;
import static org.objectweb.asm.Opcodes.LRETURN;
import static org.objectweb.asm.Opcodes.LSTORE;

public class AsmUtil {

    public static Optional<String> getMethodDescriptor(Class<?> clazz, String methodName, Class<?>[] parametersTypes) {
        for (Method method : clazz.getDeclaredMethods()) {
            if (Objects.equals(methodName, method.getName()) && Arrays.deepEquals(parametersTypes, method.getParameterTypes())) {
                return Optional.of(Type.getMethodDescriptor(method));
            }
        }

        return Optional.empty();
    }

    public static int RETURNbyClass(Class<?> clazz) {
        return opcodeByClass(clazz, IRETURN, DRETURN, LRETURN, ARETURN);
    }

    public static int LOADbyClass(Class<?> clazz) {
        return opcodeByClass(clazz, ILOAD, DLOAD, LLOAD, ALOAD);
    }

    public static int STOREbyClass(Class<?> clazz) {
        return opcodeByClass(clazz, ISTORE, DSTORE, LSTORE, ASTORE);
    }

    private static int opcodeByClass(Class<?> clazz, int iOpcode, int dOpcode, int lOpcode, int aOpcode) {
        if (Objects.equals(int.class, clazz) ||
                Objects.equals(short.class, clazz) ||
                Objects.equals(boolean.class, clazz) ||
                Objects.equals(char.class, clazz)
        ) {
            return iOpcode;
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
