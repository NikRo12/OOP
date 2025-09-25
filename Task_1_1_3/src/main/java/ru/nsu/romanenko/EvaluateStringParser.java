package ru.nsu.romanenko;

import java.util.HashMap;
import java.util.Map;

/**
 * Parser for variable assignment strings.
 */
public class EvaluateStringParser {
    /**
     * Parses a string of variable assignments.
     *
     * @param input assignment string (e.g., "x=10;y=20")
     * @return map of variable names to values
     */
    public static Map<String, Integer> parseEvalStr(String input) {
        String str = input.replaceAll("\\s+", "");
        Map<String, Integer> result = new HashMap<>();
        int i = 0;

        while (i < str.length()) {
            StringBuilder var = new StringBuilder();
            StringBuilder value = new StringBuilder();

            while (i < str.length() && str.charAt(i) != '=') {
                var.append(str.charAt(i));
                i++;
            }

            i++;

            while (i < str.length() && str.charAt(i) != ';') {
                value.append(str.charAt(i));
                i++;
            }

            if (i < str.length()) {
                i++;
            }

            if (!var.isEmpty() && !value.isEmpty()) {
                result.put(var.toString(), Integer.parseInt(value.toString()));
            }
        }

        return result;
    }
}