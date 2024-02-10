package ru.mike.kirinbytecode.asm.generator.node.method.type.original;

import lombok.extern.log4j.Log4j2;
import org.objectweb.asm.tree.MethodNode;
import ru.mike.kirinbytecode.asm.builder.InterceptorImplementation;
import ru.mike.kirinbytecode.asm.definition.MethodDefinition;
import ru.mike.kirinbytecode.asm.definition.proxy.ProxyClassDefinition;
import ru.mike.kirinbytecode.asm.matcher.FixedValue;

import static ru.mike.kirinbytecode.asm.util.AsmUtil.RETURNbyClass;

@Log4j2
public class PrimitiveReturnTypeMnGenerator extends AbstractReturnTypeMnGenerator {

    public PrimitiveReturnTypeMnGenerator(AbstractReturnTypeMnGenerator next) {
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
        Class<?> originalReturnType = methodDefinition.getReturnType();
        InterceptorImplementation implementation = methodDefinition.getImplementation();

        if (!(implementation instanceof FixedValue)) {
            log.warn("Not suitable interceptor implementation, expected {}", FixedValue.class.getSimpleName());
            return mn;
        }

        Object interceptedReturnValue = ((FixedValue) implementation).getValue();

        if (originalReturnType.isPrimitive()) {
            mn.visitLdcInsn(interceptedReturnValue);
            mn.visitInsn(RETURNbyClass(originalReturnType));

            return mn;
        }

        return this.getNext().generate(mn, definition, methodDefinition);
    }
}
