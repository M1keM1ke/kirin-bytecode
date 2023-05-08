package ru.mike.kirinbytecode.asm.definition;

import lombok.AllArgsConstructor;
import ru.mike.kirinbytecode.asm.generator.NameGenerator;

@AllArgsConstructor
public class BeforeMethodDefinition extends StageMethodDefinition {
    private Runnable runnableBefore;
    private NameGenerator runnableBeforeFieldNameGenerator;

    @Override
    public Runnable getRunnableStage() {
        return runnableBefore;
    }


    @Override
    public NameGenerator getFieldNameGenerator() {
        return runnableBeforeFieldNameGenerator;
    }
}
