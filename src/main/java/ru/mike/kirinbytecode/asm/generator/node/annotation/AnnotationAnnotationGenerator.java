package ru.mike.kirinbytecode.asm.generator.node.annotation;

import lombok.SneakyThrows;
import org.objectweb.asm.AnnotationVisitor;

import javax.annotation.Nullable;

public class AnnotationAnnotationGenerator extends AbstractAnnotationGenerator {

    @Override
    @SneakyThrows
    public void visit(AnnotationVisitor annotationVisitor, Object value, String descriptor, @Nullable String name) {
        defaultAnnotationGenerator.visitAnnotation(annotationVisitor, value, descriptor, name);
    }

//    @Override
//    @SneakyThrows
//    public void visit(AnnotationVisitor annotationVisitor, Object value, String descriptor, @Nullable String name) {
//        AnnotationVisitor annoAnnoVisitor = annotationVisitor.visitAnnotation(name, descriptor);
//
//        if (!(value instanceof Annotation)) {
//            return;
//        }
//
//        List<Method> annotationMethods = getAnnotationMethods(value);
//
//        for (Method annotationMethod : annotationMethods) {
//            Class<?> returnType = annotationMethod.getReturnType();
//            Object returnValue = annotationMethod.invoke(value);
//
//            if (returnType.isAnnotation()) {
//                defaultAnnotationGenerator.visitAnnotation(annoAnnoVisitor, returnValue, Type.getDescriptor(returnType), annotationMethod.getName());
//            } else if (returnType.isArray()) {
//                defaultAnnotationGenerator.visitArray(annoAnnoVisitor, returnValue, annotationMethod.getName());
//            } else if (returnType.isEnum()) {
//                defaultAnnotationGenerator.visitEnum(annoAnnoVisitor, returnValue, annotationMethod.getName(), Type.getDescriptor(returnType));
//            } else if (Class.class.isAssignableFrom(returnType)) {
//                defaultAnnotationGenerator.visitClass(annoAnnoVisitor, annotationMethod.getName(), Type.getType((Class<?>) returnValue));
//            } else {
//                defaultAnnotationGenerator.visitOther(annoAnnoVisitor, annotationMethod.getName(), returnValue);
//            }
//
//        }
//    }
}
