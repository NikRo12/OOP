package ru.nsu.romanenko;

import ru.nsu.romanenko.math.Expression;
import ru.nsu.romanenko.parse.EvaluateStringParser;
import ru.nsu.romanenko.parse.ExpressionParser;

/**
 * Main class for expression evaluation demonstration.
 */
public class Main {
    /**
     * Main method.
     *
     * @param args command line arguments
     */
    public static void main(String[] args) {
        Expression e = ExpressionParser.getResult();
        e.print();

        Expression de = e.derivative("x");
        de.print();

        int result = e.evaluate(EvaluateStringParser.parseEvalStr("x = 10; y = 13"));
        System.out.println(result);
    }
}