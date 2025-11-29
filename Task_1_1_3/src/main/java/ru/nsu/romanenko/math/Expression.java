package ru.nsu.romanenko.math;

import ru.nsu.romanenko.exceptions.DivisionByZeroException;

import java.util.Map;


public abstract class Expression {
    public abstract void print();

    public abstract int evaluate(Map<String, Integer> variables) throws DivisionByZeroException;

    public abstract Expression derivative(String variable);
}