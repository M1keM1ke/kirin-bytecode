package ru.mike.kirinbytecode.asm.exception.incorrect;

public class IncorrectModifierException extends RuntimeException {

    public IncorrectModifierException(String message) {
        super(message);
    }

    public static <T> void throwIncorrectPrivate(String originalClassName, String interceptedMethodName) {
        String errorMessage = String.format("Unable to generate proxy method by super value. " +
                        "Super method '%s' in class '%s' has private modifier. " +
                        "Private methods can't be called by 'super'. " +
                        "Change method modifier in super class or remove proxying '%s' method.",
                interceptedMethodName, originalClassName, interceptedMethodName
        );

        throw new IncorrectModifierException(errorMessage);
    }

    public static <T> void throwIncorrectFinal(String originalClassName, String interceptedMethodName) {
        String errorMessage = String.format("Super method '%s' in class '%s' has final modifier. " +
                        "Generated proxy method with same name can't be overridden. " +
                        "Change method modifier in super class or remove proxying this method.",
                interceptedMethodName, originalClassName
        );

        throw new IncorrectModifierException(errorMessage);
    }
}
