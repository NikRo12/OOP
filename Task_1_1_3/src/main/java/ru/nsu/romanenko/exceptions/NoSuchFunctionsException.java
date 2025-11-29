package ru.nsu.romanenko.exceptions;

public class NoSuchFunctionsException extends ExpressionParserException{

    public NoSuchFunctionsException(String message) {
        super(message);
    }
}
