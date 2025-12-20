package ru.nsu.romanenko;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

public class SubstringFinder {

    public static ArrayList<Long> find(String file, String substring) {
        if (substring == null || substring.isEmpty()) {
            return new ArrayList<>();
        }

        try (InputStream inputStream = SubstringFinder.class.getClassLoader()
                .getResourceAsStream(file)) {

            if (inputStream == null) {
                System.err.println("File not found: " + file);
                return new ArrayList<>();
            }

            return find(inputStream, substring);
        } catch (IOException ex) {
            System.err.println("Error reading file: " + ex.getMessage());
            return new ArrayList<>();
        }
    }

    public static ArrayList<Long> find(InputStream inputStream, String substring) throws IOException {
        ArrayList<Long> result = new ArrayList<>();

        if (substring == null || substring.isEmpty()) {
            return result;
        }

        int[] pattern = substring.codePoints().toArray();

        int[] prefixTable = computeFailureFunction(pattern);

        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(inputStream, StandardCharsets.UTF_8))) {

            int matchIndex = 0;
            long globalIndex = 0;

            int highSurrogate = -1;

            int charVal;
            while ((charVal = reader.read()) != -1) {
                char ch = (char) charVal;
                int currentCodePoint;

                if (Character.isHighSurrogate(ch)) {
                    highSurrogate = charVal;
                    continue;
                } else if (Character.isLowSurrogate(ch)) {
                    if (highSurrogate != -1) {
                        currentCodePoint = Character.toCodePoint((char) highSurrogate, ch);
                        highSurrogate = -1;
                    } else {
                        currentCodePoint = charVal;
                    }
                } else {
                    if (highSurrogate != -1) {
                        highSurrogate = -1;
                    }
                    currentCodePoint = charVal;
                }

                while (matchIndex > 0 && currentCodePoint != pattern[matchIndex]) {
                    matchIndex = prefixTable[matchIndex - 1];
                }

                if (currentCodePoint == pattern[matchIndex]) {
                    matchIndex++;
                }

                if (matchIndex == pattern.length) {
                    result.add(globalIndex - pattern.length + 1);
                    matchIndex = prefixTable[matchIndex - 1];
                    //return result;
                }

                globalIndex++;
            }
        }

        return result;
    }

    private static int[] computeFailureFunction(int[] pattern) {
        int[] table = new int[pattern.length];
        int k = 0;
        for (int q = 1; q < pattern.length; q++) {
            while (k > 0 && pattern[k] != pattern[q]) {
                k = table[k - 1];
            }
            if (pattern[k] == pattern[q]) {
                k++;
            }
            table[q] = k;
        }
        return table;
    }
}