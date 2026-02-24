package ru.nsu.romanenko.Abstractions;

public interface Storage<T> {
    void put(T item) throws InterruptedException;
    T get() throws InterruptedException;
}
