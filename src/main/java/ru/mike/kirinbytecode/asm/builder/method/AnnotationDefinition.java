package ru.mike.kirinbytecode.asm.builder.method;

import java.lang.annotation.Annotation;
import java.util.HashMap;
import java.util.Map;

public interface AnnotationDefinition {

    Map<String, AnnotationValue<?>> getValues();

    Class<? extends Annotation> getType();

    class Builder {
        private Class<? extends Annotation> annotationType;
        private Map<String, AnnotationValue<?>> annotationValues;

        protected Builder(Class<? extends Annotation> annotationType) {
            this.annotationType = annotationType;
            this.annotationValues = new HashMap<>();
        }

        public static AnnotationDefinition.Builder ofType(Class<? extends Annotation> annotationType) {
            return new Builder(annotationType);
        }

        public AnnotationDefinition.Builder define(String propertyName, AnnotationValue<?> annotationValue) {
            annotationValues.put(propertyName, annotationValue);
            return this;
        }

        public AnnotationDefinition.Builder define(String propertyName, boolean value) {
            return this.define(propertyName, new AnnotationValue.OtherAnnotationAnnotationValue<>(value));
        }

        public AnnotationDefinition.Builder define(String propertyName, byte value) {
            return this.define(propertyName, new AnnotationValue.OtherAnnotationAnnotationValue<>(value));
        }

        public AnnotationDefinition.Builder define(String propertyName, short value) {
            return this.define(propertyName, new AnnotationValue.OtherAnnotationAnnotationValue<>(value));
        }

        public AnnotationDefinition.Builder define(String propertyName, char value) {
            return this.define(propertyName, new AnnotationValue.OtherAnnotationAnnotationValue<>(value));
        }

        public AnnotationDefinition.Builder define(String propertyName, int value) {
            return this.define(propertyName, new AnnotationValue.OtherAnnotationAnnotationValue<>(value));
        }

        public AnnotationDefinition.Builder define(String propertyName, long value) {
            return this.define(propertyName, new AnnotationValue.OtherAnnotationAnnotationValue<>(value));
        }

        public AnnotationDefinition.Builder define(String propertyName, float value) {
            return this.define(propertyName, new AnnotationValue.OtherAnnotationAnnotationValue<>(value));
        }

        public AnnotationDefinition.Builder define(String propertyName, double value) {
            return this.define(propertyName, new AnnotationValue.OtherAnnotationAnnotationValue<>(value));
        }

        public AnnotationDefinition.Builder define(String propertyName, String value) {
            return this.define(propertyName, new AnnotationValue.OtherAnnotationAnnotationValue<>(value));
        }

        public AnnotationDefinition.Builder define(String propertyName, Annotation value) {
            return this.define(propertyName, new AnnotationValue.MainAnnotationAnnotationValue<>(value));
        }

        public AnnotationDefinition.Builder define(String propertyName, Enum<?> value) {
            return this.define(propertyName, new AnnotationValue.EnumAnnotationAnnotationValue<>(value));
        }

        public AnnotationDefinition.Builder define(String propertyName, Class<?> value) {
            return this.define(propertyName, new AnnotationValue.ClassAnnotationAnnotationValue<>(value));
        }

        public AnnotationDefinition.Builder define(String propertyName, boolean[] values) {
            return this.define(propertyName, new AnnotationValue.OtherAnnotationAnnotationValue<>(values));
        }

        public AnnotationDefinition.Builder define(String propertyName, byte[] values) {
            return this.define(propertyName, new AnnotationValue.OtherAnnotationAnnotationValue<>(values));
        }

        public AnnotationDefinition.Builder define(String propertyName, short[] values) {
            return this.define(propertyName, new AnnotationValue.OtherAnnotationAnnotationValue<>(values));
        }

        public AnnotationDefinition.Builder define(String propertyName, char[] values) {
            return this.define(propertyName, new AnnotationValue.OtherAnnotationAnnotationValue<>(values));
        }

        public AnnotationDefinition.Builder define(String propertyName, int[] values) {
            return this.define(propertyName, new AnnotationValue.OtherAnnotationAnnotationValue<>(values));
        }

        public AnnotationDefinition.Builder define(String propertyName, long[] values) {
            return this.define(propertyName, new AnnotationValue.OtherAnnotationAnnotationValue<>(values));
        }

        public AnnotationDefinition.Builder define(String propertyName, float[] values) {
            return this.define(propertyName, new AnnotationValue.OtherAnnotationAnnotationValue<>(values));
        }

        public AnnotationDefinition.Builder define(String propertyName, double[] values) {
            return this.define(propertyName, new AnnotationValue.OtherAnnotationAnnotationValue<>(values));
        }

        public AnnotationDefinition.Builder define(String propertyName, String[] values) {
            return this.define(propertyName, new AnnotationValue.OtherAnnotationAnnotationValue<>(values));
        }

        public AnnotationDefinition.Builder define(String propertyName, Annotation[] annotations) {
            return this.define(propertyName, new AnnotationValue.ArrayAnnotationValue<>(annotations));
        }

        public AnnotationDefinition.Builder define(String propertyName, Enum<?>[] enums) {
            return this.define(propertyName, new AnnotationValue.ArrayAnnotationValue<>(enums));
        }

        public AnnotationDefinition.Builder define(String propertyName, Class<?>[] classes) {
            return this.define(propertyName, new AnnotationValue.ArrayAnnotationValue<>(classes));
        }

        public AnnotationDefinition build() {
            return new DefaultAnnotationDefinition(annotationType, annotationValues);
        }

    }
}
