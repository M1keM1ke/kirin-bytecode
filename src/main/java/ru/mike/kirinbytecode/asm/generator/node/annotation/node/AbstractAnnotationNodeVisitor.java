package ru.mike.kirinbytecode.asm.generator.node.annotation.node;

import lombok.SneakyThrows;
import org.objectweb.asm.AnnotationVisitor;
import org.objectweb.asm.Type;

import javax.annotation.Nullable;
import java.lang.annotation.Annotation;
import java.lang.reflect.Array;
import java.lang.reflect.Method;
import java.util.List;

import static ru.mike.kirinbytecode.asm.util.AnnotationUtil.getAnnotationMethods;

public abstract class AbstractAnnotationNodeVisitor implements AnnotationNodeVisitor {

    @Override
    @SneakyThrows
    public void visitAnnotation(AnnotationVisitor annotationVisitor, Object value, String descriptor, @Nullable String name) {
        AnnotationVisitor annoAnnoVisitor = annotationVisitor.visitAnnotation(name, descriptor);

        if (!(value instanceof Annotation)) {
            return;
        }

        List<Method> annotationMethods = getAnnotationMethods(value);

        for (Method annotationMethod : annotationMethods) {
            Class<?> returnType = annotationMethod.getReturnType();
            Object returnValue = annotationMethod.invoke(value);

//          если тип возвращаемого значения метода аннотации - аннотация, то рекурсивно вызываем этот же метод
            if (returnType.isAnnotation()) {
                visitAnnotation(annoAnnoVisitor, returnValue, Type.getDescriptor(returnType), annotationMethod.getName());
            } else if (returnType.isArray()) {
                visitArray(annoAnnoVisitor, returnValue, annotationMethod.getName());
            } else if (returnType.isEnum()) {
                visitEnum(annoAnnoVisitor, returnValue, annotationMethod.getName(), Type.getDescriptor(returnType));
            } else if (Class.class.isAssignableFrom(returnType)) {
                visitClass(annoAnnoVisitor, annotationMethod.getName(), Type.getType((Class<?>) returnValue));
            } else {
                visitOther(annoAnnoVisitor, annotationMethod.getName(), returnValue);
            }
        }
    }

    @Override
    @SneakyThrows
    public void visitArray(AnnotationVisitor annotationVisitor, Object value, @Nullable String name) {
        AnnotationVisitor arrayAnnoVisitor = annotationVisitor.visitArray(name);

        Class<?> valueClass = value.getClass();

        if (!valueClass.isArray()) {
            return;
        }

        int arrLength = Array.getLength(value);
        Class<?> arrayComponentType = valueClass.getComponentType();

        if (arrayComponentType.isAnnotation()) {
            for (int i = 0; i < arrLength; i++) {
                Object arrayValue = Array.get(value, i);
                visitAnnotation(arrayAnnoVisitor, arrayValue, Type.getDescriptor(arrayComponentType), null);
            }
//      если тип массива в возвращаемом значении метода аннотации - массив, то рекурсивно вызываем этот же метод
//      но по идее такого не может быть: двойной массив не может быть в качестве параметра аннотации
        } else if (arrayComponentType.isArray()) {
            for (int i = 0; i < arrLength; i++) {
                Object arrayValue = Array.get(value, i);
                visitArray(arrayAnnoVisitor, arrayValue, null);
            }
        } else if (arrayComponentType.isEnum()) {
            for (int i = 0; i < arrLength; i++) {
                Object arrayValue = Array.get(value, i);
                visitEnum(arrayAnnoVisitor, arrayValue, null, Type.getDescriptor(arrayComponentType));
            }
        } else if (Class.class.isAssignableFrom(arrayComponentType)) {
            for (int i = 0; i < arrLength; i++) {
                Object arrayValue = Array.get(value, i);
                visitClass(arrayAnnoVisitor, null, Type.getType((Class<?>)arrayValue));
            }
        } else {
            for (int i = 0; i < arrLength; i++) {
                Object arrayValue = Array.get(value, i);
                visitOther(arrayAnnoVisitor, null, arrayValue);
            }
        }
    }

    @Override
    @SneakyThrows
    public void visitEnum(AnnotationVisitor annotationVisitor, Object value, @Nullable String name, String descriptor) {
        String enumName = (String) value.getClass().getMethod("name").invoke(value);
        annotationVisitor.visitEnum(name, descriptor, enumName);
    }

    @Override
    public void visitClass(AnnotationVisitor annotationVisitor, @Nullable String name, Object value) {
        annotationVisitor.visit(name, value);
    }

    @Override
    public void visitOther(AnnotationVisitor annotationVisitor, @Nullable String name, Object value) {
        annotationVisitor.visit(name, value);
    }
}
