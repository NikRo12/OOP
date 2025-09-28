package ru.nsu.romanenko.tests.model.game;

import org.junit.jupiter.api.Test;
import ru.nsu.romanenko.model.card.Deck;
import ru.nsu.romanenko.model.game.GamePlay;
import ru.nsu.romanenko.model.game.PointsCounter;

import java.io.ByteArrayInputStream;
import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.*;

public class GamePlayTest {

    @Test
    void testUserGameplayWithValidInput() {
        PointsCounter user = new PointsCounter(false);
        PointsCounter dealer = new PointsCounter(true);
        Deck deck = new Deck();

        String input = "0\n";
        Scanner scanner = new Scanner(new ByteArrayInputStream(input.getBytes()));

        assertDoesNotThrow(() -> {
            GamePlay.gaming(user, dealer, deck, true, scanner);
        });
    }

    @Test
    void testUserGameplayWithCardDrawing() {
        PointsCounter user = new PointsCounter(false);
        PointsCounter dealer = new PointsCounter(true);
        Deck deck = new Deck();

        String input = "1\n0\n";
        Scanner scanner = new Scanner(new ByteArrayInputStream(input.getBytes()));

        int initialPoints = user.getPoints();

        GamePlay.gaming(user, dealer, deck, true, scanner);

        assertTrue(user.getPoints() > initialPoints);
    }

    @Test
    void testUserGameplayWithInvalidInput() {
        PointsCounter user = new PointsCounter(false);
        PointsCounter dealer = new PointsCounter(true);
        Deck deck = new Deck();

        String input = "5\ninvalid\n2\n0\n";
        Scanner scanner = new Scanner(new ByteArrayInputStream(input.getBytes()));

        assertDoesNotThrow(() -> {
            GamePlay.gaming(user, dealer, deck, true, scanner);
        });
    }

    @Test
    void testDealerGameplay() {
        PointsCounter user = new PointsCounter(false);
        PointsCounter dealer = new PointsCounter(true);
        Deck deck = new Deck();
        Scanner scanner = new Scanner(System.in);

        dealer.appendCard(deck.getRandomCard());
        dealer.appendCard(deck.getRandomCard());

        int initialPoints = dealer.getPoints();

        GamePlay.gaming(user, dealer, deck, false, scanner);

        assertTrue(dealer.getPoints() >= 17 || dealer.getPoints() > initialPoints);
    }
}