import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.Test;
import ru.nsu.romanenko.*;
import ru.nsu.romanenko.Number;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for expression classes.
 */
class ExpressionTest {

    @Test
    void testNumber() {
        Number num = new Number(5);
        assertEquals("5", num.toString());
        assertEquals(5, num.evaluate(new HashMap<>()));
        assertEquals(0, num.derivative("x").evaluate(new HashMap<>()));

        // Additional cases
        Number negative = new Number(-10);
        assertEquals(-10, negative.evaluate(Map.of("x", 100)));
    }

    @Test
    void testVariable() {
        Variable var = new Variable("x");
        assertEquals("x", var.toString());

        Map<String, Integer> vars = Map.of("x", 10, "y", 20);
        assertEquals(10, var.evaluate(vars));

        assertEquals(1, var.derivative("x").evaluate(new HashMap<>()));
        assertEquals(0, var.derivative("y").evaluate(new HashMap<>()));

        // Missing variable should throw exception
        assertThrows(NullPointerException.class,
                () -> new Variable("z").evaluate(vars));
    }

    @Test
    void testAdd() {
        Expression add = new Add(new Number(2), new Number(3));
        assertEquals("(2 + 3)", add.toString());
        assertEquals(5, add.evaluate(new HashMap<>()));

        Expression deriv = add.derivative("x");
        assertEquals(0, deriv.evaluate(new HashMap<>()));
    }

    @Test
    void testSub() {
        Expression sub = new Sub(new Number(5), new Number(2));
        assertEquals("(5 - 2)", sub.toString());
        assertEquals(3, sub.evaluate(new HashMap<>()));
    }

    @Test
    void testMul() {
        Expression mul = new Mul(new Number(4), new Number(3));
        assertEquals("(4 * 3)", mul.toString());
        assertEquals(12, mul.evaluate(new HashMap<>()));

        // Test derivative with variable
        Expression mulWithVar = new Mul(new Variable("x"), new Number(2));
        Expression deriv = mulWithVar.derivative("x");
        assertEquals(2, deriv.evaluate(Map.of("x", 5)));
    }

    @Test
    void testDiv() {
        Expression div = new Div(new Number(6), new Number(2));
        assertEquals("(6 / 2)", div.toString());
        assertEquals(3, div.evaluate(new HashMap<>()));
    }

    @Test
    void testComplexExpression() {
        Expression expr = new Mul(
                new Add(new Variable("x"), new Number(3)),
                new Sub(new Variable("y"), new Number(2))
        );

        Map<String, Integer> vars = Map.of("x", 5, "y", 7);
        assertEquals(40, expr.evaluate(vars));
    }

    @Test
    void testExpressionParser() {
        ExpressionParser parser = new ExpressionParser("(x + (3 * y))");
        Expression expr = parser.parse();
        assertEquals("(x + (3 * y))", expr.toString());
        assertEquals(14, expr.evaluate(Map.of("x", 2, "y", 4)));

        // Test all operations
        assertEquals(8, new ExpressionParser("(5 + 3)").parse().evaluate(new HashMap<>()));
        assertEquals(2, new ExpressionParser("(5 - 3)").parse().evaluate(new HashMap<>()));
        assertEquals(15, new ExpressionParser("(5 * 3)").parse().evaluate(new HashMap<>()));
        assertEquals(2, new ExpressionParser("(6 / 3)").parse().evaluate(new HashMap<>()));
    }

    @Test
    void testExpressionParserNested() {
        Expression expr = new ExpressionParser("((x + 2) * (y - 1))").parse();
        assertEquals(20, expr.evaluate(Map.of("x", 3, "y", 5)));
    }

    @Test
    void testEvaluateStringParser() {
        Map<String, Integer> result = EvaluateStringParser.parseEvalStr("x=10;y=20;z=30");
        assertEquals(3, result.size());
        assertEquals(10, result.get("x"));
        assertEquals(20, result.get("y"));
        assertEquals(30, result.get("z"));

        // Test with whitespace
        Map<String, Integer> result2 = EvaluateStringParser.parseEvalStr("a = 5; b = 10");
        assertEquals(5, result2.get("a"));
        assertEquals(10, result2.get("b"));
    }

    @Test
    void testDerivativeComplex() {
        Expression expr = new Add(
                new Mul(new Variable("x"), new Number(2)),
                new Number(3)
        );

        Expression deriv = expr.derivative("x");
        assertEquals(2, deriv.evaluate(Map.of("x", 5)));
    }

    @Test
    void testIntegration() {
        // Parse expression and variables, then evaluate
        Expression expr = new ExpressionParser("((x * y) + z)").parse();
        Map<String, Integer> vars = EvaluateStringParser.parseEvalStr("x=2;y=3;z=4");
        assertEquals(10, expr.evaluate(vars));
    }
}