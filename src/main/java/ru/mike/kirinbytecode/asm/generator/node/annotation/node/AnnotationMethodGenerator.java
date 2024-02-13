package ru.mike.kirinbytecode.asm.generator.node.annotation.node;

import org.objectweb.asm.AnnotationVisitor;
import org.objectweb.asm.Type;
import org.objectweb.asm.tree.MethodNode;
import ru.mike.kirinbytecode.asm.builder.method.AnnotationDefinition;
import ru.mike.kirinbytecode.asm.builder.method.AnnotationValue;

import java.util.Map;
import java.util.Objects;

public class AnnotationMethodGenerator extends AbstractAnnotationNodeGenerator {


    @Override
    public void visitNodeAnnotation(AnnotationNodeContext annotationNodeContext, AnnotationDefinition annotationDefinition) {
        AnnotationNodeContext.AnnotationMethodNodeContext annotationMethodNodeContext = annotationNodeContext.getAnnotationMethodNodeContext();
        MethodNode mn = annotationMethodNodeContext.getMethodNode();

        if (Objects.isNull(mn)) {
            throw new RuntimeException("MethodNode is null, unable to generate " +
                    "MethodNode annotations. Context:" + annotationNodeContext);
        }

        AnnotationVisitor annotationVisitor = mn.visitAnnotation(Type.getDescriptor(annotationDefinition.getType()), true);
        Map<String, AnnotationValue<?>> annotationValues = annotationDefinition.getValues();

//      для аннотации проставляем ее параметры
        for (Map.Entry<String, AnnotationValue<?>> property : annotationValues.entrySet()) {
            annotationTypeGeneratorChooser.choose(annotationVisitor, property.getKey(), property.getValue());
        }
    }
}
