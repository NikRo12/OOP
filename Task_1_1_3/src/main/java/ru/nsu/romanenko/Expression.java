package ru.nsu.romanenko;

import java.util.Map;

/**
 * Abstract class representing a mathematical expression.
 */
public abstract class Expression {
    /**
     * Returns string representation of the expression.
     *
     * @return string representation
     */
    public abstract String toString();

    /**
     * Prints the expression to standard output.
     */
    public abstract void print();

    /**
     * Evaluates the expression with given variables.
     *
     * @param variables map of variable names to values
     * @return result of evaluation
     */
    public abstract int evaluate(Map<String, Integer> variables);

    /**
     * Computes derivative of the expression with respect to a variable.
     *
     * @param variable variable to differentiate by
     * @return derivative expression
     */
    public abstract Expression derivative(String variable);
}