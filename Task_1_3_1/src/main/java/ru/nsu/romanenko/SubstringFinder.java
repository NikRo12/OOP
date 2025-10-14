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

    public static ArrayList<Integer> find(InputStream inputStream, String substring) {
        if (substring == null || substring.isEmpty()) {
            return new ArrayList<>();
        }

        ArrayList<Integer> result = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(inputStream, StandardCharsets.UTF_8))) {

            int globalPosition = 0;
            String line;

            while ((line = reader.readLine()) != null) {
                int index = 0;
                while ((index = line.indexOf(substring, index)) != -1) {
                    result.add(globalPosition + index);
                    index += 1;
                }
                globalPosition += line.length() + 1;
            }

            return result;
        } catch (IOException ex) {
            System.err.println("Error reading from input stream: " + ex.getMessage());
            return new ArrayList<>();
        }
    }
}