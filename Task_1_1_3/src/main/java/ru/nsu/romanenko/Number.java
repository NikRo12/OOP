package ru.nsu.romanenko;
import java.util.Map;

public class Number extends Expression{
    private final int value;

    public Number(int value)
    {
        this.value = value;
    }

    public int getValue()
    {
        return value;
    }

    @Override
    public void print()
    {
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
