package ru.mike.kirinbytecode.asm.generator.node.method.type.original;

import lombok.extern.slf4j.Slf4j;
import org.objectweb.asm.tree.MethodNode;
import ru.mike.kirinbytecode.asm.builder.InterceptorImplementation;
import ru.mike.kirinbytecode.asm.definition.MethodDefinition;
import ru.mike.kirinbytecode.asm.definition.proxy.ProxyClassDefinition;
import ru.mike.kirinbytecode.asm.matcher.FixedValue;

import java.util.Optional;

import static jdk.dynalink.linker.support.TypeUtilities.isWrapperType;
import static org.objectweb.asm.Opcodes.ARETURN;
import static org.objectweb.asm.Opcodes.INVOKESTATIC;
import static ru.mike.kirinbytecode.asm.util.AsmUtil.getMethodDescriptor;
import static sun.invoke.util.Wrapper.asPrimitiveType;

@Slf4j
public class WrapperReturnTypeMnGenerator extends OriginalReturnTypeMnGenerator {

    public WrapperReturnTypeMnGenerator(OriginalReturnTypeMnGenerator next) {
        super(next);
    }

    /**
     * Генерирует возвращаемое значение прокси метода, если это обертка над примитивом.
     * Генерирует конструкцию вида:
     * return <wrapper_type>.valueOf(<value>);
     * где <wrapper_type> - это тип обертки, а <value> - это возвращаемое значение
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

        if (isWrapperType(originalReturnType)) {
            mn.visitLdcInsn(interceptedReturnValue);
            Optional<String> valueOfMethodDescOpt = getMethodDescriptor(originalReturnType, "valueOf", new Class[] {asPrimitiveType(originalReturnType)});

            if (valueOfMethodDescOpt.isEmpty()) {
                throw new RuntimeException("Unable to get method descriptor of 'valueOf' method in class:" + originalReturnType);
            }

            mn.visitMethodInsn(INVOKESTATIC, originalReturnType.getName().replaceAll("\\.", "/"), "valueOf", valueOfMethodDescOpt.get(), false);

            mn.visitInsn(ARETURN);
            return mn;
        }

        return this.getNext().generate(mn, definition, methodDefinition);
    }
}
