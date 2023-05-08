package ru.mike.kirinbytecode.asm.definition;

import lombok.AllArgsConstructor;
import ru.mike.kirinbytecode.asm.generator.NameGenerator;

@AllArgsConstructor
public class AfterMethodDefinition extends StageMethodDefinition {

    private Runnable runnableAfter;
    private NameGenerator runnableAfterFieldNameGenerator;

    @Override
    public Runnable getRunnableStage() {
        return runnableAfter;
    }

    @Override
    public NameGenerator getFieldNameGenerator() {
        return runnableAfterFieldNameGenerator;
    }
}
