package ru.nsu.romanenko;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.io.ByteArrayInputStream;
import java.util.Scanner;

/**
 * Тесты для класса GamePlay.
 */
class GamePlayTest {

    @Test
    void testUserTurn() {
        PointsCounter user = new PointsCounter(false);
        PointsCounter dealer = new PointsCounter(true);
        Deck deck = new Deck();
        String input = "1\n0\n";
        Scanner scanner = new Scanner(new ByteArrayInputStream(input.getBytes()));

        GamePlay.gaming(user, dealer, deck, true, scanner);

        assertTrue(user.getPoints() >= 0);
    }

    @Test
    void testDealerTurn() {
        PointsCounter user = new PointsCounter(false);
        PointsCounter dealer = new PointsCounter(true);
        Deck deck = new Deck();
        Scanner scanner = new Scanner(System.in);

        GamePlay.gaming(user, dealer, deck, false, scanner);

        assertTrue(dealer.getPoints() >= 17 || dealer.getPoints() == 0);
    }
}