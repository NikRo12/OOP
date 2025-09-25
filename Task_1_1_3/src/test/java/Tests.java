import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.Test;
import ru.nsu.romanenko.*;
import ru.nsu.romanenko.Number;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ExpressionTest {

    @Test
    void testNumberAndVariable() {
        Number num = new Number(5);
        assertEquals("5", num.toString());
        assertEquals(5, num.evaluate(new HashMap<>()));
        assertEquals(0, num.derivative("x").evaluate(new HashMap<>()));

        Variable var = new Variable("x");
        assertEquals("x", var.toString());

        Map<String, Integer> vars = Map.of("x", 10);
        assertEquals(10, var.evaluate(vars));
        assertEquals(1, var.derivative("x").evaluate(new HashMap<>()));
        assertEquals(0, var.derivative("y").evaluate(new HashMap<>()));
    }

    @Test
    void testBasicOperations() {
        assertEquals(5, new Add(new Number(2), new Number(3)).evaluate(new HashMap<>()));
        assertEquals(3, new Sub(new Number(5), new Number(2)).evaluate(new HashMap<>()));
        assertEquals(12, new Mul(new Number(4), new Number(3)).evaluate(new HashMap<>()));
        assertEquals(3, new Div(new Number(6), new Number(2)).evaluate(new HashMap<>()));

        // Производная от x*2 по x = 2
        Expression mulWithVar = new Mul(new Variable("x"), new Number(2));
        assertEquals(2, mulWithVar.derivative("x").evaluate(Map.of("x", 5)));

        // Производная от x/2 по x = 1/2
        Expression divWithVar = new Div(new Variable("x"), new Number(2));
        assertEquals("(((1 * 2) - (x * 0)) / (2 * 2))", divWithVar.derivative("x").toString());
    }

    @Test
    void testComplexExpressionAndParser() {
        Expression expr = new Mul(
                new Add(new Variable("x"), new Number(3)),
                new Sub(new Variable("y"), new Number(2))
        );
        assertEquals(40, expr.evaluate(Map.of("x", 5, "y", 7)));

        // Parser: (x + (3 * y))
        Expression parsed = new ExpressionParser("(x + (3 * y))").parse();
        assertEquals(14, parsed.evaluate(Map.of("x", 2, "y", 4)));
    }

    @Test
    void testEvaluateStringParserAndDerivative() {
        Map<String, Integer> result = EvaluateStringParser.parseEvalStr("x=10;y=20;z=30");
        assertEquals(10, result.get("x"));
        assertEquals(20, result.get("y"));
        assertEquals(30, result.get("z"));

        Expression expr = new Add(new Mul(new Variable("x"), new Number(2)), new Number(3));
        assertEquals(2, expr.derivative("x").evaluate(Map.of("x", 5)));
    }
}
