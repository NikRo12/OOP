package ru.nsu.romanenko.system;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public final class Input {
    public static ArrayList<String> read(String path) {
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
                    throw new IllegalArgumentException(
                            "Line " + lineNumber + " contains invalid characters. Only '0' and '1' are allowed."
                    );
                }

                if (expectedSize == -1) {
                    expectedSize = cleanedLine.length();
                }

                if (cleanedLine.length() != expectedSize) {
                    throw new IllegalArgumentException(
                            "Matrix is not square. Line " + lineNumber + " has length " + cleanedLine.length() +
                                    ", but expected " + expectedSize + "."
                    );
                }

                lines.add(cleanedLine);
            }

            // Если нет ни одной непустой строки, возвращаем пустой список (матрица 0x0)
            if (!hasNonEmptyLines) {
                return new ArrayList<>();
            }

            if (lines.size() != expectedSize) {
                throw new IllegalArgumentException(
                        "Matrix is not square. Rows: " + lines.size() + ", Columns: " + expectedSize
                );
            }

            return lines;

        } catch (IOException e) {
            throw new RuntimeException("Error reading file: " + e.getMessage(), e);
        } catch (IllegalArgumentException e) {
            throw new RuntimeException("Invalid input format: " + e.getMessage(), e);
        }
    }
}