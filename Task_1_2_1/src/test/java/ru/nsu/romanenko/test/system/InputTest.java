package ru.nsu.romanenko.test.system;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import ru.nsu.romanenko.system.Input;

/**
 * Test class for Input utility.
 */
class InputTest {

    @TempDir
    Path tempDir;

    @Test
    void testReadValidFile() throws IOException {
        Path testFile = tempDir.resolve("valid.txt");
        Files.write(testFile, "101\n010\n101".getBytes());

        ArrayList<String> result = Input.read(testFile.toString());
        assertEquals(3, result.size());
        assertEquals("101", result.get(0));
        assertEquals("010", result.get(1));
        assertEquals("101", result.get(2));
    }

    @Test
    void testReadFileWithSpaces() throws IOException {
        Path testFile = tempDir.resolve("with_spaces.txt");
        Files.write(testFile, "1 0 1\n0 1 0\n1 0 1".getBytes());

        ArrayList<String> result = Input.read(testFile.toString());
        assertEquals(3, result.size());
        assertEquals("101", result.get(0));
        assertEquals("010", result.get(1));
        assertEquals("101", result.get(2));
    }

    @Test
    void testReadFileWithEmptyLines() throws IOException {
        Path testFile = tempDir.resolve("empty_lines.txt");
        Files.write(testFile, "101\n\n010\n\n101".getBytes());

        ArrayList<String> result = Input.read(testFile.toString());
        assertEquals(3, result.size());
        assertEquals("101", result.get(0));
        assertEquals("010", result.get(1));
        assertEquals("101", result.get(2));
    }

    @Test
    void testReadFileWithInvalidCharacters() throws IOException {
        Path testFile = tempDir.resolve("invalid_chars.txt");
        Files.write(testFile, "101\n02a\n101".getBytes());

        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> Input.read(testFile.toString()));
        assertTrue(exception.getMessage().contains("invalid characters"));
    }

    @Test
    void testReadNonSquareMatrix() throws IOException {
        Path testFile = tempDir.resolve("non_square.txt");
        Files.write(testFile, "101\n01\n101".getBytes());

        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> Input.read(testFile.toString()));
        assertTrue(exception.getMessage().contains("not square"));
    }

    @Test
    void testReadEmptyFile() throws IOException {
        Path testFile = tempDir.resolve("empty.txt");
        Files.write(testFile, "".getBytes());

        ArrayList<String> result = Input.read(testFile.toString());
        assertTrue(result.isEmpty());
    }

    @Test
    void testReadNonExistentFile() {
        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> Input.read("non_existent_file.txt"));
        assertTrue(exception.getMessage().contains("Error reading file"));
    }

    @Test
    void testReadSingleLineMatrix() throws IOException {
        Path testFile = tempDir.resolve("single.txt");
        Files.write(testFile, "1".getBytes());

        ArrayList<String> result = Input.read(testFile.toString());
        assertEquals(1, result.size());
        assertEquals("1", result.get(0));
    }
}