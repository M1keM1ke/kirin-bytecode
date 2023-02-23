package ru.mike.kirinbytecode.asm.definition;

import lombok.Getter;
import ru.mike.kirinbytecode.asm.definition.proxy.ProxyClassDefinition;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Getter
public class InterceptedMethodsDefinition<T> implements Definition {
    private HashMap<String, MethodDefinition<T>> proxyMethods;
    private ProxyClassDefinition<T> definition;

    public InterceptedMethodsDefinition(ProxyClassDefinition<T> definition) {
        this.definition = definition;
        proxyMethods = new HashMap<>();
    }


    /**
     * Создает список {@link MethodDefinition<T>} на основе переданного списка,
     * добавляет его в proxyMethods и возвращает созданные {@link MethodDefinition<T>}
     * @param methods методы для добавления в proxyMethods
     * @return созданные {@link MethodDefinition<T>}
     */
    public List<MethodDefinition<T>> addProxyMethods(List<Method> methods) {
        List<MethodDefinition<T>> createdMethodsDefinitions = new ArrayList<>();

        for (Method method : methods) {
            createdMethodsDefinitions.add(addProxyMethod(method));
        }

        return createdMethodsDefinitions;
    }

    public MethodDefinition<T> addProxyMethod(Method method) {
        MethodDefinition<T> methodDefinition = new MethodDefinition<T>(definition, method);
        proxyMethods.put(generateKey(methodDefinition.getMethodDescriptor(), method.getName()), methodDefinition);

        return methodDefinition;
    }

    public String generateKey(String descriptor, String methodName) {
        return descriptor + methodName;
    }
}
