package ru.nsu.romanenko.math;

import java.util.Map;

/**
 * Represents a variable in an expression.
 */
public class Variable extends Expression {
    private final String name;

    /**
     * Constructs a variable with given name.
     *
     * @param name variable name
     */
    public Variable(String name) {
        this.name = name;
    }

    /**
     * Returns the variable name.
     *
     * @return variable name
     */
    public String getName() {
        return name;
    }

    @Override
    public void print() {
        System.out.println(this.toString());
    }

    @Override
    public String toString() {
        return name;
    }

    @Override
    public int evaluate(Map<String, Integer> variables) {
        return variables.get(name);
    }

    @Override
    public Expression derivative(String variable) {
        if (variable.equals(name)) {
            return new Number(1);
        } else {
            return new Number(0);
        }
    }
}