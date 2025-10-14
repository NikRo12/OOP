package ru.nsu.romanenko.test;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.junit.jupiter.api.Timeout;
import ru.nsu.romanenko.SubstringFinder;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;

class SubstringFinderTest {

    @TempDir
    Path tempDir;

    // Базовые тесты
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
    void testNullSubstring() throws IOException {
        File testFile = tempDir.resolve("test.txt").toFile();

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(testFile))) {
            writer.write("Test content");
        }

        try (InputStream inputStream = new FileInputStream(testFile)) {
            ArrayList<Integer> result = SubstringFinder.find(inputStream, null);
            assertEquals(new ArrayList<>(), result);
        }
    }

    @Test
    void testEmptyFile() throws IOException {
        File testFile = tempDir.resolve("empty.txt").toFile();

        boolean fileCreated = testFile.createNewFile();

        try (InputStream inputStream = new FileInputStream(testFile)) {
            ArrayList<Integer> result = SubstringFinder.find(inputStream, "abc");
            assertEquals(new ArrayList<>(), result);
        }
    }

    // Тесты на граничные случаи
    @Test
    void testSubstringAtBeginning() throws IOException {
        File testFile = tempDir.resolve("beginning.txt").toFile();

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(testFile))) {
            writer.write("start of the content\n");
            writer.write("more content here\n");
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
            writer.write("This is some text\n");
            writer.write("with no matching pattern\n");
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
            writer.write("Case CASE case\n");
            writer.write("TEST test Test\n");
        }

        try (InputStream inputStream = new FileInputStream(testFile)) {
            ArrayList<Integer> result = SubstringFinder.find(inputStream, "CASE");
            assertEquals(List.of(5), result);
        }
    }

    // Тесты на специальные символы
    @Test
    void testSpecialCharacters() throws IOException {
        File testFile = tempDir.resolve("special.txt").toFile();

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(testFile))) {
            writer.write("Line with tabs\tand\ttabs\n");
            writer.write("Line with \\backslashes\\\n");
            writer.write("Line with $pecial $ymbols\n");
        }

        try (InputStream inputStream = new FileInputStream(testFile)) {
            ArrayList<Integer> result = SubstringFinder.find(inputStream, "\t");
            assertEquals(Arrays.asList(14, 18), result);
        }
    }

    @Test
    void testNewlineInSearch() throws IOException {
        File testFile = tempDir.resolve("newline.txt").toFile();

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(testFile))) {
            writer.write("First line\n");
            writer.write("Second line\n");
            writer.write("Third line\n");
        }

        try (InputStream inputStream = new FileInputStream(testFile)) {
            ArrayList<Integer> result = SubstringFinder.find(inputStream, "\n");
            assertEquals(Arrays.asList(10, 22, 33), result);
        }
    }

    // Тесты на кодировки
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
            assertEquals(List.of(17), result);
        }
    }

    @Test
    void testUTF8Characters() throws IOException {
        File testFile = tempDir.resolve("utf8.txt").toFile();

        try (OutputStreamWriter writer = new OutputStreamWriter(
                new FileOutputStream(testFile), StandardCharsets.UTF_8)) {
            writer.write("Emoji: 🎉 🚀\n");
        }

        try (InputStream inputStream = new FileInputStream(testFile)) {
            ArrayList<Integer> result = SubstringFinder.find(inputStream, "🎉");
            assertEquals(List.of(7), result);
        }
    }

    // Тесты на производительность и большие файлы
    @Test
    @Timeout(value = 10, unit = TimeUnit.SECONDS)
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

        try (InputStream inputStream = new FileInputStream(testFile)) {
            ArrayList<Integer> result = SubstringFinder.find(inputStream, searchString);

            assertEquals(expectedCount, result.size());

            if (!result.isEmpty()) {
                int firstExpectedPos = getFirstOccurrencePosition(pattern, searchString);
                assertEquals(firstExpectedPos, result.get(0).intValue());
            }
        }
    }

    @Test
    void testVeryLongLine() throws IOException {
        File testFile = tempDir.resolve("long_line.txt").toFile();

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(testFile))) {
            writer.write("abc123XYZ".repeat(10000));
        }

        try (InputStream inputStream = new FileInputStream(testFile)) {
            ArrayList<Integer> result = SubstringFinder.find(inputStream, "123");

            // Должно найти много вхождений
            assertTrue(result.size() > 1000);

            // Проверяем что все позиции корректны
            for (int i = 0; i < Math.min(10, result.size()); i++) {
                int pos = result.get(i);
                assertEquals(3, pos % 9, "Position " + i + " should be at correct offset");
            }
        }
    }

    // Тесты на edge cases
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
    void testWindowsLineEndings() throws IOException {
        File testFile = tempDir.resolve("windows.txt").toFile();

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(testFile))) {
            writer.write("First line\n");
            writer.write("Second line\n");
            writer.write("Third line\n");
        }

        try (InputStream inputStream = new FileInputStream(testFile)) {
            ArrayList<Integer> result = SubstringFinder.find(inputStream, "Second");
            assertEquals(List.of(11), result);
        }
    }

    @Test
    void testMixedLineEndings() throws IOException {
        File testFile = tempDir.resolve("mixed.txt").toFile();

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(testFile))) {
            writer.write("Unix line\n");
            writer.write("Windows line\r\n");
            writer.write("Mac line\r");
            writer.write("No newline");
        }

        try (InputStream inputStream = new FileInputStream(testFile)) {
            ArrayList<Integer> result = SubstringFinder.find(inputStream, "line");
            assertEquals(Arrays.asList(5, 18, 27, 38), result);
        }
    }

    @Test
    void testRepeatingPattern() throws IOException {
        File testFile = tempDir.resolve("repeat.txt").toFile();

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(testFile))) {
            for (int i = 0; i < 100; i++) {
                writer.write("pattern ");
            }
        }

        try (InputStream inputStream = new FileInputStream(testFile)) {
            ArrayList<Integer> result = SubstringFinder.find(inputStream, "pattern");
            assertEquals(100, result.size());

            for (int i = 1; i < result.size(); i++) {
                assertEquals(8, result.get(i) - result.get(i-1));
            }
        }
    }

    @Test
    void testBinaryData() throws IOException {
        File testFile = tempDir.resolve("binary.bin").toFile();

        try (OutputStream outputStream = new FileOutputStream(testFile)) {
            for (int i = 0; i < 100; i++) {
                outputStream.write(i);
            }
            outputStream.write("searchme".getBytes());
            for (int i = 0; i < 100; i++) {
                outputStream.write(i);
            }
        }

        try (InputStream inputStream = new FileInputStream(testFile)) {
            ArrayList<Integer> result = SubstringFinder.find(inputStream, "searchme");
            assertEquals(List.of(100), result);
        }
    }

    // Вспомогательные методы
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

    @Test
    void testNonExistentResource() {
        ArrayList<Integer> result = SubstringFinder.find("non_existent_file.txt", "test");
        assertEquals(new ArrayList<>(), result);
    }
}