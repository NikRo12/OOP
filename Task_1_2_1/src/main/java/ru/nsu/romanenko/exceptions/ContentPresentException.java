package ru.nsu.romanenko.exceptions;

public class ContentPresentException extends InputExceptions{
    public ContentPresentException(int lineNumber, int length, int expectedSize) {
        super("Matrix is not square. Line " + lineNumber + " has length "
                + length + ", but expected " + expectedSize + ".");
    }

    public ContentPresentException(int size, int expectedSize)
    {
        super("Matrix is not square. Rows: " + size + ", Columns: " + expectedSize);
    }
}
