package ru.nsu.romanenko.math;

import ru.nsu.romanenko.exceptions.DivisionByZeroException;
import ru.nsu.romanenko.input_output.Output;

import java.util.Map;
import java.util.Objects;

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
        Output.print_Expression(this.toString());
    }

    @Override
    public String toString() {
        return '(' + exp1.toString() + " / " + exp2.toString() + ')';
    }

    @Override
    public int evaluate(Map<String, Integer> variables) throws DivisionByZeroException {
        int denominator = exp2.evaluate(variables);
        if (denominator == 0) {
            throw new DivisionByZeroException(this.toString());
        }
        return exp1.evaluate(variables) / denominator;
    }

    @Override
    public Expression derivative(String variable) {
        return new Div(new Sub(new Mul(exp1.derivative(variable), exp2),
                new Mul(exp1, exp2.derivative(variable))), new Mul(exp2, exp2));
    }
}