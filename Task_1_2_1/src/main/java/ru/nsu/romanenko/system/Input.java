package ru.nsu.romanenko.system;

import ru.nsu.romanenko.exceptions.ContentPresentException;
import ru.nsu.romanenko.exceptions.ContentValueException;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public final class Input {
    public static ArrayList<String> read(String path){
        try (BufferedReader buffer = new BufferedReader(new FileReader(path))) {
            ArrayList<String> lines = new ArrayList<>();
            String line;
            int expectedSize = -1;
            int lineNumber = 0;
            boolean hasNonEmptyLines = false;

            while ((line = buffer.readLine()) != null) {
                lineNumber++;
                String cleanedLine = line.replace(" ", "").trim();

                if (cleanedLine.isEmpty()) {
                    continue;
                }

                hasNonEmptyLines = true;

                if (!cleanedLine.matches("[01]+")) {
                    throw new ContentValueException(lineNumber);
                }

                if (expectedSize == -1) {
                    expectedSize = cleanedLine.length();
                }

                if (cleanedLine.length() != expectedSize) {
                    throw new ContentPresentException(lineNumber, cleanedLine.length(), expectedSize);
                }

                lines.add(cleanedLine);
            }

            if (!hasNonEmptyLines) {
                return new ArrayList<>();
            }

            if (lines.size() != expectedSize) {
                throw new ContentPresentException(lines.size(), expectedSize);
            }

            return lines;

        } catch (IOException e) {
            throw new RuntimeException("System error reading file: " + e.getMessage(), e);
        } catch (ContentValueException | ContentPresentException e) {
            System.out.println(e.getMessage());
        }

        return null;
    }
}