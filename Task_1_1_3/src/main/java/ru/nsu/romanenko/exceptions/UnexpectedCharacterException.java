package ru.nsu.romanenko.exceptions;

public class UnexpectedCharacterException extends ExpressionParserException {
    public UnexpectedCharacterException(char character) {
        super("Unexpected character: " + character);
    }
}
