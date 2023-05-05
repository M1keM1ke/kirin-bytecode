package ru.mike.kirinbytecode.asm.generator.node.method.type.original;

public class OriginalReturnTypeChainBuilder {

    /**
     * Строит цепочку обязанностей для определения возвращаемого типа проксируемого метода.
     * Порядок важен, первым проверяется, что тип примитивный, потом что обертка, потом что строка,
     * и только потом что любой объект.
     *
     * @return цепочка обязанностей типа {@link PrimitiveReturnTypeMnGenerator}, которая является первой в цепочке
     */
    public static PrimitiveReturnTypeMnGenerator buildChain() {
        var anyObjectReturnTypeMnGenerator = new AnyObjectReturnTypeMnGenerator(null);
        var stringReturnTypeMnGenerator = new StringReturnTypeMnGenerator(anyObjectReturnTypeMnGenerator);
        var wrapperReturnTypeMnGenerator = new WrapperReturnTypeMnGenerator(stringReturnTypeMnGenerator);
        var primitiveReturnTypeMnGenerator = new PrimitiveReturnTypeMnGenerator(wrapperReturnTypeMnGenerator);

        return primitiveReturnTypeMnGenerator;
    }
}
