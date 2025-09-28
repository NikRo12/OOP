package ru.nsu.romanenko.tests.view;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import ru.nsu.romanenko.model.card.Card;
import ru.nsu.romanenko.model.card.Suit;
import ru.nsu.romanenko.model.card.Value;
import ru.nsu.romanenko.view.Output;

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
    void testPrintInfo() {
        String userInfo = "[TEN HEARTS (10), FIVE DIAMONDS (5)] => 15";
        String dealerInfo = "[KING CLUBS (10)] => 10";

        Output.printInfo(userInfo, dealerInfo);
        String output = outputStream.toString();

        assertTrue(output.contains("Your cards"));
        assertTrue(output.contains("Dealer's cards"));
    }

    @Test
    void testPrintStart() {
        String userInfo = "[TWO HEARTS (2), THREE SPADES (3)] => 5";
        String dealerInfo = "[QUEEN DIAMONDS (10)] => 10";

        Output.printStart(userInfo, dealerInfo);
        String output = outputStream.toString();

        assertTrue(output.contains("Dealer puts cards"));
        assertTrue(output.contains("Your move"));
    }

    @Test
    void testPrintDealerMove() {
        String userInfo = "[TEN HEARTS (10), SIX CLUBS (6)] => 16";
        String dealerInfo = "[ACE SPADES (11), FOUR DIAMONDS (4)] => 15";

        Output.printDealerMove(userInfo, dealerInfo);
        String output = outputStream.toString();

        assertTrue(output.contains("Dealer's move"));
        assertTrue(output.contains("Dealer opened closed card"));
    }

    @Test
    void testPrintOpenCardForUser() {
        Card card = new Card(Suit.HEARTS, Value.KING);

        Output.printOpenCard(card, 10, true);
        String output = outputStream.toString();

        assertTrue(output.contains("You've opened card:"));
        assertTrue(output.contains("KING HEARTS"));
    }

    @Test
    void testPrintOpenCardForDealer() {
        Card card = new Card(Suit.SPADES, Value.ACE);

        Output.printOpenCard(card, 11, false);
        String output = outputStream.toString();

        assertTrue(output.contains("Dealer opened card:"));
        assertTrue(output.contains("ACE SPADES"));
    }

    @Test
    void testPrintToGetCard() {
        Output.printToGetCard();
        String output = outputStream.toString();
        assertTrue(output.contains("Press \"1\" to get card or \"0\" to pass..."));
    }

    @Test
    void testPrintToGetCardError() {
        Output.printToGetCardError();
        String output = outputStream.toString();
        assertTrue(output.contains("Please enter only 1 or 0"));
    }

    @Test
    void testPrintScannerException() {
        Output.printScannerException();
        String output = outputStream.toString();
        assertTrue(output.contains("Input error, please try again"));
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

    @Test
    void testPrintResult() {
        Output.printResult(3, 2);
        String output = outputStream.toString();

        assertTrue(output.contains("Total score:"));
        assertTrue(output.contains("Your score is --- 3"));
        assertTrue(output.contains("Dealer's score is --- 2"));
    }

    @Test
    void testPrintFinalResultUserWins() {
        Output.printFinalResult(3, 1);
        String output = outputStream.toString();

        assertTrue(output.contains("GAME OVER"));
        assertTrue(output.contains("Congratulations!"));
    }

    @Test
    void testPrintFinalResultDealerWins() {
        Output.printFinalResult(1, 3);
        String output = outputStream.toString();

        assertTrue(output.contains("GAME OVER"));
        assertTrue(output.contains("Dealer wins"));
    }

    @Test
    void testPrintFinalResultTie() {
        Output.printFinalResult(2, 2);
        String output = outputStream.toString();

        assertTrue(output.contains("GAME OVER"));
        assertTrue(output.contains("It's a tie!"));
    }
}