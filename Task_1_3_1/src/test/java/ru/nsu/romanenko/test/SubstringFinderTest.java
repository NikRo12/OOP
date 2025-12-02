package ru.nsu.romanenko.test;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import ru.nsu.romanenko.SubstringFinder;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

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
    void testEmptySubstring() throws IOException {
        File testFile = tempDir.resolve("test3.txt").toFile();

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(testFile))) {
            writer.write("Hello, world!\r\n");
        }

        try (InputStream inputStream = new FileInputStream(testFile)) {
            ArrayList<Integer> result = SubstringFinder.find(inputStream, "");
            assertEquals(new ArrayList<>(), result);
        }
    }

    @Test
    void testEmptyFile() throws IOException {
        File testFile = tempDir.resolve("empty.txt").toFile();
        testFile.createNewFile();

        try (InputStream inputStream = new FileInputStream(testFile)) {
            ArrayList<Integer> result = SubstringFinder.find(inputStream, "abc");
            assertEquals(new ArrayList<>(), result);
        }
    }

    @Test
    void testSubstringAtBeginning() throws IOException {
        File testFile = tempDir.resolve("beginning.txt").toFile();

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(testFile))) {
            writer.write("start of the content\r\n");
            writer.write("more content here\r\n");
        }

        try (InputStream inputStream = new FileInputStream(testFile)) {
            ArrayList<Integer> result = SubstringFinder.find(inputStream, "start");
            assertEquals(List.of(0), result);
        }
    }

    @Test
    void testSubstringAtEnd() throws IOException {
        File testFile = tempDir.resolve("end.txt").toFile();

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(testFile))) {
            writer.write("This is the content\n");
            writer.write("this is the end");
        }

        try (InputStream inputStream = new FileInputStream(testFile)) {
            ArrayList<Integer> result = SubstringFinder.find(inputStream, "end");
            assertEquals(List.of(32), result);
        }
    }

    @Test
    void testSubstringNotFound() throws IOException {
        File testFile = tempDir.resolve("notfound.txt").toFile();

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(testFile))) {
            writer.write("This is some text\r\n");
            writer.write("with no matching pattern\r\n");
        }

        try (InputStream inputStream = new FileInputStream(testFile)) {
            ArrayList<Integer> result = SubstringFinder.find(inputStream, "nonexistent");
            assertEquals(new ArrayList<>(), result);
        }
    }

    @Test
    void testCaseSensitive() throws IOException {
        File testFile = tempDir.resolve("case.txt").toFile();

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(testFile))) {
            writer.write("Case CASE case\r\n");
            writer.write("TEST test Test\r\n");
        }

        try (InputStream inputStream = new FileInputStream(testFile)) {
            ArrayList<Integer> result = SubstringFinder.find(inputStream, "CASE");
            assertEquals(List.of(5), result);
        }
    }

    @Test
    void testSpecialCharacters() throws IOException {
        File testFile = tempDir.resolve("special.txt").toFile();

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(testFile))) {
            writer.write("Line with tabs\tand\ttabs\r\n");
        }

        try (InputStream inputStream = new FileInputStream(testFile)) {
            ArrayList<Integer> result = SubstringFinder.find(inputStream, "\t");
            assertEquals(Arrays.asList(14, 18), result);
        }
    }

    @Test
    void testSingleLineNoNewline() throws IOException {
        File testFile = tempDir.resolve("single.txt").toFile();

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(testFile))) {
            writer.write("test");
        }

        try (InputStream inputStream = new FileInputStream(testFile)) {
            ArrayList<Integer> result = SubstringFinder.find(inputStream, "test");
            assertEquals(List.of(0), result);
        }
    }

    @Test
    void testMultipleSameLine() throws IOException {
        File testFile = tempDir.resolve("multiple.txt").toFile();

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(testFile))) {
            writer.write("test test test");
        }

        try (InputStream inputStream = new FileInputStream(testFile)) {
            ArrayList<Integer> result = SubstringFinder.find(inputStream, "test");
            assertEquals(Arrays.asList(0, 5, 10), result);
        }
    }

    @Test
    void testWithDifferentEncodings() throws IOException {
        File testFile = tempDir.resolve("unicode.txt").toFile();

        try (OutputStreamWriter writer = new OutputStreamWriter(
                new FileOutputStream(testFile), StandardCharsets.UTF_8)) {
            writer.write("Привет, мир!\r\n");
            writer.write("Это тестовый файл.\r\n");
        }

        try (InputStream inputStream = new FileInputStream(testFile)) {
            ArrayList<Integer> result = SubstringFinder.find(inputStream, "тест");
            assertEquals(List.of(17), result);
        }
    }

    @Test
    void testUTF8Characters() throws IOException {
        File testFile = tempDir.resolve("utf8.txt").toFile();

        try (OutputStreamWriter writer = new OutputStreamWriter(
                new FileOutputStream(testFile), StandardCharsets.UTF_8)) {
            writer.write("Emoji: 🎉 🚀\r\n");
        }

        try (InputStream inputStream = new FileInputStream(testFile)) {
            ArrayList<Integer> result = SubstringFinder.find(inputStream, "🎉");
            assertEquals(List.of(7), result);
        }
    }

    @Test
    void testLargeFile() throws IOException {
        File testFile = tempDir.resolve("large_file.txt").toFile();

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(testFile))) {
            String pattern = "abc123XYZ\r\n";
            for (int i = 0; i < 1000; i++) {
                writer.write(pattern);
            }
        }

        try (InputStream inputStream = new FileInputStream(testFile)) {
            ArrayList<Integer> result = SubstringFinder.find(inputStream, "123");
            assertEquals(1000, result.size());
            assertEquals(3, result.get(0).intValue());
        }
    }

    @Test
    void testSingleCharacterFile() throws IOException {
        File testFile = tempDir.resolve("single_char.txt").toFile();

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(testFile))) {
            writer.write("a");
        }

        try (InputStream inputStream = new FileInputStream(testFile)) {
            ArrayList<Integer> result = SubstringFinder.find(inputStream, "a");
            assertEquals(List.of(0), result);
        }
    }

    @Test
    void testSubstringLongerThanFile() throws IOException {
        File testFile = tempDir.resolve("short.txt").toFile();

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(testFile))) {
            writer.write("abc");
        }

        try (InputStream inputStream = new FileInputStream(testFile)) {
            ArrayList<Integer> result = SubstringFinder.find(inputStream, "abcdefgh");
            assertEquals(new ArrayList<>(), result);
        }
    }

    @Test
    void testExactMatch() throws IOException {
        File testFile = tempDir.resolve("exact.txt").toFile();

        String content = "exact content match";
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(testFile))) {
            writer.write(content);
        }

        try (InputStream inputStream = new FileInputStream(testFile)) {
            ArrayList<Integer> result = SubstringFinder.find(inputStream, content);
            assertEquals(List.of(0), result);
        }
    }

    @Test
    void testMultipleEmptyLines() throws IOException {
        File testFile = tempDir.resolve("empty_lines.txt").toFile();

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(testFile))) {
            writer.write("\n\n\n");
            writer.write("text\n");
            writer.write("\n\n");
        }

        try (InputStream inputStream = new FileInputStream(testFile)) {
            ArrayList<Integer> result = SubstringFinder.find(inputStream, "text");
            assertEquals(List.of(3), result);
        }
    }

    @Test
    void testNonExistentResource() {
        ArrayList<Integer> result = SubstringFinder.find("non_existent_file.txt", "test");
        assertEquals(new ArrayList<>(), result);
    }
}