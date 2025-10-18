package ru.nsu.romanenko;

import ru.nsu.romanenko.exceptions.DivisionByZeroException;
import ru.nsu.romanenko.exceptions.ExpressionParserException;
import ru.nsu.romanenko.math.Expression;
import ru.nsu.romanenko.parse.EvaluateStringParser;
import ru.nsu.romanenko.parse.ExpressionParser;

import java.lang.reflect.InvocationTargetException;

public class Main {
    public static void main(String[] args) throws ExpressionParserException, DivisionByZeroException {
        try
        {
            Expression e = ExpressionParser.getExpressionObjectFromInput();
            e.print();

            Expression de = e.derivative("x");
            de.print();

            int result = e.evaluate(EvaluateStringParser.parseEvalStr("x = 10"));
            System.out.println(result);
        }

        catch (ExpressionParserException | DivisionByZeroException ex)
        {
            System.out.println(ex.getMessage());
        }
    }
}