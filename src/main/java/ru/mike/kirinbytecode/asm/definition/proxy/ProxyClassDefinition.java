package ru.mike.kirinbytecode.asm.definition.proxy;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import ru.mike.kirinbytecode.asm.definition.Definition;
import ru.mike.kirinbytecode.asm.generator.NameGenerator;
import ru.mike.kirinbytecode.asm.generator.name.DefaultUUIDLazyProxyClassNameGenerator;
import ru.mike.kirinbytecode.asm.generator.node.DefaultClassGenerator;
import ru.mike.kirinbytecode.asm.matcher.NameMatcher;

import java.util.Objects;

@Getter
@Setter
public class ProxyClassDefinition<T> implements Definition {
    public static String JAVA_PACKAGE_PREFIX = "java";
    public static String DEFAULT_PROXY_PACKAGE = "example";

    private Class<T> originalClazz;
    private DefaultClassGenerator<T> classGenerator;
    private String generatedProxyClassName;

    private String proxyPackageName;

    @Getter(AccessLevel.NONE)
    @Setter(AccessLevel.NONE)
    private NameGenerator nameGenerator;
    private NameMatcher<T> nameMatcher;
    private byte[] bytecodeClazz;
    private Class<? extends T> generatedClazz;
    private ProxyClassMethodsDefinition<T> proxyClassMethodsDefinition;
    private ProxyClassFieldsDefinition<T> proxyClassFieldsDefinition;
    private ProxyClassInterfacesDefinition<T> proxyClassInterfacesDefinition;

    public ProxyClassDefinition() {
        this.proxyClassMethodsDefinition = new ProxyClassMethodsDefinition<>(this);
        this.proxyClassFieldsDefinition = new ProxyClassFieldsDefinition<>(this);
        this.proxyClassInterfacesDefinition = new ProxyClassInterfacesDefinition<>(this);
    }

    public void setNameGeneratorIfNeeded(NameGenerator nameGenerator) {
        if (Objects.isNull(this.nameGenerator)) {
            this.nameGenerator = nameGenerator;
        }
    }

    public NameGenerator getNameGenerator() {
//      если до получения генератора он не был установлен, то присваиваем дефолтную реализацию
        if (Objects.isNull(this.nameGenerator)) {
            setNameGeneratorIfNeeded(new DefaultUUIDLazyProxyClassNameGenerator());
        }

        return this.nameGenerator;
    }

    /**
     * Устанавливает имя пакета для прокси класса {@link ProxyClassDefinition#proxyPackageName}
     * Если имя пакета начинается с {@link ProxyClassDefinition#JAVA_PACKAGE_PREFIX},
     * то имя пакета для прокси класса будет {@link ProxyClassDefinition#DEFAULT_PROXY_PACKAGE}
     * @param proxyPackageName устанавливаемое имя пакета для прокси класса
     */
    public void setProxyPackageName(String proxyPackageName) {
        if (proxyPackageName.startsWith(JAVA_PACKAGE_PREFIX)) {
            this.proxyPackageName = DEFAULT_PROXY_PACKAGE;
        } else {
            this.proxyPackageName = proxyPackageName;
        }
    }

}
