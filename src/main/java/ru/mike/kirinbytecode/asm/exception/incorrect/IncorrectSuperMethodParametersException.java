package ru.mike.kirinbytecode.asm.exception.incorrect;

import java.lang.reflect.Method;
import java.util.Arrays;

public class IncorrectSuperMethodParametersException extends RuntimeException {

    public IncorrectSuperMethodParametersException(String message) {
        super(message);
    }

    public static void throwParamsNonNull(Object[] superValueImpParams) {
        String errorMessage = String.format(
                "Incorrect parameters for calling super method in proxy method." +
                        "Expected: null, got: %s", Arrays.toString(superValueImpParams)
        );

        throw new IncorrectSuperMethodParametersException(errorMessage);
    }

    public static void throwParamsIsNull(Method interceptedMethod) {
        String errorMessage = String.format(
                "Incorrect parameters for calling super method in proxy method. Expected: %s, got: null",
                Arrays.toString(interceptedMethod.getParameterTypes())
        );

        throw new IncorrectSuperMethodParametersException(errorMessage);
    }

    public static void throwParamsCountNotEquals(int transmittedParamsCount, int originalParamsCount) {
        String errorMessage = String.format(
                "Incorrect parameters for calling super method in proxy method. Parameters count are not equals." +
                        " Expected: %d, got: %d",
                originalParamsCount, transmittedParamsCount
        );

        throw new IncorrectSuperMethodParametersException(errorMessage);
    }

    public static void throwParamsTypesNotEquals(int paramNumber, Object superValueImpParam, Class<?> interceptedMethodParamType) {
        String errorMessage = String.format(
                "Incorrect parameters for calling super method in proxy method. Parameters types are not equals." +
                        " Expected: %s, got: %s for parameter №%d",
                interceptedMethodParamType, superValueImpParam.getClass(), paramNumber
        );

        throw new IncorrectSuperMethodParametersException(errorMessage);
    }
}
