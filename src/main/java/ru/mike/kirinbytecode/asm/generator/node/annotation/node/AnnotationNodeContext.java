package ru.mike.kirinbytecode.asm.generator.node.annotation.node;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.MethodNode;

import javax.annotation.Nullable;

@Builder
@AllArgsConstructor
@Getter
@ToString
public class AnnotationNodeContext {
    private AnnotationClassNodeContext annotationClassNodeContext;
    private AnnotationMethodNodeContext annotationMethodNodeContext;

    @Builder
    @AllArgsConstructor
    @Getter
    @ToString
    public static class AnnotationClassNodeContext {
        @Nullable
        private ClassNode classNode;
    }

    @Builder
    @AllArgsConstructor
    @Getter
    @ToString
    public static class AnnotationMethodNodeContext {
        @Nullable
        private MethodNode methodNode;

        @Nullable
        private int methodParameterNum;
    }
}
