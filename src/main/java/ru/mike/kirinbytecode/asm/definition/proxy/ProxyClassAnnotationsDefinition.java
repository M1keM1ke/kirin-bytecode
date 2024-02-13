package ru.mike.kirinbytecode.asm.definition.proxy;

import lombok.Getter;
import ru.mike.kirinbytecode.asm.builder.method.AnnotationDefinition;
import ru.mike.kirinbytecode.asm.definition.Definition;
import ru.mike.kirinbytecode.asm.generator.node.annotation.node.AbstractAnnotationNodeGenerator;
import ru.mike.kirinbytecode.asm.generator.node.annotation.node.AnnotationClassGenerator;

import java.util.HashMap;
import java.util.Map;

@Getter
public class ProxyClassAnnotationsDefinition<T> implements Definition {
    private ProxyClassDefinition<T> proxyClassDefinition;
    private Map<String, AnnotationDefinition> annotationDefinitions;
    private AbstractAnnotationNodeGenerator annotationNodeGenerator;

    public ProxyClassAnnotationsDefinition(ProxyClassDefinition<T> proxyClassDefinition) {
        this.proxyClassDefinition = proxyClassDefinition;
        this.annotationDefinitions = new HashMap<>();
        this.annotationNodeGenerator = new AnnotationClassGenerator();
    }

    public void addAnnotation(AnnotationDefinition annotationDefinition) {
        annotationDefinitions.put(annotationDefinition.getType().getName(), annotationDefinition);
    }
}
