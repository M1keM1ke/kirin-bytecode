package ru.mike.kirinbytecode.asm.builder;

import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.tree.ClassNode;
import ru.mike.kirinbytecode.asm.builder.field.DefaultFieldDefinitionBuilder;
import ru.mike.kirinbytecode.asm.builder.field.FieldDefinitionBuilder;
import ru.mike.kirinbytecode.asm.builder.method.DefaultMethodDefinitionBuilder;
import ru.mike.kirinbytecode.asm.builder.method.MethodDefinitionBuilder;
import ru.mike.kirinbytecode.asm.definition.FieldDefinition;
import ru.mike.kirinbytecode.asm.definition.InterceptedMethodsDefinition;
import ru.mike.kirinbytecode.asm.definition.InterfaceDefinition;
import ru.mike.kirinbytecode.asm.definition.MethodDefinition;
import ru.mike.kirinbytecode.asm.definition.proxy.ProxyClassDefinition;
import ru.mike.kirinbytecode.asm.definition.proxy.ProxyClassMethodsDefinition;
import ru.mike.kirinbytecode.asm.exception.MatcherNotFoundException;
import ru.mike.kirinbytecode.asm.exception.MethodNotFoundException;
import ru.mike.kirinbytecode.asm.generator.name.ConstantProxyClassNameGenerator;
import ru.mike.kirinbytecode.asm.generator.node.DefaultClassGenerator;
import ru.mike.kirinbytecode.asm.matcher.NameMatcher;
import ru.mike.kirinbytecode.asm.matcher.name.NameMatchersUtil;
import ru.mike.kirinbytecode.asm.util.PathDelimiter;

import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static org.objectweb.asm.ClassWriter.COMPUTE_FRAMES;

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
    public MethodDefinitionBuilder<T> method(NameMatcher<T> nameMatcher) {
        definition.setNameMatcher(nameMatcher);

        Method[] methods = definition.getOriginalClazz().getDeclaredMethods();

//      отобрали методы по фильтру
        List<Method> methodsByMatcher = getMethodsForProxy(nameMatcher, methods);

//      получили класс ProxyClassMethodsDefinition, хранящий все описания прокси методов
        ProxyClassMethodsDefinition<T> classMethodsDefinition = definition.getProxyClassMethodsDefinition();

//      создали InterceptedMethodsDefinition, который хранит мапу методов, собранных по одному фильтру,
//      и добавили в его мапу список методов выше
        InterceptedMethodsDefinition<T> interceptedMethodsDefinition = new InterceptedMethodsDefinition<T>(definition);
        interceptedMethodsDefinition.addProxyMethods(methodsByMatcher);

//      добавили созданный InterceptedMethodsDefinition в список ProxyClassMethodsDefinition
        classMethodsDefinition.addInterceptedMethodsDefinition(interceptedMethodsDefinition);

//      вернули новый DefaultMethodDefinitionBuilder, который дополнит описания методов в InterceptedMethodsDefinition
        return new DefaultMethodDefinitionBuilder<T>(interceptedMethodsDefinition, this);
    }

    @Override
    public FieldDefinitionBuilder<T> defineField(String name, Type type, Integer... modifiers) {
        definition.getProxyClassFieldsDefinition().createFieldDefinition(name, type, modifiers);

        return new DefaultFieldDefinitionBuilder<>(definition, name, this);
    }

    @Override
    public UnloadedType<T> make() {
//      сначала генерируем все независимые от контекста ноды - методы, поля
        var allMethodsDefinitions = definition.getProxyClassMethodsDefinition().getAllMethodsDefinitions();

        for (MethodDefinition<T> methodDefinition : allMethodsDefinitions) {
            methodDefinition.getMethodGenerator().generateNode();
        }

        Map<String, FieldDefinition<T>> proxyFields = definition.getProxyClassFieldsDefinition().getProxyFields();
        var allFieldsDefinitions = proxyFields.values();

        for (FieldDefinition<T> fieldDefinition : allFieldsDefinitions) {
            fieldDefinition.getFieldGenerator().generateNode();
        }

//        дальше генерируем ClassNode и все ноды собираем в ClassNode
        DefaultClassGenerator<T> classGenerator = definition.getClassGenerator();
        classGenerator.generateNode();

        ClassNode cn = classGenerator.getCn();

        for (MethodDefinition<T> methodDefinition : allMethodsDefinitions) {
            cn.methods.add(methodDefinition.getMethodGenerator().getMn());
        }

        for (FieldDefinition<T> fieldDefinition : allFieldsDefinitions) {
            cn.fields.add(fieldDefinition.getFieldGenerator().getFn());
        }

        var allInterfacesDefinitions = definition.getProxyClassInterfacesDefinition().getProxyInterfaces().values();

        for (InterfaceDefinition<T> interfaceDefinition : allInterfacesDefinitions) {
            cn.interfaces.add(interfaceDefinition.getFullName(PathDelimiter.RIGHT_SLASH));
        }

        ClassWriter cw = new ClassWriter(COMPUTE_FRAMES);
        cn.accept(cw);
        byte[] bytecode = cw.toByteArray();

        definition.setBytecodeClazz(bytecode);

        return new UnloadedType<>(definition);
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

    private List<Method> getMethodsForProxy(NameMatcher<T> nameMatcher, Method[] methods) {
        List<Method> suitableMethods = NameMatchersUtil
                .getNameMatchersOrThrow(nameMatcher.getValue(), nameMatcher.getMode())
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
