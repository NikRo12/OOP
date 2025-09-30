import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import org.junit.jupiter.api.Test;
import ru.nsu.romanenko.math.*;
import ru.nsu.romanenko.math.Number;
import ru.nsu.romanenko.parse.EvaluateStringParser;
import ru.nsu.romanenko.parse.ExpressionParser;

/**
 * Unit tests for expression classes.
 */
class ExpressionTests {

    @Test
    void testNumber() {
        Number num = new Number(5);
        assertEquals("5", num.toString());
        assertEquals(5, num.evaluate(new HashMap<>()));
        assertEquals(0, num.derivative("x").evaluate(new HashMap<>()));

        // Additional cases for better coverage
        Number negative = new Number(-10);
        assertEquals("-10", negative.toString());
        assertEquals(-10, negative.evaluate(Map.of("x", 100)));

        Number zero = new Number(0);
        assertEquals("0", zero.toString());
        assertEquals(0, zero.evaluate(new HashMap<>()));
    }

    @Test
    void testVariable() {
        Variable var = new Variable("x");
        assertEquals("x", var.toString());

        Map<String, Integer> vars = Map.of("x", 10, "y", 20);
        assertEquals(10, var.evaluate(vars));

        assertEquals(1, var.derivative("x").evaluate(new HashMap<>()));
        assertEquals(0, var.derivative("y").evaluate(new HashMap<>()));

        assertThrows(NullPointerException.class,
                () -> new Variable("z").evaluate(vars));

        assertEquals("x", var.getName());
    }

    @Test
    void testAdd() {
        Expression add = new Add(new Number(2), new Number(3));
        assertEquals("(2 + 3)", add.toString());
        assertEquals(5, add.evaluate(new HashMap<>()));

        Expression deriv = add.derivative("x");
        assertEquals("(0 + 0)", deriv.toString());
        assertEquals(0, deriv.evaluate(new HashMap<>()));

        Expression addWithVars = new Add(new Variable("x"), new Variable("y"));
        assertEquals(15, addWithVars.evaluate(Map.of("x", 5, "y", 10)));
    }

    @Test
    void testSub() {
        Expression sub = new Sub(new Number(5), new Number(2));
        assertEquals("(5 - 2)", sub.toString());
        assertEquals(3, sub.evaluate(new HashMap<>()));

        Expression deriv = sub.derivative("x");
        assertEquals("(0 - 0)", deriv.toString());
        assertEquals(0, deriv.evaluate(new HashMap<>()));

        Expression subWithVars = new Sub(new Variable("x"), new Variable("y"));
        assertEquals(2, subWithVars.evaluate(Map.of("x", 7, "y", 5)));
    }

    @Test
    void testMul() {
        Expression mul = new Mul(new Number(4), new Number(3));
        assertEquals("(4 * 3)", mul.toString());
        assertEquals(12, mul.evaluate(new HashMap<>()));

        Expression mulWithVar = new Mul(new Variable("x"), new Number(2));
        Expression deriv = mulWithVar.derivative("x");
        assertEquals("((1 * 2) + (x * 0))", deriv.toString());
        assertEquals(2, deriv.evaluate(Map.of("x", 5)));

        Expression mulTwoVars = new Mul(new Variable("x"), new Variable("y"));
        Expression deriv2 = mulTwoVars.derivative("x");
        assertEquals(3, deriv2.evaluate(Map.of("x", 2, "y", 3)));
    }

    @Test
    void testDiv() {
        Expression div = new Div(new Number(6), new Number(2));
        assertEquals("(6 / 2)", div.toString());
        assertEquals(3, div.evaluate(new HashMap<>()));

        Expression divWithVar = new Div(new Variable("x"), new Number(2));
        Expression deriv = divWithVar.derivative("x");
        assertEquals("(((1 * 2) - (x * 0)) / (2 * 2))", deriv.toString());
        assertEquals(0, deriv.evaluate(Map.of("x", 5)));

        Expression divVars = new Div(new Variable("x"), new Variable("y"));
        assertEquals(2, divVars.evaluate(Map.of("x", 6, "y", 3)));
    }

    @Test
    void testComplexExpression() {
        Expression expr = new Mul(
                new Add(new Variable("x"), new Number(3)),
                new Sub(new Variable("y"), new Number(2))
        );

        Map<String, Integer> vars = Map.of("x", 5, "y", 7);
        assertEquals(40, expr.evaluate(vars));

        Expression deriv = expr.derivative("x");
        assertEquals(5, deriv.evaluate(Map.of("x", 2, "y", 7)));
    }

    @Test
    void testExpressionParser() {
        ExpressionParser parser = new ExpressionParser("(x + (3 * y))");
        Expression expr = parser.parse();
        assertEquals("(x + (3 * y))", expr.toString());
        assertEquals(14, expr.evaluate(Map.of("x", 2, "y", 4)));

        assertEquals(8, new ExpressionParser("(5 + 3)").parse().evaluate(new HashMap<>()));
        assertEquals(2, new ExpressionParser("(5 - 3)").parse().evaluate(new HashMap<>()));
        assertEquals(15, new ExpressionParser("(5 * 3)").parse().evaluate(new HashMap<>()));
        assertEquals(2, new ExpressionParser("(6 / 3)").parse().evaluate(new HashMap<>()));

        assertEquals(105, new ExpressionParser("(100 + 5)").parse().evaluate(
                new HashMap<>()));
    }

    @Test
    void testExpressionParserNested() {
        Expression expr = new ExpressionParser("((x + 2) * (y - 1))").parse();
        assertEquals(20, expr.evaluate(Map.of("x", 3, "y", 5)));
    }

    @Test
    void testExpressionParserEdgeCases() {
        Expression expr1 = new ExpressionParser("( x +  y )").parse();
        assertEquals(5, expr1.evaluate(Map.of("x", 2, "y", 3)));
    }

    @Test
    void testExpressionParserErrorCases() {
        assertThrows(RuntimeException.class, () -> new ExpressionParser("").parse());
        assertThrows(RuntimeException.class, () -> new ExpressionParser("x + y").parse());
        assertThrows(RuntimeException.class, () -> new ExpressionParser("(x & y)").parse());
    }

    @Test
    void testEvaluateStringParser() {
        Map<String, Integer> result = EvaluateStringParser.parseEvalStr("x=10;y=20;z=30");
        assertEquals(3, result.size());
        assertEquals(10, result.get("x"));
        assertEquals(20, result.get("y"));
        assertEquals(30, result.get("z"));

        Map<String, Integer> result2 = EvaluateStringParser.parseEvalStr("a = 5; b = 10");
        assertEquals(5, result2.get("a"));
        assertEquals(10, result2.get("b"));

        Map<String, Integer> result3 = EvaluateStringParser.parseEvalStr("x=42");
        assertEquals(1, result3.size());
        assertEquals(42, result3.get("x"));

        Map<String, Integer> result4 = EvaluateStringParser.parseEvalStr("x=-10;y=-20");
        assertEquals(-10, result4.get("x"));
        assertEquals(-20, result4.get("y"));
    }

    @Test
    void testDerivativeComplex() {
        Expression e = new Add(new Number(3), new Mul(new Number(2),
                new Variable("x")));

        Expression de = e.derivative("x");
        assertEquals("(0 + ((0 * x) + (2 * 1)))", de.toString());
    }

    @Test
    void testPrintMethods() {
        Number num = new Number(5);
        assertDoesNotThrow(num::print);

        Variable var = new Variable("x");
        assertDoesNotThrow(var::print);

        Expression add = new Add(new Number(1), new Number(2));
        assertDoesNotThrow(add::print);

        Expression sub = new Sub(new Number(5), new Number(3));
        assertDoesNotThrow(sub::print);

        Expression mul = new Mul(new Number(2), new Number(3));
        assertDoesNotThrow(mul::print);

        Expression div = new Div(new Number(6), new Number(2));
        assertDoesNotThrow(div::print);
    }

    @Test
    void testIntegration() {
        Expression expr = new ExpressionParser("((x * y) + z)").parse();
        Map<String, Integer> vars = EvaluateStringParser.parseEvalStr("x=2;y=3;z=4");
        assertEquals(10, expr.evaluate(vars));

        Expression complexExpr = new ExpressionParser("((x * x) + (3 * x))").parse();
        Expression deriv = complexExpr.derivative("x");
        assertEquals(7, deriv.evaluate(Map.of("x", 2)));
    }
}