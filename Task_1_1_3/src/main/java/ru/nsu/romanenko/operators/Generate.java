package ru.nsu.romanenko.operators;

import ru.nsu.romanenko.math.*;

import java.util.HashMap;
import java.util.Map;

public class Generate {
    public static Map<Character, Class<? extends Expression>> generate_operators()
    {
        Map<Character, Class<? extends Expression>> result = new HashMap<>();
        result.put('+', Add.class);
        result.put('-', Sub.class);
        result.put('*', Mul.class);
        result.put('/', Div.class);

        return result;
    }
}
