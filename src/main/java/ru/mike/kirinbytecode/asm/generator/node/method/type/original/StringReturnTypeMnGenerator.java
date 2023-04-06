package ru.mike.kirinbytecode.asm.generator.node.method.type.original;

import lombok.extern.log4j.Log4j2;
import org.objectweb.asm.tree.MethodNode;
import ru.mike.kirinbytecode.asm.builder.InterceptorImplementation;
import ru.mike.kirinbytecode.asm.definition.MethodDefinition;
import ru.mike.kirinbytecode.asm.definition.proxy.ProxyClassDefinition;
import ru.mike.kirinbytecode.asm.matcher.FixedValue;

import java.util.Objects;

import static org.objectweb.asm.Opcodes.ARETURN;

@Log4j2
public class StringReturnTypeMnGenerator extends OriginalReturnTypeMnGenerator {

    public StringReturnTypeMnGenerator(OriginalReturnTypeMnGenerator next) {
        super(next);
    }

    /**
     * Генерирует возвращаемое значение прокси метода, если это строка.
     * Генерирует конструкцию вида:
     * return <str>;
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

        if (Objects.equals(String.class, originalReturnType)) {
            mn.visitLdcInsn(interceptedReturnValue);

            mn.visitInsn(ARETURN);
            return mn;
        }

        return this.getNext().generate(mn,definition ,methodDefinition);
    }
}
