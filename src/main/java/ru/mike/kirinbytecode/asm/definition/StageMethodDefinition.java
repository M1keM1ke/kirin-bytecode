package ru.mike.kirinbytecode.asm.definition;

import ru.mike.kirinbytecode.asm.generator.NameGenerator;

public abstract class StageMethodDefinition implements Definition {
    public static final String RUNNABLE_FIELD_DESCRIPTOR = "Ljava/lang/Runnable;";

    public abstract Runnable getRunnableStage();

    public abstract NameGenerator getFieldNameGenerator();
}
