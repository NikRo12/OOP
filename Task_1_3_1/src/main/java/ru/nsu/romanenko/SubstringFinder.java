package ru.nsu.romanenko;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

public class SubstringFinder {


    public static ArrayList<Integer> find(String file, String substring) {
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

    public static ArrayList<Integer> find(InputStream inputStream, String substring)
            throws IOException {
        ArrayList<Integer> result = new ArrayList<>();

        if (substring.isEmpty()) {
            return result;
        }

        int[] subCodePoints = substring.codePoints().toArray();

        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(inputStream, StandardCharsets.UTF_8))) {

            int codePointPosition = 0;
            String line;

            while ((line = reader.readLine()) != null) {
                int lineLength = line.length();
                int lineCodePointCount = line.codePointCount(0, lineLength);

                if (lineCodePointCount < subCodePoints.length) {
                    codePointPosition += lineCodePointCount + 1;
                    continue;
                }

                for (int i = 0; i <= lineCodePointCount - subCodePoints.length; i++) {
                    boolean found = true;

                    int lineIndex = line.offsetByCodePoints(0, i);
                    for (int j = 0; j < subCodePoints.length; j++) {
                        int currentLineCodePoint = line.codePointAt(lineIndex);
                        if (currentLineCodePoint != subCodePoints[j]) {
                            found = false;
                            break;
                        }
                        lineIndex = line.offsetByCodePoints(lineIndex, 1);
                    }

                    if (found) {
                        result.add(codePointPosition + i);
                        i += subCodePoints.length - 1;
                    }
                }

                codePointPosition += lineCodePointCount + 1;
            }
        }

        return result;
    }
}