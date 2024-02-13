package ru.mike.kirinbytecode.asm.builder.method;

import ru.mike.kirinbytecode.asm.generator.node.annotation.type.AnnotationAnnotationTypeGenerator;
import ru.mike.kirinbytecode.asm.generator.node.annotation.type.AnnotationTypeGenerator;
import ru.mike.kirinbytecode.asm.generator.node.annotation.type.ArrayAnnotationTypeGenerator;
import ru.mike.kirinbytecode.asm.generator.node.annotation.type.ClassAnnotationTypeGenerator;
import ru.mike.kirinbytecode.asm.generator.node.annotation.type.EnumAnnotationTypeGenerator;
import ru.mike.kirinbytecode.asm.generator.node.annotation.type.OtherAnnotationTypeGenerator;

public interface AnnotationValue<V> {

    V getValue();

    AnnotationTypeGenerator getAnnotationGenerator();

    abstract class AbstractAnnotationValue<V> implements AnnotationValue<V> {
        protected V value;

        protected AbstractAnnotationValue(V value) {
            this.value = value;
        }
    }

    class MainAnnotationAnnotationValue<V> extends AbstractAnnotationValue<V> {
        private AnnotationTypeGenerator annotationGenerator;

        protected MainAnnotationAnnotationValue(V value) {
            super(value);
            this.annotationGenerator = new AnnotationAnnotationTypeGenerator();
        }

        @Override
        public V getValue() {
            return value;
        }

        @Override
        public AnnotationTypeGenerator getAnnotationGenerator() {
            return annotationGenerator;
        }
    }

    class OtherAnnotationAnnotationValue<V> extends AbstractAnnotationValue<V> {
        private AnnotationTypeGenerator annotationGenerator;

        protected OtherAnnotationAnnotationValue(V value) {
            super(value);
            this.annotationGenerator = new OtherAnnotationTypeGenerator();
        }

        @Override
        public V getValue() {
            return value;
        }

        @Override
        public AnnotationTypeGenerator getAnnotationGenerator() {
            return annotationGenerator;
        }
    }

    class ArrayAnnotationValue<V> extends AbstractAnnotationValue<V> {
        private AnnotationTypeGenerator annotationGenerator;

        protected ArrayAnnotationValue(V value) {
            super(value);
            this.annotationGenerator = new ArrayAnnotationTypeGenerator();
        }

        @Override
        public V getValue() {
            return value;
        }

        @Override
        public AnnotationTypeGenerator getAnnotationGenerator() {
            return annotationGenerator;
        }
    }

    class EnumAnnotationAnnotationValue<V> extends AbstractAnnotationValue<V> {
        private AnnotationTypeGenerator annotationGenerator;

        protected EnumAnnotationAnnotationValue(V value) {
            super(value);
            this.annotationGenerator = new EnumAnnotationTypeGenerator();
        }

        @Override
        public V getValue() {
            return value;
        }

        @Override
        public AnnotationTypeGenerator getAnnotationGenerator() {
            return annotationGenerator;
        }
    }

    class ClassAnnotationAnnotationValue<V> extends AbstractAnnotationValue<V> {
        private AnnotationTypeGenerator annotationGenerator;

        protected ClassAnnotationAnnotationValue(V value) {
            super(value);
            this.annotationGenerator = new ClassAnnotationTypeGenerator();
        }

        @Override
        public V getValue() {
            return value;
        }

        @Override
        public AnnotationTypeGenerator getAnnotationGenerator() {
            return annotationGenerator;
        }
    }
}
