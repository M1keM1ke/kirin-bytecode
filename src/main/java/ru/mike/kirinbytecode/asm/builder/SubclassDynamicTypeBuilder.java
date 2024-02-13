package ru.mike.kirinbytecode.asm.builder;

import lombok.extern.slf4j.Slf4j;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.tree.ClassNode;
import ru.mike.kirinbytecode.asm.builder.field.DefaultFieldDefinitionBuilder;
import ru.mike.kirinbytecode.asm.builder.field.FieldDefinitionBuilder;
import ru.mike.kirinbytecode.asm.builder.method.AnnotationDefinition;
import ru.mike.kirinbytecode.asm.builder.method.DefaultMethodDefinitionBuilder;
import ru.mike.kirinbytecode.asm.builder.method.DefaultMethodInterceptionBuilder;
import ru.mike.kirinbytecode.asm.builder.method.MethodDefinitionBuilder;
import ru.mike.kirinbytecode.asm.builder.method.MethodInterceptionStagesBuilder;
import ru.mike.kirinbytecode.asm.definition.FieldDefinition;
import ru.mike.kirinbytecode.asm.definition.InterceptedMethodDefinition;
import ru.mike.kirinbytecode.asm.definition.InterceptedMethodsDefinition;
import ru.mike.kirinbytecode.asm.definition.InterfaceDefinition;
import ru.mike.kirinbytecode.asm.definition.MethodDefinition;
import ru.mike.kirinbytecode.asm.definition.proxy.ProxyClassAnnotationsDefinition;
import ru.mike.kirinbytecode.asm.definition.proxy.ProxyClassDefinedMethodsDefinition;
import ru.mike.kirinbytecode.asm.definition.proxy.ProxyClassDefinition;
import ru.mike.kirinbytecode.asm.definition.proxy.ProxyClassInterceptedMethodsDefinition;
import ru.mike.kirinbytecode.asm.exception.notfound.MatcherNotFoundException;
import ru.mike.kirinbytecode.asm.exception.notfound.MethodNotFoundException;
import ru.mike.kirinbytecode.asm.generator.name.ConstantProxyClassNameGenerator;
import ru.mike.kirinbytecode.asm.generator.node.DefaultClassGenerator;
import ru.mike.kirinbytecode.asm.generator.node.annotation.node.AnnotationNodeContext;
import ru.mike.kirinbytecode.asm.matcher.NameMatcher;
import ru.mike.kirinbytecode.asm.matcher.name.NameMatchersUtil;
import ru.mike.kirinbytecode.asm.util.PathDelimiter;

import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import static org.objectweb.asm.ClassWriter.COMPUTE_FRAMES;

@Slf4j
public class SubclassDynamicTypeBuilder<T> implements Builder<T> {
    private DefaultClassGenerator<T> generator;
    private ProxyClassDefinition<T> definition;

    public SubclassDynamicTypeBuilder(ProxyClassDefinition<T> definition) {
        this.definition = definition;
        generator = new DefaultClassGenerator<>(definition);

        definition.setClassGenerator(generator);
        definition.setProxyPackageName(definition.getOriginalClazz().getPackage().getName());
    }

    @Override
    public MethodInterceptionStagesBuilder<T> method(NameMatcher<T> nameMatcher) {
        Method[] methods = definition.getOriginalClazz().getDeclaredMethods();

//      отобрали методы по фильтру
        List<Method> methodsByMatcher = getMethodsForProxy(nameMatcher, methods);

//      если разными двумя матчерами выберется один и тот же метод, то к нему могут продублироваться настройки
//      или если матчером выберется несколько методов, то к ним применятся все последующие настройки одновременно
        if (methodsByMatcher.size() > 1) {
            log.warn("Found {} methods by matcher:{}. " +
                    "This may produce some errors like duplication annotations for the same method",
                    methodsByMatcher.size(), nameMatcher
            );
        }

//      получили класс ProxyClassInterceptedMethodsDefinition, хранящий все описания прокси методов
        ProxyClassInterceptedMethodsDefinition<T> classMethodsDefinition = definition.getProxyClassInterceptedMethodsDefinition();

//      создали InterceptedMethodsDefinition, который хранит мапу методов, собранных по одному фильтру,
//      и добавили в его мапу список методов выше
        InterceptedMethodsDefinition<T> interceptedMethodsDefinition = new InterceptedMethodsDefinition<>(definition);
        interceptedMethodsDefinition.addProxyMethods(methodsByMatcher);

//      добавили созданный InterceptedMethodsDefinition в список ProxyClassInterceptedMethodsDefinition
        classMethodsDefinition.addInterceptedMethodsDefinition(interceptedMethodsDefinition);

//      вернули новый DefaultMethodInterceptionBuilder, который дополнит описания методов в InterceptedMethodsDefinition
        return new DefaultMethodInterceptionBuilder<>(interceptedMethodsDefinition, this);
    }

    @Override
    public MethodDefinitionBuilder<T> defineMethod(String name, Class<?> returnType, int modifiers) {
        ProxyClassDefinedMethodsDefinition<T> proxyClassDefinedMethodsDefinition = definition.getProxyClassDefinedMethodsDefinition();
        MethodDefinition<T> methodDefinition = proxyClassDefinedMethodsDefinition.addNewMethod(name, returnType, modifiers);

        return new DefaultMethodDefinitionBuilder<>(definition, methodDefinition, this);
    }

    @Override
    public FieldDefinitionBuilder<T> defineField(String name, Type type, Integer... modifiers) {
        definition.getProxyClassFieldsDefinition().createFieldDefinition(name, type, modifiers);

        return new DefaultFieldDefinitionBuilder<>(definition, name, this);
    }

    @Override
    public UnloadedType<T> make() {
//      сначала генерируем все независимые от контекста ноды - методы, поля
        generateInterceptedMethods(definition);
        generateDefinedMethods(definition);
        generateFields(definition);

//        дальше генерируем ClassNode и все ноды собираем в ClassNode
        generateClassNode(definition);
        generateClassNodeAnnotations(definition);

        addInterceptedMethodsToClassNode(definition);
        addDefinedMethodsToClassNode(definition);
        addFieldsToClassNode(definition);
        addInterfacesToClassNode(definition);

        ClassNode cn = definition.getClassGenerator().getCn();
        ClassWriter cw = new ClassWriter(COMPUTE_FRAMES);
        cn.accept(cw);
        byte[] bytecode = cw.toByteArray();

        definition.setBytecodeClazz(bytecode);

        return new UnloadedType<>(definition);
    }

    private void generateClassNodeAnnotations(ProxyClassDefinition<T> definition) {
        ClassNode cn = definition.getClassGenerator().getCn();

        ProxyClassAnnotationsDefinition<T> proxyClassAnnotationsDefinition = definition
                .getProxyClassAnnotationsDefinition();
        Collection<AnnotationDefinition> annotationDefinitions = proxyClassAnnotationsDefinition
                .getAnnotationDefinitions()
                .values();

        for (AnnotationDefinition annotationDefinition : annotationDefinitions) {
            proxyClassAnnotationsDefinition.getAnnotationNodeGenerator().visitNodeAnnotation(
                    AnnotationNodeContext.builder()
                            .annotationClassNodeContext(
                                    AnnotationNodeContext.AnnotationClassNodeContext.builder()
                                            .classNode(cn)
                                            .build()
                            )
                            .build(),
                    annotationDefinition

            );
        }
    }

    @Override
    public Builder<T> implement(Type type) {
        definition.getProxyClassInterfacesDefinition().addProxyInterface(type);
        return this;
    }

    @Override
    public Builder<T> name(String name) {
        definition.setNameGeneratorIfNeeded(new ConstantProxyClassNameGenerator(name));
        return this;
    }

    @Override
    public Builder<T> annotateType(AnnotationDefinition annotationDefinition) {
        definition.getProxyClassAnnotationsDefinition().addAnnotation(annotationDefinition);
        return this;
    }

    private void generateClassNode(ProxyClassDefinition<T> definition) {
        DefaultClassGenerator<T> classGenerator = definition.getClassGenerator();
        classGenerator.generateNode();
    }

    private void generateFields(ProxyClassDefinition<T> definition) {
        Collection<FieldDefinition<T>> fieldsDefinitions = definition
                .getProxyClassFieldsDefinition()
                .getProxyFields()
                .values();

        generateFields(fieldsDefinitions);
    }

    private void generateDefinedMethods(ProxyClassDefinition<T> definition) {
        Collection<MethodDefinition<T>> definedMethodsDefinitions = definition
                .getProxyClassDefinedMethodsDefinition()
                .getDefinedMethods()
                .values();

        generateMethods(definedMethodsDefinitions);
    }

    private void generateInterceptedMethods(ProxyClassDefinition<T> definition) {
        List<InterceptedMethodDefinition<T>> interceptedMethodDefinitions = definition
                .getProxyClassInterceptedMethodsDefinition()
                .getAllMethodsDefinitions();

        generateMethods(interceptedMethodDefinitions);
    }

    private void addInterfacesToClassNode(ProxyClassDefinition<T> definition) {
        ClassNode cn = definition.getClassGenerator().getCn();

        var interfacesDefinitions = definition
                .getProxyClassInterfacesDefinition()
                .getProxyInterfaces()
                .values();

        for (InterfaceDefinition<T> interfaceDefinition : interfacesDefinitions) {
            cn.interfaces.add(interfaceDefinition.getFullName(PathDelimiter.RIGHT_SLASH));
        }
    }

    private void addFieldsToClassNode(ProxyClassDefinition<T> definition) {
        ClassNode cn = definition.getClassGenerator().getCn();

        Collection<FieldDefinition<T>> fieldsDefinitions = definition
                .getProxyClassFieldsDefinition()
                .getProxyFields()
                .values();

        for (FieldDefinition<T> fieldDefinition : fieldsDefinitions) {
            cn.fields.add(fieldDefinition.getFieldGenerator().getFn());
        }
    }

    private void addInterceptedMethodsToClassNode(ProxyClassDefinition<T> definition) {
        List<InterceptedMethodDefinition<T>> interceptedMethodDefinitions = definition
                .getProxyClassInterceptedMethodsDefinition()
                .getAllMethodsDefinitions();

        addMethodsToClassNode(definition, interceptedMethodDefinitions);
    }

    private void addDefinedMethodsToClassNode(ProxyClassDefinition<T> definition) {
        Collection<MethodDefinition<T>> definedMethodsDefinitions = definition
                .getProxyClassDefinedMethodsDefinition()
                .getDefinedMethods()
                .values();

        addMethodsToClassNode(definition, definedMethodsDefinitions);
    }

    private void addMethodsToClassNode(ProxyClassDefinition<T> definition, Collection<? extends MethodDefinition<T>> methodDefinitions) {
        ClassNode cn = definition.getClassGenerator().getCn();

        for (MethodDefinition<T> methodDefinition : methodDefinitions) {
            cn.methods.add(methodDefinition.getMethodGenerator().getMn());
        }
    }

    private void generateFields(Collection<FieldDefinition<T>> allFieldsDefinitions) {
        for (FieldDefinition<T> fieldDefinition : allFieldsDefinitions) {
            fieldDefinition.getFieldGenerator().generateNode();
        }
    }

    private void generateMethods(Collection<? extends MethodDefinition<T>> interceptedMethodDefinitions) {
        for (MethodDefinition<T> methodDefinition : interceptedMethodDefinitions) {
            methodDefinition.getMethodGenerator().generateNode();
        }
    }

    private List<Method> getMethodsForProxy(NameMatcher<T> nameMatcher, Method[] methods) {
        List<Method> suitableMethods = NameMatchersUtil
                .getNameMatchersOrThrow(nameMatcher.getValue())
                .stream()
                .filter(nm -> nm.isCurrentNameMatcher(nameMatcher))
                .findFirst()
                .orElseThrow(() -> new MatcherNotFoundException("Unable to find suitable matcher. " +
                        "Current matcher is:" + nameMatcher.getClass().getSimpleName()))
                .findSuitableMethods(Arrays.asList(methods));

        if (suitableMethods.isEmpty()) {
            throw new MethodNotFoundException("Unable to find any method by name:" + nameMatcher.getValue());
        }

        return suitableMethods;
    }
}
