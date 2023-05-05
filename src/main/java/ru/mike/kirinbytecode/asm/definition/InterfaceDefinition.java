package ru.mike.kirinbytecode.asm.definition;

import lombok.Getter;
import ru.mike.kirinbytecode.asm.util.PathDelimiter;

import java.lang.reflect.Type;

@Getter
public class InterfaceDefinition<T> implements Definition {
    private Type type;
    private String fullName;

    public InterfaceDefinition(Type type) {
        this.type = type;
        this.fullName = type.getTypeName();
    }

    public String getFullName(PathDelimiter delimiter) {
        return fullName.replaceAll("\\.", delimiter.getDelimiter());
    }
}
