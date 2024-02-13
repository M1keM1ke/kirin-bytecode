package ru.mike.kirinbytecode.asm.generator.node.annotation.node;

import org.objectweb.asm.AnnotationVisitor;
import org.objectweb.asm.Type;
import org.objectweb.asm.tree.ClassNode;
import ru.mike.kirinbytecode.asm.builder.method.AnnotationDefinition;
import ru.mike.kirinbytecode.asm.builder.method.AnnotationValue;

import java.util.Map;
import java.util.Objects;

public class AnnotationClassGenerator extends AbstractAnnotationNodeGenerator {

    @Override
    public void visitNodeAnnotation(AnnotationNodeContext annotationNodeContext, AnnotationDefinition annotationDefinition) {
        AnnotationNodeContext.AnnotationClassNodeContext annotationClassNodeContext = annotationNodeContext.getAnnotationClassNodeContext();
        ClassNode cn = annotationClassNodeContext.getClassNode();

        if (Objects.isNull(cn)) {
            throw new RuntimeException("ClassNode is null, unable to generate " +
                    "ClassNode annotations. Context:" + annotationNodeContext);
        }

        AnnotationVisitor annotationVisitor = cn.visitAnnotation(Type.getDescriptor(annotationDefinition.getType()), true);
        Map<String, AnnotationValue<?>> annotationValues = annotationDefinition.getValues();

//      для аннотации проставляем ее параметры
        for (Map.Entry<String, AnnotationValue<?>> property : annotationValues.entrySet()) {
            annotationTypeGeneratorChooser.choose(annotationVisitor, property.getKey(), property.getValue());
        }
    }
}
