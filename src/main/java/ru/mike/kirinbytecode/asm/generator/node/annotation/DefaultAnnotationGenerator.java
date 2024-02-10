package ru.mike.kirinbytecode.asm.generator.node.annotation;

import lombok.SneakyThrows;
import org.objectweb.asm.AnnotationVisitor;
import org.objectweb.asm.Type;
import org.objectweb.asm.tree.MethodNode;
import ru.mike.kirinbytecode.asm.builder.method.AnnotationDefinition;
import ru.mike.kirinbytecode.asm.builder.method.AnnotationValue;
import ru.mike.kirinbytecode.asm.definition.parameter.ParameterDefinition;
import ru.mike.kirinbytecode.asm.generator.node.annotation.processor.AnnotationParameterTypeProcessor;

import javax.annotation.Nullable;
import java.lang.annotation.Annotation;
import java.lang.reflect.Array;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;
import java.util.ServiceLoader;
import java.util.stream.StreamSupport;

import static ru.mike.kirinbytecode.asm.util.AnnotationUtil.getAnnotationMethods;

public class DefaultAnnotationGenerator {
    ServiceLoader<AnnotationParameterTypeProcessor> loader;

    public DefaultAnnotationGenerator() {
        loader = ServiceLoader.load(AnnotationParameterTypeProcessor.class);
    }

    public void visitParametersAnnotations(List<ParameterDefinition> parameterDefinitions, MethodNode mn) {
        for (int i = 0, parameterDefinitionsSize = parameterDefinitions.size(); i < parameterDefinitionsSize; i++) {
            ParameterDefinition parameterDefinition = parameterDefinitions.get(i);
            List<AnnotationDefinition> parameterAnnoDefinitions = parameterDefinition.getAnnotationDefinitions();

//          для каждого параметра проставляем аннотации
            for (AnnotationDefinition annotationDefinition : parameterAnnoDefinitions) {
                visitParameterAnnotation(mn, i, annotationDefinition);
            }
        }
    }

    public void visitParameterAnnotation(MethodNode mn, int parameterNum, AnnotationDefinition annotationDefinition) {
        AnnotationVisitor annotationVisitor = mn.visitParameterAnnotation(parameterNum, Type.getDescriptor(annotationDefinition.getType()), true);

        Map<String, AnnotationValue<?>> annotationValues = annotationDefinition.getValues();

//              для аннотации проставляем ее параметры
        for (Map.Entry<String, AnnotationValue<?>> property : annotationValues.entrySet()) {
            visit(annotationVisitor, property.getKey(), property.getValue());
        }
    }

    @SneakyThrows
    public void visit(AnnotationVisitor annotationVisitor, String property, AnnotationValue<?> annotationValue) {
        Object value = annotationValue.getValue();
        AnnotationGenerationContext agc = AnnotationGenerationContext.builder()
                .annotationVisitor(annotationVisitor)
                .name(property)
                .value(value)
                .annotationGenerator(annotationValue.getAnnotationGenerator())
                .build();

        StreamSupport.stream(loader.spliterator(), false)
                .filter(ap -> ap.isSuitableProcessor(agc))
                .findFirst()
                .orElseThrow(() -> new RuntimeException(
                        "Not found suitable AnnotationParameterTypeProcessor. Context:" + agc))
                .process(agc);
    }

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
//      по идее такого не может быть: двойной массив не может быть в качестве параметра аннотации
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

    public void visitEnum(AnnotationVisitor annotationVisitor, Object value, @Nullable String name, String descriptor) throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {
        String enumName = (String) value.getClass().getMethod("name").invoke(value);
        annotationVisitor.visitEnum(name, descriptor, enumName);
    }

    public void visitClass(AnnotationVisitor annotationVisitor, @Nullable String name, Object value) {
        annotationVisitor.visit(name, value);
    }

    public void visitOther(AnnotationVisitor annotationVisitor, @Nullable String name, Object value) {
        annotationVisitor.visit(name, value);
    }
}
