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

        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(inputStream, StandardCharsets.UTF_8))) {

            int codePointPosition = 0;
            String line;

            while ((line = reader.readLine()) != null) {

                int[] lineCodePoints = line.codePoints().toArray();
                int[] subCodePoints = substring.codePoints().toArray();

                if (subCodePoints.length == 0) {
                    continue;
                }

                for (int i = 0; i <= lineCodePoints.length - subCodePoints.length; i++) {
                    boolean found = true;
                    for (int j = 0; j < subCodePoints.length; j++) {
                        if (lineCodePoints[i + j] != subCodePoints[j]) {
                            found = false;
                            break;
                        }
                    }
                    if (found) {
                        result.add(codePointPosition + i);
                        i += subCodePoints.length - 1;
                    }
                }

                codePointPosition += lineCodePoints.length + 1;
            }

        }

        return result;
    }
}