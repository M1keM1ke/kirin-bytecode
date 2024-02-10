package ru.mike.kirinbytecode.asm.builder.method;

import ru.mike.kirinbytecode.asm.generator.node.annotation.AnnotationAnnotationGenerator;
import ru.mike.kirinbytecode.asm.generator.node.annotation.AnnotationGenerator;
import ru.mike.kirinbytecode.asm.generator.node.annotation.ArrayAnnotationGenerator;
import ru.mike.kirinbytecode.asm.generator.node.annotation.ClassAnnotationGenerator;
import ru.mike.kirinbytecode.asm.generator.node.annotation.EnumAnnotationGenerator;
import ru.mike.kirinbytecode.asm.generator.node.annotation.OtherAnnotationGenerator;

public interface AnnotationValue<V> {

    V getValue();

    AnnotationGenerator getAnnotationGenerator();

    abstract class AbstractAnnotationValue<V> implements AnnotationValue<V> {
        protected V value;

        protected AbstractAnnotationValue(V value) {
            this.value = value;
        }
    }

    class MainAnnotationAnnotationValue<V> extends AbstractAnnotationValue<V> {
        private AnnotationGenerator annotationGenerator;

        protected MainAnnotationAnnotationValue(V value) {
            super(value);
            this.annotationGenerator = new AnnotationAnnotationGenerator();
        }

        @Override
        public V getValue() {
            return value;
        }

        @Override
        public AnnotationGenerator getAnnotationGenerator() {
            return annotationGenerator;
        }
    }

    class OtherAnnotationAnnotationValue<V> extends AbstractAnnotationValue<V> {
        private AnnotationGenerator annotationGenerator;

        protected OtherAnnotationAnnotationValue(V value) {
            super(value);
            this.annotationGenerator = new OtherAnnotationGenerator();
        }

        @Override
        public V getValue() {
            return value;
        }

        @Override
        public AnnotationGenerator getAnnotationGenerator() {
            return annotationGenerator;
        }
    }

    class ArrayAnnotationValue<V> extends AbstractAnnotationValue<V> {
        private AnnotationGenerator annotationGenerator;

        protected ArrayAnnotationValue(V value) {
            super(value);
            this.annotationGenerator = new ArrayAnnotationGenerator();
        }

        @Override
        public V getValue() {
            return value;
        }

        @Override
        public AnnotationGenerator getAnnotationGenerator() {
            return annotationGenerator;
        }
    }

    class EnumAnnotationAnnotationValue<V> extends AbstractAnnotationValue<V> {
        private AnnotationGenerator annotationGenerator;

        protected EnumAnnotationAnnotationValue(V value) {
            super(value);
            this.annotationGenerator = new EnumAnnotationGenerator();
        }

        @Override
        public V getValue() {
            return value;
        }

        @Override
        public AnnotationGenerator getAnnotationGenerator() {
            return annotationGenerator;
        }
    }

    class ClassAnnotationAnnotationValue<V> extends AbstractAnnotationValue<V> {
        private AnnotationGenerator annotationGenerator;

        protected ClassAnnotationAnnotationValue(V value) {
            super(value);
            this.annotationGenerator = new ClassAnnotationGenerator();
        }

        @Override
        public V getValue() {
            return value;
        }

        @Override
        public AnnotationGenerator getAnnotationGenerator() {
            return annotationGenerator;
        }
    }
}
