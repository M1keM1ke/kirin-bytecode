package ru.mike.kirinbytecode.asm.generator.node.method.type.original;

public class OriginalReturnTypeChainBuilder {

    public static PrimitiveReturnTypeMnGenerator buildChain() {
        var anyObjectReturnTypeMnGenerator = new AnyObjectReturnTypeMnGenerator(null);
        var stringReturnTypeMnGenerator = new StringReturnTypeMnGenerator(anyObjectReturnTypeMnGenerator);
        var wrapperReturnTypeMnGenerator = new WrapperReturnTypeMnGenerator(stringReturnTypeMnGenerator);
        var primitiveReturnTypeMnGenerator = new PrimitiveReturnTypeMnGenerator(wrapperReturnTypeMnGenerator);

        return primitiveReturnTypeMnGenerator;
    }
}
