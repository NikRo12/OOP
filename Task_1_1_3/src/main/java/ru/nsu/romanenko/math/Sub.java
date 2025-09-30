package ru.nsu.romanenko.math;

import java.util.Map;

/**
 * Represents a subtraction operation.
 */
public class Sub extends Expression {
    private final Expression exp1;
    private final Expression exp2;

    /**
     * Constructs a subtraction expression.
     *
     * @param exp1 left operand
     * @param exp2 right operand
     */
    public Sub(Expression exp1, Expression exp2) {
        this.exp1 = exp1;
        this.exp2 = exp2;
    }

    @Override
    public void print() {
        System.out.println(this.toString());
    }

    @Override
    public String toString() {
        return '(' + exp1.toString() + " - " + exp2.toString() + ')';
    }

    @Override
    public int evaluate(Map<String, Integer> variables) {
        return exp1.evaluate(variables) - exp2.evaluate(variables);
    }

    @Override
    public Expression derivative(String variable) {
        return new Sub(exp1.derivative(variable), exp2.derivative(variable));
    }
}