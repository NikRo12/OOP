package ru.nsu.romanenko.Abstractions;

public interface Logger {
    void log(int id, String state);
    void systemLog(String message);
}
