package ru.nsu.romanenko;

import java.util.Map;

public class Div extends Expression{

    private final Expression exp1;
    private final Expression exp2;

    public Div(Expression exp1, Expression epx2)
    {
        this.exp1 = exp1;
        this.exp2 = epx2;
    }

    @Override
    public void print()
    {
        System.out.println(this.toString());
    }

    @Override
    public String toString() {
        return '(' + exp1.toString() + " / " + exp2.toString() + ')';
    }

    @Override
    public int evaluate(Map<String, Integer> variable) {
        return exp1.evaluate(variable) / exp2.evaluate(variable);
    }

    @Override
    public Expression derivative(String variable)
    {
        return new Div(
                new Sub(
                        new Mul(exp1.derivative(variable), exp2),
                        new Mul(exp1, exp2.derivative(variable))
                ),
                new Mul(exp2, exp2) // g²
        );
    }
}
