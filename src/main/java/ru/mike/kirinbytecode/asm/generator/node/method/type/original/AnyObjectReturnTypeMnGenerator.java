package ru.mike.kirinbytecode.asm.generator.node.method.type.original;

import lombok.extern.log4j.Log4j2;
import org.objectweb.asm.Type;
import org.objectweb.asm.tree.MethodNode;
import ru.mike.kirinbytecode.asm.builder.InterceptorImplementation;
import ru.mike.kirinbytecode.asm.definition.MethodDefinition;
import ru.mike.kirinbytecode.asm.definition.proxy.ProxyClassDefinition;
import ru.mike.kirinbytecode.asm.generator.name.FieldNameGenerator;
import ru.mike.kirinbytecode.asm.matcher.FixedValue;

import static org.objectweb.asm.Opcodes.ACC_PUBLIC;
import static org.objectweb.asm.Opcodes.ALOAD;
import static org.objectweb.asm.Opcodes.ARETURN;
import static org.objectweb.asm.Opcodes.GETFIELD;

@Log4j2
public class AnyObjectReturnTypeMnGenerator extends OriginalReturnTypeMnGenerator {

    public AnyObjectReturnTypeMnGenerator(OriginalReturnTypeMnGenerator next) {
        super(next);
    }

    /**
     * Генерирует возвращаемое значение прокси метода, если это любой объект,
     * кроме примитивов, оберток и строк.
     * Генерирует поле <generatedField> в прокси-классе со значением из ((FixedValue) implementation).getValue()
     * из methodDefinition.getImplementation(). После генерирует в конце метода конструкцию вида:
     * return generatedField;
     *
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
        Class<?> interceptedReturnType = interceptedReturnValue.getClass();

        if (Object.class.isAssignableFrom(originalReturnType)) {
            String fieldGeneratedName = new FieldNameGenerator().getGeneratedName(null);

            mn.visitVarInsn(ALOAD, 0);

            mn.visitFieldInsn(
                    GETFIELD,
                    definition.getProxyPackageName().replaceAll("\\.", "/") + "/" + definition.getNameGenerator().getGeneratedName(definition.getOriginalClazz().getSimpleName()),
                    fieldGeneratedName,
                    Type.getDescriptor(interceptedReturnType)
            );

            definition.getProxyClassFieldsDefinition().createFieldDefinition(
                    fieldGeneratedName,
                    interceptedReturnValue,
                    interceptedReturnType,
                    ACC_PUBLIC
            );

            mn.visitInsn(ARETURN);
            return mn;
        }

        return this.getNext().generate(mn, definition, methodDefinition);
    }
}
