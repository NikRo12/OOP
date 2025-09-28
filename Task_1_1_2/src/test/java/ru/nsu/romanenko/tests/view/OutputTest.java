package ru.nsu.romanenko.tests.view;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import ru.nsu.romanenko.view.Output;

import static org.junit.jupiter.api.Assertions.assertTrue;

class OutputTest {

    private final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
    private final PrintStream originalOut = System.out;

    @BeforeEach
    void setUp() {
        System.setOut(new PrintStream(outputStream));
    }

    @AfterEach
    void tearDown() {
        System.setOut(originalOut);
    }

    @Test
    void testPrintHello() {
        Output.printHello();
        String output = outputStream.toString();
        assertTrue(output.contains("Welcome to BlackJack"));
    }

    @Test
    void testPrintRound() {
        Output.printRound(1);
        String output = outputStream.toString();
        assertTrue(output.contains("Round 1"));
    }

    @Test
    void testPrintWin() {
        Output.printWin();
        String output = outputStream.toString();
        assertTrue(output.contains("You win"));
    }

    @Test
    void testPrintLose() {
        Output.printLose();
        String output = outputStream.toString();
        assertTrue(output.contains("You lost"));
    }
}