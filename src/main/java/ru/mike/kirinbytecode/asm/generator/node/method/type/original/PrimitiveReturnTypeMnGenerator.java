package ru.mike.kirinbytecode.asm.generator.node.method.type.original;

import lombok.extern.slf4j.Slf4j;
import org.objectweb.asm.tree.MethodNode;
import ru.mike.kirinbytecode.asm.builder.InterceptorImplementation;
import ru.mike.kirinbytecode.asm.definition.MethodDefinition;
import ru.mike.kirinbytecode.asm.definition.proxy.ProxyClassDefinition;
import ru.mike.kirinbytecode.asm.matcher.FixedValue;

import java.util.Objects;

import static org.objectweb.asm.Opcodes.DRETURN;
import static org.objectweb.asm.Opcodes.IRETURN;
import static org.objectweb.asm.Opcodes.LRETURN;

@Slf4j
public class PrimitiveReturnTypeMnGenerator extends OriginalReturnTypeMnGenerator {

    public PrimitiveReturnTypeMnGenerator(OriginalReturnTypeMnGenerator next) {
        super(next);
    }

    /**
     * Генерирует возвращаемое значение прокси метода, если это примитивный тип.
     * Генерирует конструкцию вида:
     * return <return_operand>, где <return_operand> - это:
     * IRETURN, DRETURN или LRETURN
     *
     * @param mn {@link MethodNode}, которая генерируется
     * @param definition описание прокси-класса со всеми конфигурациями
     * @param methodDefinition описание прокси метода
     * @return сгенерированная {@link MethodNode}, либо неизмененная
     */
    @Override
    public <T> MethodNode generate(MethodNode mn, ProxyClassDefinition<T> definition, MethodDefinition<T> methodDefinition) {
        Class<?> originalReturnType = methodDefinition.getMethod().getReturnType();
        InterceptorImplementation implementation = methodDefinition.getImplementation();

        if (!(implementation instanceof FixedValue)) {
            log.warn("Not suitable interceptor implementation, expected {}", FixedValue.class.getSimpleName());
            return mn;
        }

        Object interceptedReturnValue = ((FixedValue) implementation).getValue();

        if (originalReturnType.isPrimitive()) {
            mn.visitLdcInsn(interceptedReturnValue);

            if (Objects.equals(int.class, originalReturnType) ||
                    Objects.equals(short.class, originalReturnType) ||
                    Objects.equals(boolean.class, originalReturnType) ||
                    Objects.equals(char.class, originalReturnType)
            ) {
                mn.visitInsn(IRETURN);
            }

            if (Objects.equals(double.class, originalReturnType)) {
                mn.visitInsn(DRETURN);
            }

            if (Objects.equals(long.class, originalReturnType)) {
                mn.visitInsn(LRETURN);
            }

            return mn;
        }

        return this.getNext().generate(mn, definition, methodDefinition);
    }
}
