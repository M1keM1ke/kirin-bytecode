package ru.mike.kirinbytecode.asm.definition;

import lombok.AllArgsConstructor;
import ru.mike.kirinbytecode.asm.generator.NameGenerator;

@AllArgsConstructor
public class AfterMethodDefinition extends StageMethodDefinition {
    public static final String RUNNABLE_AFTER_FIELD_DESCRIPTOR = "Ljava/lang/Runnable;";

    private Runnable runnableAfter;
    private NameGenerator runnableAfterFieldNameGenerator;

    @Override
    public void setRunnableStage(Runnable runnable) {
        this.runnableAfter = runnable;
    }

    @Override
    public Runnable getRunnableStage() {
        return runnableAfter;
    }

    @Override
    public void setFieldNameGenerator(NameGenerator nameGenerator) {
        this.runnableAfterFieldNameGenerator = nameGenerator;
    }

    @Override
    public NameGenerator getFieldNameGenerator() {
        return runnableAfterFieldNameGenerator;
    }
}
