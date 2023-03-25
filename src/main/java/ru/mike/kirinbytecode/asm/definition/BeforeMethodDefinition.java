package ru.mike.kirinbytecode.asm.definition;

import lombok.AllArgsConstructor;
import ru.mike.kirinbytecode.asm.generator.NameGenerator;

@AllArgsConstructor
public class BeforeMethodDefinition extends StageMethodDefinition {
    public static final String RUNNABLE_BEFORE_FIELD_DESCRIPTOR = "Ljava/lang/Runnable;";

    private Runnable runnableBefore;
    private NameGenerator runnableBeforeFieldNameGenerator;

    @Override
    public void setRunnableStage(Runnable runnable) {
        this.runnableBefore = runnable;
    }

    @Override
    public Runnable getRunnableStage() {
        return runnableBefore;
    }

    @Override
    public void setFieldNameGenerator(NameGenerator nameGenerator) {
        this.runnableBeforeFieldNameGenerator = nameGenerator;
    }

    @Override
    public NameGenerator getFieldNameGenerator() {
        return runnableBeforeFieldNameGenerator;
    }
}
