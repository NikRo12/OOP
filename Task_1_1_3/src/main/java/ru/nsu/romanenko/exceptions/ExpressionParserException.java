package ru.nsu.romanenko.exceptions;

public abstract class ExpressionParserException extends Exception {
    public ExpressionParserException(String message) {
        super(message);
    }
}
