package ru.mike.kirinbytecode.asm.generator.node.annotation;

import lombok.SneakyThrows;
import org.objectweb.asm.AnnotationVisitor;

import javax.annotation.Nullable;

public class ArrayAnnotationGenerator extends AbstractAnnotationGenerator {

    @Override
    @SneakyThrows
    public void visit(AnnotationVisitor annotationVisitor, Object value, String descriptor, @Nullable String name) {
        defaultAnnotationGenerator.visitArray(annotationVisitor, value, name);
    }

//    @Override
//    @SneakyThrows
//    public void visit(AnnotationVisitor annotationVisitor, Object value, String descriptor, @Nullable String name) {
//        AnnotationVisitor arrayAnnoVisitor = annotationVisitor.visitArray(name);
//
//        Class<?> valueClass = value.getClass();
//
//        if (!valueClass.isArray()) {
//            return;
//        }
//
//        int arrLength = Array.getLength(value);
//        Class<?> arrayComponentType = valueClass.getComponentType();
//
//        if (arrayComponentType.isAnnotation()) {
//            for (int i = 0; i < arrLength; i++) {
//                Object arrayValue = Array.get(value, i);
//                defaultAnnotationGenerator.visitAnnotation(arrayAnnoVisitor, arrayValue, Type.getDescriptor(arrayComponentType), null);
//            }
////      по идее такого не может быть: двойной массив не может быть в качестве параметра аннотации
//        } else if (arrayComponentType.isArray()) {
//            for (int i = 0; i < arrLength; i++) {
//                Object arrayValue = Array.get(value, i);
//                defaultAnnotationGenerator.visitArray(arrayAnnoVisitor, arrayValue, null);
//            }
//        } else if (arrayComponentType.isEnum()) {
//            for (int i = 0; i < arrLength; i++) {
//                Object arrayValue = Array.get(value, i);
//                defaultAnnotationGenerator.visitEnum(arrayAnnoVisitor, arrayValue, null, Type.getDescriptor(arrayComponentType));
//            }
//        } else if (Class.class.isAssignableFrom(arrayComponentType)) {
//            for (int i = 0; i < arrLength; i++) {
//                Object arrayValue = Array.get(value, i);
//                defaultAnnotationGenerator.visitClass(arrayAnnoVisitor, null, Type.getType((Class<?>)arrayValue));
//            }
//        } else {
//            for (int i = 0; i < arrLength; i++) {
//                Object arrayValue = Array.get(value, i);
//                defaultAnnotationGenerator.visitOther(arrayAnnoVisitor, null, arrayValue);
//            }
//        }
//    }
}
