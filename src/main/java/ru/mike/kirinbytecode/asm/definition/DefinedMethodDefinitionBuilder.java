package ru.mike.kirinbytecode.asm.definition;

import ru.mike.kirinbytecode.asm.definition.parameter.ParameterDefinition;

import java.util.List;
import java.util.function.IntFunction;

import static ru.mike.kirinbytecode.asm.mapper.ParameterMapper.toParameterDefinition;
import static ru.mike.kirinbytecode.asm.util.AsmUtil.getMethodDescriptor;

public class DefinedMethodDefinitionBuilder<T> {

    public ParameterDefinition addNamedParameterAndUpdateMethodDescriptor(
            MethodDefinition<T> methodDefinition, String parameterName, Class<?> parameterType
    ) {
        ParameterDefinition parameterDefinition = addNamedParameter(methodDefinition, parameterName, parameterType);
        updateMethodDescriptor(methodDefinition);

        return parameterDefinition;
    }

    public void addAnonymousParametersAndUpdateMethodDescriptor(MethodDefinition<T> methodDefinition, Class<?>[] parameterTypes) {
        for (Class<?> parameterType : parameterTypes) {
            addAnonymousParameter(methodDefinition, parameterType);
        }

        updateMethodDescriptor(methodDefinition);
    }
    public ParameterDefinition addNamedParameter(MethodDefinition<T> methodDefinition, String parameterName, Class<?> parameterType) {
        List<ParameterDefinition> parameterDefinitions = methodDefinition.getParameterDefinitions();
        int paramsCount = parameterDefinitions.size();

        ParameterDefinition parameterDefinition = toParameterDefinition(parameterName, parameterType, paramsCount);
        parameterDefinitions.add(parameterDefinition);

        return parameterDefinition;
    }


    public void addAnonymousParameter(MethodDefinition<T> methodDefinition, Class<?> parameterType) {
        List<ParameterDefinition> parameterDefinitions = methodDefinition.getParameterDefinitions();
        int paramsCount = parameterDefinitions.size();

        parameterDefinitions.add(toParameterDefinition(getAnonymousParameterName(paramsCount), parameterType, paramsCount));
    }

    public void updateMethodDescriptor(MethodDefinition<T> methodDefinition) {
        List<ParameterDefinition> parameterDefinitions = methodDefinition.getParameterDefinitions();
        Class<?>[] parametersTypes = parameterDefinitions.stream()
                .map(ParameterDefinition::getType)
                .toArray((IntFunction<Class<?>[]>) value -> new Class[parameterDefinitions.size()]);

        methodDefinition.setMethodDescriptor(getMethodDescriptor(methodDefinition.getReturnType(), parametersTypes));
    }

    public String getAnonymousParameterName(Integer parametersCount) {
        return "arg" + parametersCount;
    }
}
