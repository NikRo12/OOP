package ru.nsu.romanenko.viewTest;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.nsu.romanenko.view.Output;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.junit.jupiter.api.Assertions.*;

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

        outputStream.reset();
        Output.printRound(5);
        output = outputStream.toString();
        assertTrue(output.contains("Round 5"));
    }

    @Test
    void testPrintInfo() {
        String userInfo = "[TEN HEARTS (10), FIVE DIAMONDS (5)] => 15";
        String dealerInfo = "[KING CLUBS (10), <closed card>] => 10";

        Output.printInfo(userInfo, dealerInfo);
        String output = outputStream.toString();

        assertTrue(output.contains("Your cards"));
        assertTrue(output.contains("Dealer's cards"));
        assertTrue(output.contains(userInfo));
        assertTrue(output.contains(dealerInfo));
    }

    @Test
    void testPrintStart() {
        String userInfo = "[TWO HEARTS (2), THREE SPADES (3)] => 5";
        String dealerInfo = "[QUEEN DIAMONDS (10)] => 10";

        Output.printStart(userInfo, dealerInfo);
        String output = outputStream.toString();

        assertTrue(output.contains("Dealer puts cards"));
        assertTrue(output.contains("Your cards"));
        assertTrue(output.contains("Dealer's cards"));
        assertTrue(output.contains("Your move"));
        assertTrue(output.contains("-----------"));
    }

    @Test
    void testPrintDealerMove() {
        String userInfo = "[TEN HEARTS (10), SIX CLUBS (6)] => 16";
        String dealerInfo = "[ACE SPADES (11), FOUR DIAMONDS (4)] => 15";

        Output.printDealerMove(userInfo, dealerInfo);
        String output = outputStream.toString();

        assertTrue(output.contains("Dealer's move"));
        assertTrue(output.contains("-----------"));
        assertTrue(output.contains("Dealer opened closed card"));
        assertTrue(output.contains(userInfo));
        assertTrue(output.contains(dealerInfo));
    }

    @Test
    void testPrintOpenCardForUser() {
        // Создаем mock карту
        var card = new ru.nsu.romanenko.model.card.Card(
                ru.nsu.romanenko.model.card.Suit.HEARTS,
                ru.nsu.romanenko.model.card.Value.KING
        );

        Output.printOpenCard(card, 10, true);
        String output = outputStream.toString();

        assertTrue(output.contains("You've opened card:"));
        assertTrue(output.contains("KING HEARTS"));
        assertTrue(output.contains("(10)"));
    }

    @Test
    void testPrintOpenCardForDealer() {
        // Создаем mock карту
        var card = new ru.nsu.romanenko.model.card.Card(
                ru.nsu.romanenko.model.card.Suit.SPADES,
                ru.nsu.romanenko.model.card.Value.ACE
        );

        Output.printOpenCard(card, 11, false);
        String output = outputStream.toString();

        assertTrue(output.contains("Dealer opened card:"));
        assertTrue(output.contains("ACE SPADES"));
        assertTrue(output.contains("(11)"));
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

        assertTrue(output.contains("=== GAME OVER ==="));
        assertTrue(output.contains("Final score:"));
        assertTrue(output.contains("Your score is --- 3"));
        assertTrue(output.contains("Dealer's score is --- 1"));
        assertTrue(output.contains("Congratulations! You won the game!"));
    }

    @Test
    void testPrintFinalResultDealerWins() {
        Output.printFinalResult(1, 3);
        String output = outputStream.toString();

        assertTrue(output.contains("=== GAME OVER ==="));
        assertTrue(output.contains("Final score:"));
        assertTrue(output.contains("Your score is --- 1"));
        assertTrue(output.contains("Dealer's score is --- 3"));
        assertTrue(output.contains("Dealer wins the game. Better luck next time!"));
    }

    @Test
    void testPrintFinalResultTie() {
        Output.printFinalResult(2, 2);
        String output = outputStream.toString();

        assertTrue(output.contains("=== GAME OVER ==="));
        assertTrue(output.contains("Final score:"));
        assertTrue(output.contains("Your score is --- 2"));
        assertTrue(output.contains("Dealer's score is --- 2"));
        assertTrue(output.contains("It's a tie!"));
    }
}