package ru.nsu.romanenko.System;

public class SimpleLogger implements ru.nsu.romanenko.Abstractions.Logger {
    @Override
    public void log(int id, String state) {
        System.out.println("[ " + id + " ][ " + state + " ]");
    }

    @Override
    public void systemLog(String message) {
        System.out.println("{ " + message + " }");
    }
}
