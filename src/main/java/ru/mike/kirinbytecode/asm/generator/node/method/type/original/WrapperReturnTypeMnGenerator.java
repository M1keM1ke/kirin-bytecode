package ru.mike.kirinbytecode.asm.generator.node.method.type.original;

import lombok.extern.log4j.Log4j2;
import org.objectweb.asm.tree.MethodNode;
import ru.mike.kirinbytecode.asm.builder.InterceptorImplementation;
import ru.mike.kirinbytecode.asm.definition.MethodDefinition;
import ru.mike.kirinbytecode.asm.definition.proxy.ProxyClassDefinition;
import ru.mike.kirinbytecode.asm.matcher.FixedValue;

import static jdk.dynalink.linker.support.TypeUtilities.isWrapperType;
import static org.objectweb.asm.Opcodes.ARETURN;
import static org.objectweb.asm.Opcodes.INVOKESTATIC;
import static ru.mike.kirinbytecode.asm.util.AsmUtil.getMethodDescriptor;
import static ru.mike.kirinbytecode.asm.util.Constants.Methods.VALUE_OF_NAME;
import static sun.invoke.util.Wrapper.asPrimitiveType;

@Log4j2
public class WrapperReturnTypeMnGenerator extends AbstractReturnTypeMnGenerator {

    public WrapperReturnTypeMnGenerator(AbstractReturnTypeMnGenerator next) {
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
        Class<?> originalReturnType = methodDefinition.getReturnType();
        InterceptorImplementation implementation = methodDefinition.getImplementation();

        if (!(implementation instanceof FixedValue)) {
            log.warn("Not suitable interceptor implementation, expected {}", FixedValue.class.getSimpleName());
            return mn;
        }

        Object interceptedReturnValue = ((FixedValue) implementation).getValue();

        if (isWrapperType(originalReturnType)) {
            mn.visitLdcInsn(interceptedReturnValue);
            String valueOfMethodDesc = getMethodDescriptor(
                    originalReturnType, VALUE_OF_NAME, asPrimitiveType(originalReturnType)
            );

            if (valueOfMethodDesc.isEmpty()) {
                throw new RuntimeException("Unable to get method descriptor of 'valueOf' method in class:" + originalReturnType);
            }

            mn.visitMethodInsn(
                    INVOKESTATIC,
                    originalReturnType.getName().replaceAll("\\.", "/"),
                    VALUE_OF_NAME,
                    valueOfMethodDesc,
                    false
            );

            mn.visitInsn(ARETURN);
            return mn;
        }

        return this.getNext().generate(mn, definition, methodDefinition);
    }
}
