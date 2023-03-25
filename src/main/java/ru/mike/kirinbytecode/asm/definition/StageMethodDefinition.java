package ru.mike.kirinbytecode.asm.definition;

import ru.mike.kirinbytecode.asm.generator.NameGenerator;

public abstract class StageMethodDefinition implements Definition {

    public abstract void setRunnableStage(Runnable runnable);

    public abstract Runnable getRunnableStage();

    public abstract void setFieldNameGenerator(NameGenerator nameGenerator);

    public abstract NameGenerator getFieldNameGenerator();
}
