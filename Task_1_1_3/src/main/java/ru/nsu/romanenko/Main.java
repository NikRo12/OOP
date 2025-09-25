package ru.nsu.romanenko;

public class Main {
    public static void main(String[] args) {
        Expression e = ExpressionParser.getResult();
        e.print();

        Expression de = e.derivative("x");
        de.print();

        int result = e.evaluate(EvaluateStringParser.parseEvalStr("x = 10; y = 13"));
        System.out.println(result);
    }
}