package ru.nsu.romanenko.exceptions;
public class DivisionByZeroException extends Exception{
    public DivisionByZeroException(String object)
    {
        super("Division by zero in expression: " + object);
    }
}
