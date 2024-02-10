package ru.mike.kirinbytecode.asm.generator.node.annotation.processor;

import com.google.auto.service.AutoService;
import org.objectweb.asm.Type;
import ru.mike.kirinbytecode.asm.generator.node.annotation.AnnotationGenerationContext;
import ru.mike.kirinbytecode.asm.util.AnnotationUtil;

import static ru.mike.kirinbytecode.asm.util.AnnotationUtil.getAnnotationInterfaceOrThrow;


@AutoService(AnnotationParameterTypeProcessor.class)
public class MainAnnotationAnnotationParameterTypeProcessor implements AnnotationParameterTypeProcessor {

    @Override
    public Boolean isSuitableProcessor(AnnotationGenerationContext annotationGenerationContext) {
        return AnnotationUtil.isAnnotation(annotationGenerationContext.getValue());
    }

    @Override
    public void process(AnnotationGenerationContext agc) {
        Object value = agc.getValue();

        agc.getAnnotationGenerator().visit(
                agc.getAnnotationVisitor(),
                value,
                Type.getDescriptor(getAnnotationInterfaceOrThrow(value.getClass())),
                agc.getName()
        );
    }

//    @Override
//    public void process(AnnotationVisitor annotationVisitor, String methodName, Object value) {
////        AnnotationVisitor annoAnnoVisitor = annotationVisitor.visitAnnotation(methodName, Type.getDescriptor(getAnnoClassOrThrow(returnType)));
////
////        visitAnnotation(annoAnnoVisitor, value);
//    }

//    @Override
//    @SneakyThrows
//    public void process(AnnotationVisitor annotationVisitor, Object value, Method annotationMethod) {
//        Class<?> returnType = annotationMethod.getReturnType();
//
//
//        AnnotationVisitor annoAnnoVisitor = annotationVisitor.visitAnnotation(annotationMethod.getName(), Type.getDescriptor(returnType));
//        Object returnValue = annotationMethod.invoke(value);
//
//        new DefaultAnnotationGenerator().visitAnnotation(annoAnnoVisitor, returnValue);
//    }


}
