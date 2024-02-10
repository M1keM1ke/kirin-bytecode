package ru.mike.kirinbytecode.asm.definition.parameter;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import ru.mike.kirinbytecode.asm.builder.method.AnnotationDefinition;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@ToString
public class ParameterDefinition {
    private String name;
    private Class<?> type;
    private Integer index;

    private List<AnnotationDefinition> annotationDefinitions;

    @Builder
    public ParameterDefinition(String name, Class<?> type, Integer index) {
        this.name = name;
        this.type = type;
        this.index = index;
        this.annotationDefinitions = new ArrayList<>();
    }
}
