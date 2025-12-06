package ru.nsu.romanenko.exceptions;

public class ContentValueException extends InputExceptions {
    public ContentValueException(int lineNumber) {
        super("Line " + lineNumber + " contains invalid characters. Only '0' and '1' are allowed.");
    }
}
