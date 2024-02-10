package ru.mike.kirinbytecode.asm.definition;

import lombok.Getter;
import ru.mike.kirinbytecode.asm.definition.proxy.ProxyClassDefinition;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

@Getter
public class InterceptedMethodsDefinition<T> implements Definition {
    private List<InterceptedMethodDefinition<T>> proxyMethods;
    private ProxyClassDefinition<T> definition;

    public InterceptedMethodsDefinition(ProxyClassDefinition<T> definition) {
        this.definition = definition;
        proxyMethods = new ArrayList<>();
    }


    /**
     * Создает список {@link InterceptedMethodDefinition <T>} на основе переданного списка,
     * добавляет его в proxyMethods и возвращает созданные {@link InterceptedMethodDefinition <T>}
     * @param methods методы для добавления в proxyMethods
     * @return созданные {@link InterceptedMethodDefinition <T>}
     */
    public List<InterceptedMethodDefinition<T>> addProxyMethods(List<Method> methods) {
        List<InterceptedMethodDefinition<T>> createdMethodsDefinitions = new ArrayList<>();

        for (Method method : methods) {
            createdMethodsDefinitions.add(addProxyMethod(method));
        }

        return createdMethodsDefinitions;
    }

    public InterceptedMethodDefinition<T> addProxyMethod(Method method) {
        InterceptedMethodDefinition<T> methodDefinition = new InterceptedMethodDefinition<>(definition, method);
        proxyMethods.add(methodDefinition);

        return methodDefinition;
    }
}
