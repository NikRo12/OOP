package ru.nsu.romanenko.exceptions;

public class UnexpectedTokenException extends ExpressionParserException {
    public UnexpectedTokenException(char expected, char found) {
        super(String.format("Expected '%c' but found '%c'", expected, found));
    }
}