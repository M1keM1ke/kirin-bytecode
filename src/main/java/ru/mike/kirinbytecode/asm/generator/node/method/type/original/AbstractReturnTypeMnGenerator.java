package ru.mike.kirinbytecode.asm.generator.node.method.type.original;

import org.objectweb.asm.tree.MethodNode;
import ru.mike.kirinbytecode.asm.definition.MethodDefinition;
import ru.mike.kirinbytecode.asm.definition.proxy.ProxyClassDefinition;


public abstract class AbstractReturnTypeMnGenerator {
    private AbstractReturnTypeMnGenerator next;

    protected AbstractReturnTypeMnGenerator(AbstractReturnTypeMnGenerator next) {
        this.next = next;
    }

    /**
     * Генерирует возвращаемое значение прокси метода в зависимости от его типа
     *
     * @param mn {@link MethodNode}, которая генерируется
     * @param definition описание прокси-класса со всеми конфигурациями
     * @param methodDefinition описание прокси метода
     * @return сгенерированная {@link MethodNode}, либо неизмененная
     */
    public abstract <T> MethodNode generate(MethodNode mn, ProxyClassDefinition<T> definition, MethodDefinition<T> methodDefinition);

    public AbstractReturnTypeMnGenerator getNext() { return this.next; }
}
