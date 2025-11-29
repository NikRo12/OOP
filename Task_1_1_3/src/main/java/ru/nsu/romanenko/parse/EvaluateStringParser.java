package ru.nsu.romanenko.parse;

import java.util.HashMap;
import java.util.Map;

public class EvaluateStringParser {
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