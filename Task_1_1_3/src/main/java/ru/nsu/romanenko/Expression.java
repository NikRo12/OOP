package ru.nsu.romanenko;

import java.util.Map;

public abstract class Expression
{
    public abstract String toString();

    public abstract void print();

    public abstract int evaluate(Map<String, Integer> variables);

    public abstract Expression derivative(String variable);
}
