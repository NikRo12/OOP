package ru.nsu.romanenko.math;

import ru.nsu.romanenko.input_output.Output;

import java.util.Map;

public class Number extends Expression {
    private final int value;
    public Number(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    @Override
    public void print() {
        Output.print_Expression(this.toString());
    }

    @Override
    public String toString() {
        return String.valueOf(value);
    }

    @Override
    public int evaluate(Map<String, Integer> variables) {
        return value;
    }

    @Override
    public Expression derivative(String variable) {
        return new Number(0);
    }
}