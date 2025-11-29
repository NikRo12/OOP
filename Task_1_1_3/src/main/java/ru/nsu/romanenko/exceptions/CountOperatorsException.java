package ru.nsu.romanenko.exceptions;

public class CountOperatorsException extends ExpressionParserException {
    public CountOperatorsException() {
        super("Not enough operators in expression");
    }
}
