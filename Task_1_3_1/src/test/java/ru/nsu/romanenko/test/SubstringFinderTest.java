package ru.nsu.romanenko.test;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import ru.nsu.romanenko.SubstringFinder;

import java.io.*;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertEquals;

class SubstringFinderTest {

    @TempDir
    Path tempDir;

    @Test
    void testFindInMultipleLines() throws IOException {
        File testFile = tempDir.resolve("test.txt").toFile();

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(testFile))) {
            writer.write("Hello, world!\n");
            writer.write("This is a test file.\n");
            writer.write("Hello again!\n");
        }

        try (InputStream inputStream = new FileInputStream(testFile)) {
            ArrayList<Integer> result = SubstringFinder.find(inputStream, "Hello");
            assertEquals(Arrays.asList(0, 35), result);
        }
    }

    @Test
    void testFindOverlapping() throws IOException {
        File testFile = tempDir.resolve("test2.txt").toFile();

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(testFile))) {
            writer.write("abcabcabc\n");
            writer.write("abc\n");
        }

        try (InputStream inputStream = new FileInputStream(testFile)) {
            ArrayList<Integer> result = SubstringFinder.find(inputStream, "abc");
            assertEquals(Arrays.asList(0, 3, 6, 10), result);
        }
    }

    @Test
    void testEmptySubstring() throws IOException {
        File testFile = tempDir.resolve("test3.txt").toFile();

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(testFile))) {
            writer.write("Hello, world!\n");
        }

        try (InputStream inputStream = new FileInputStream(testFile)) {
            ArrayList<Integer> result = SubstringFinder.find(inputStream, "");
            assertEquals(new ArrayList<>(), result);
        }
    }

    @Test
    void testEmptyFile() throws IOException {
        File testFile = tempDir.resolve("empty.txt").toFile();

        boolean fileCreated = testFile.createNewFile();

        System.out.println("Testing with file: " + testFile.getAbsolutePath());

        try (InputStream inputStream = new FileInputStream(testFile)) {
            ArrayList<Integer> result = SubstringFinder.find(inputStream, "abc");
            assertEquals(new ArrayList<>(), result);
        }
    }

    @Test
    void testWithDifferentEncodings() throws IOException {
        File testFile = tempDir.resolve("unicode.txt").toFile();

        try (OutputStreamWriter writer = new OutputStreamWriter(
                new FileOutputStream(testFile), "UTF-8")) {
            writer.write("Привет, мир!\n");
            writer.write("Это тестовый файл.\n");
        }

        try (InputStream inputStream = new FileInputStream(testFile)) {
            ArrayList<Integer> result = SubstringFinder.find(inputStream, "тест");
            assertEquals(Arrays.asList(17), result);
        }
    }

    @Test
    void testLargeFile() throws IOException {
        File testFile = tempDir.resolve("large_file.txt").toFile();

        int fileSizeMB = 5;
        int chunkSize = 1024 * 1024;
        String pattern = "abc123XYZ";
        String searchString = "123";

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(testFile))) {
            StringBuilder largeContent = new StringBuilder();

            while (largeContent.length() < chunkSize) {
                largeContent.append(pattern).append("\n");
            }
            String chunk = largeContent.toString();

            for (int i = 0; i < fileSizeMB; i++) {
                writer.write(chunk);
                writer.flush();
            }
        }

        long expectedCount = countExpectedOccurrences(testFile, searchString);

        System.out.println("Testing large file: " + testFile.length() + " bytes");
        System.out.println("Expected occurrences: " + expectedCount);

        try (InputStream inputStream = new FileInputStream(testFile)) {
            long startTime = System.currentTimeMillis();
            ArrayList<Integer> result = SubstringFinder.find(inputStream, searchString);
            long endTime = System.currentTimeMillis();

            System.out.println("Found occurrences: " + result.size());
            System.out.println("Execution time: " + (endTime - startTime) + " ms");

            assertEquals(expectedCount, result.size());

            if (!result.isEmpty()) {
                int firstExpectedPos = getFirstOccurrencePosition(pattern, searchString);
                assertEquals(firstExpectedPos, result.get(0).intValue());
            }
        }
    }

    private long countExpectedOccurrences(File file, String searchString) throws IOException {
        long count = 0;
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                int index = 0;
                while ((index = line.indexOf(searchString, index)) != -1) {
                    count++;
                    index += searchString.length();
                }
            }
        }
        return count;
    }

    private int getFirstOccurrencePosition(String pattern, String searchString) {
        return pattern.indexOf(searchString);
    }
}