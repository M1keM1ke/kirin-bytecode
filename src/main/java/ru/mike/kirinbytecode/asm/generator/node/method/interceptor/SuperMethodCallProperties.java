package ru.mike.kirinbytecode.asm.generator.node.method.interceptor;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class SuperMethodCallProperties {
    private Integer lastLOADOpcodeNumber;
}
