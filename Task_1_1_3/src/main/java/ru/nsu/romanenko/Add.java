package ru.nsu.romanenko;

import java.util.Map;

public class Add extends Expression
{

    private final Expression exp1;
    private final Expression exp2;

    public Add(Expression exp1, Expression epx2)
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
        return '(' + exp1.toString() + " + " + exp2.toString() + ')';
    }

    @Override
    public int evaluate(Map<String, Integer> variables) {
        return exp1.evaluate(variables) + exp2.evaluate(variables);
    }

    @Override
    public Expression derivative(String variable) {
        return new Add(exp1.derivative(variable), exp2.derivative(variable));
    }
}