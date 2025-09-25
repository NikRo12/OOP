package ru.nsu.romanenko;

import java.util.Map;

/**
 * Represents a division operation.
 */
public class Div extends Expression {
    private final Expression exp1;
    private final Expression exp2;

    /**
     * Constructs a division expression.
     *
     * @param exp1 numerator
     * @param exp2 denominator
     */
    public Div(Expression exp1, Expression exp2) {
        this.exp1 = exp1;
        this.exp2 = exp2;
    }

    @Override
    public void print() {
        System.out.println(this.toString());
    }

    @Override
    public String toString() {
        return '(' + exp1.toString() + " / " + exp2.toString() + ')';
    }

    @Override
    public int evaluate(Map<String, Integer> variables) {
        return exp1.evaluate(variables) / exp2.evaluate(variables);
    }

    @Override
    public Expression derivative(String variable) {
        return new Div(new Sub(new Mul(exp1.derivative(variable), exp2),
                new Mul(exp1, exp2.derivative(variable))), new Mul(exp2, exp2));
    }
}