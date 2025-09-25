import java.util.HashMap;
import java.util.Map;

import ru.nsu.romanenko.*;

import org.junit.jupiter.api.Test;
import ru.nsu.romanenko.Number;

import static org.junit.jupiter.api.Assertions.assertEquals;

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
    }

    @Test
    void testVariable() {
        Variable var = new Variable("x");
        assertEquals("x", var.toString());

        Map<String, Integer> vars = new HashMap<>();
        vars.put("x", 10);
        assertEquals(10, var.evaluate(vars));

        assertEquals(1, var.derivative("x").evaluate(new HashMap<>()));
        assertEquals(0, var.derivative("y").evaluate(new HashMap<>()));
    }

    @Test
    void testAdd() {
        Expression add = new Add(new Number(2), new Number(3));
        assertEquals("(2 + 3)", add.toString());
        assertEquals(5, add.evaluate(new HashMap<>()));

        Expression deriv = add.derivative("x");
        assertEquals("(0 + 0)", deriv.toString());
        assertEquals(0, deriv.evaluate(new HashMap<>()));
    }

    @Test
    void testSub() {
        Expression sub = new Sub(new Number(5), new Number(2));
        assertEquals("(5 - 2)", sub.toString());
        assertEquals(3, sub.evaluate(new HashMap<>()));

        Expression deriv = sub.derivative("x");
        assertEquals("(0 - 0)", deriv.toString());
    }

    @Test
    void testMul() {
        Expression mul = new Mul(new Number(4), new Number(3));
        assertEquals("(4 * 3)", mul.toString());
        assertEquals(12, mul.evaluate(new HashMap<>()));

        // Производная от x*2 по x должна быть 2
        Expression mulWithVar = new Mul(new Variable("x"), new Number(2));
        Expression deriv = mulWithVar.derivative("x");
        Map<String, Integer> vars = Map.of("x", 5);
        assertEquals(2, deriv.evaluate(vars)); // (1*2 + x*0) = 2
    }

    @Test
    void testDiv() {
        Expression div = new Div(new Number(6), new Number(2));
        assertEquals("(6 / 2)", div.toString());
        assertEquals(3, div.evaluate(new HashMap<>()));

        // Производная от x/2 по x должна быть 1/2
        Expression divWithVar = new Div(new Variable("x"), new Number(2));
        Expression deriv = divWithVar.derivative("x");
        assertEquals("(((1 * 2) - (x * 0)) / (2 * 2))", deriv.toString());
    }

    @Test
    void testComplexExpression() {
        // (x + 3) * (y - 2)
        Expression expr = new Mul(
                new Add(new Variable("x"), new Number(3)),
                new Sub(new Variable("y"), new Number(2))
        );

        Map<String, Integer> vars = new HashMap<>();
        vars.put("x", 5);
        vars.put("y", 7);

        assertEquals(40, expr.evaluate(vars)); // (5+3)*(7-2) = 8*5 = 40
    }

    @Test
    void testExpressionParser() {
        ExpressionParser parser = new ExpressionParser("(x + (3 * y))");
        Expression expr = parser.parse();
        assertEquals("(x + (3 * y))", expr.toString());

        Map<String, Integer> vars = Map.of("x", 2, "y", 4);
        assertEquals(14, expr.evaluate(vars)); // 2 + (3*4) = 14
    }

    @Test
    void testEvaluateStringParser() {
        String input = "x=10;y=20;z=30";
        Map<String, Integer> result = EvaluateStringParser.parseEvalStr(input);

        assertEquals(3, result.size());
        assertEquals(10, result.get("x"));
        assertEquals(20, result.get("y"));
        assertEquals(30, result.get("z"));
    }

    @Test
    void testDerivativeComplex() {
        // Производная от (x*2 + 3) по x должна быть 2
        Expression expr = new Add(
                new Mul(new Variable("x"), new Number(2)),
                new Number(3)
        );

        Expression deriv = expr.derivative("x");
        Map<String, Integer> vars = Map.of("x", 5);

        // (1*2 + x*0) + 0 = 2
        assertEquals(2, deriv.evaluate(vars));
    }
}