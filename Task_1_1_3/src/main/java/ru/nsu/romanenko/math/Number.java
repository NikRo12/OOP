package ru.nsu.romanenko.math;

import java.util.Map;

/**
 * Represents a constant number in an expression.
 */
public class Number extends Expression {
    private final int value;

    /**
     * Constructs a number with given value.
     *
     * @param value integer value
     */
    public Number(int value) {
        this.value = value;
    }

    /**
     * Returns the numeric value.
     *
     * @return the value
     */
    public int getValue() {
        return value;
    }

    @Override
    public void print() {
        System.out.println(this.toString());
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